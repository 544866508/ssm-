package service.impl;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bigdecimaltest.BigDecimalTest;
import common.Const;
import common.ServerResponse;
import common.TokenCache;
import dao.UserMapper;
import pojo.User;
import service.IUserService;
import util.MD5Util;

@Service("iUserService")
public class UserServiceImpl implements IUserService{
	
	
	@Autowired
	private UserMapper userMapper;
	
	//登录
	@Override
	public ServerResponse<User> login(String username,String password){
		int resultCount = userMapper.checkUsername(username);
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		//密码登录MD5
		String md5password = MD5Util.md5Encrypt32Upper(password);
		
		User user = userMapper.selectLogin(username, md5password);
		if(user == null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccessMessage("登录成功", user);
	}
	
	//注册
	@Override
	public ServerResponse<String> register(User user) {
		ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if(!validResponse.isSuccess()){
			return validResponse;
		}
		validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
		if(!validResponse.isSuccess()){
			return validResponse;
		}		
		user.setRole(Const.role.ROLE_CUSTOMER);
		//MD5加密
		user.setPassword(MD5Util.md5Encrypt32Upper(user.getPassword()));
		
		int resultCount = userMapper.insert(user);
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}
	
	
	//校验用户名密码是否不存在
	@Override
	public ServerResponse<String> checkValid(String str,String type) {
		if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
			//开始校验
			if(Const.USERNAME.equals(type)){
				int resultCount = userMapper.checkUsername(str);
				if(resultCount > 0){
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if(Const.EMAIL.equals(type)){
				int resultCount = userMapper.checkEmail(str);
				if(resultCount > 0){
					return ServerResponse.createByErrorMessage("Email已经存在");
				}
			}			
		}else{
			return ServerResponse.createByErrorMessage("参数错误，用户名或邮箱");
		}
		return ServerResponse.createBySuccessMessage("校验成功");
	}
	
	//获取密码提示问题
	@Override
	public ServerResponse selectQuestion(String username) {
		ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
		if(validResponse.isSuccess()){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("找回密码的问题是空的");
	}
	
	//检查某个用户的用户名下的密码提示问题所对应的答案是否正确（检查用户名，问题，答案三者是否在数据库的同一个id下）
	@Override
	public ServerResponse<String> checkAnswer(String username,String question,String answer) {
		int resultCount = userMapper.checkAnswer(username, question, answer);
		if(resultCount > 0){
			//说明问题是这个用户的，并且问题的答案是正确的
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("问题的答案错误");
	}  
	
	//修改用户密码
	@Override
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		//验证客户端传入的token是否为空
		if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
			return ServerResponse.createByErrorMessage("参数错误，token为空白");
		}
		//验证用户名是否为空
		ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
		if(validResponse.isSuccess()){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		//提取key值为(token_username)的用户的服务端的token
		String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
		if(org.apache.commons.lang3.StringUtils.isBlank(token)){
			return ServerResponse.createByErrorMessage("token无效或者过期");
		}
		//比较客户端的token和服务端的token是否一致
		//采用StringUtils.equals比较，不用担心空指针异常
		if(org.apache.commons.lang3.StringUtils.equals(forgetToken, token)){
			String md5Password = MD5Util.md5Encrypt32Upper(passwordNew);
			int rowCount = userMapper.updatePasswordByUsername(username, md5Password);	
			
			if(rowCount > 0){
				return ServerResponse.createBySuccessMessage("修改密码成功");
			}
		}else{
			return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
		}
		
		return ServerResponse.createByErrorMessage("密码修改失败");
	}
	
	//已登录的用户修改密码
	@Override
	public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user) {
		//防止横向越权，要验证这个用户的旧密码，一定要指定是这个用户，因为这里会查询一个count（1），如果不指定id，那么结果就是true，因为count的值肯定大于0
		int resultCount =userMapper.checkPassword(MD5Util.md5Encrypt32Upper(passwordOld), user.getId());
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("旧密码错误");
		}
		
		user.setPassword(MD5Util.md5Encrypt32Upper(passwordNew));
		int updateCount = userMapper.updateByPrimaryKeySelective(user);
		if(updateCount > 0){
			return ServerResponse.createBySuccessMessage("密码更新成功");
		}
		
		return ServerResponse.createByErrorMessage("密码更新失败");
	}	
	
	public ServerResponse<User> updateInformation(User user) {
		//username是不能被更新的
		//email也要进行校验，校验email是否已存在，并且存在的email如果相同的话，不能是当前这个用户的。
		int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if(resultCount > 0){
			return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");
		}
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());
		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if(updateCount > 0){
			return ServerResponse.createBySuccessMessage("更新个人信息成功", updateUser);
		}
		return ServerResponse.createByErrorMessage("更新个人信息失败");
	}
	
	public ServerResponse<User> getINformation(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if(user == null){
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
	}
	
	
	//backend
	//检查是否是管理员
	public ServerResponse checkAdminRole(User user) {
		if(user != null && Const.role.ROLE_ADMIN == user.getRole()){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}
}
