package service;



import common.ServerResponse;
import pojo.User;
public interface IUserService {
	ServerResponse<User> login(String username,String password);
	
	ServerResponse<String> register(User user);
	
	ServerResponse<String> checkValid(String str,String type); 
	
	ServerResponse selectQuestion(String username);
	
	ServerResponse<String> checkAnswer(String username,String question,String answer);
	
	//未登录用户通过密码提示问题修改密码
	ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);
	
	//已登录的用户修改密码
	ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

	//更新个人信息
	ServerResponse<User> updateInformation(User user);
	
	//获取个人信息
	ServerResponse<User> getINformation(Integer userId);
	
	//检查是不是管理员
	ServerResponse checkAdminRole(User user);
	
}
