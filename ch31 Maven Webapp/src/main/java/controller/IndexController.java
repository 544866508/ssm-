package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	@RequestMapping("/findUserById")
	public String findUserById(){
		return "findUserById";
	}
}
