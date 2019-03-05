package controller;
 
import javax.annotation.Resource;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

 

import service.UserService;
 
@Controller
@RequestMapping("/user")
public class UserController {
 
	@Resource(name="UserService")
	private UserService userService;
 
	@RequestMapping("/findUserById")
	public String findUserById(int id, Model model) {
		String username = userService.findUserById(id);
		if (username != null) {
			model.addAttribute("username", username);
		} else {
			model.addAttribute("username", "未找到");
		}
		return "test";
	}
 
}
