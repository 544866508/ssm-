package controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import common.Const;
import common.ServerResponse;
import pojo.User;
import service.IUserService;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {
	
	@Autowired
	private IUserService iUserService;
	
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session) {
		//调用iUserService的login方法登录管理员
		ServerResponse<User> response = iUserService.login(username, password);
		if(response.isSuccess()){
			User user = response.getData();
			//判断role的值是否是1，1就是管理员，是1的话就把user加入session中
			if(user.getRole() == Const.role.ROLE_ADMIN){
				session.setAttribute(Const.CURRENT_USER, user);
				return response;
			}else{
				return ServerResponse.createByErrorMessage("不是管理员，无法登录");
			}
		}
		return response;
	}
}
