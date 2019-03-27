package controller;
 
import javax.annotation.Resource;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import entity.User;
import service.UserService;
 
@Controller
@RequestMapping("/user")
public class UserController {
 
	@Resource(name="UserService")
	private UserService userService;
 
	@RequestMapping(value="/findUserById",method=RequestMethod.POST)
	public String findUserById(int id, Model model) {
		User username = userService.findUserById(id);
		if (username != null) {
			model.addAttribute("name", username.getUserame());
			model.addAttribute("id", username.getId());
		} else {
			model.addAttribute("username", "未找到");
		}
		return "test";
	}
 
}
