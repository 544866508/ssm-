package controller.portal;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import pojo.User;
import service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	//登录
	@RequestMapping(value = "login.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session) {
		ServerResponse<User> response = iUserService.login(username, password);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER,response.getData());
		}
		return response;
	}
	
	//登出
	@RequestMapping(value = "logout.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	
	//注册
	@RequestMapping(value = "register.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user) {
		return iUserService.register(user);
	}
	
	//校验用户名和邮箱
	@RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type) {
		return iUserService.checkValid(str, type);
	}
	
	//获取用户信息
	@RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user != null){
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
	}
	
	//获取密码提示问题
	@RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
	@ResponseBody	
	public ServerResponse<String> forgetGetQuestion(String username) {
		return iUserService.selectQuestion(username);
	}
	
	//验证问题的答案并返回uuid加密过的token给客户端
	//todo(guava报错用不了，改用适合高并发的redis将token存入服务器	缓存)
	@RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer) {
		return iUserService.checkAnswer(username, question, answer);
	}
	
	//通过客户端的token和服务器的token比较，对用户密码进行修改
	@RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetRsetPassword(String username,String passwordNew,String forgetToken){
		return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
	}
	
	//登录后对密码进行修改
	@RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew) {
		//通过session中是否存在currentUser来判断用户是否已经登录
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		return iUserService.resetPassword(passwordOld, passwordNew, user);
	}
	
	//更新用户信息
	//需要把完整用户信息返回给前端，所以泛型采用user类
	@RequestMapping(value = "update_information.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> update_information(HttpSession session,User user) {
		//通过session中是否存在currentUser来判断用户是否已经登录
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		//为了防止横向越权，将客户端传入的用户id设置为从session中获取的用户id，防止id被变化
		user.setId(currentUser.getId());
		//同样，用户名也是不变的
		user.setUsername(currentUser.getUsername());
		
		ServerResponse<User> response = iUserService.updateInformation(user);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	
	//获取个人信息
	@RequestMapping(value = "get_information.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> get_information(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status==10");
		}	
		return iUserService.getINformation(currentUser.getId());
	}
	
	

	
}

