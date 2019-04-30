package controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import jdk.nashorn.internal.ir.annotations.Ignore;
import pojo.Shipping;
import pojo.User;
import service.IShippingService;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

	
	@Autowired
	private IShippingService iShippingService;
	
	
	//新增地址
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse add(HttpSession session, Shipping shipping) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iShippingService.addShipping(user.getId(), shipping);
	}
	
	
	//删除地址
	@RequestMapping("del.do")
	@ResponseBody
	public ServerResponse del(HttpSession session, Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iShippingService.deleteShipping(user.getId(), shippingId);
	}
	
	//更新地址
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse update(HttpSession session, Shipping shipping) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iShippingService.updateShipping(user.getId(), shipping);
	}
	
	//查询单个地址
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse select(HttpSession session, Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iShippingService.selectShipping(user.getId(), shippingId);
	}
	
	
	//列出所有地址
	//使用分页插件pagehelper
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1")int pageNum, 
	@RequestParam(value = "pageSize", defaultValue = "10")int pageSize, 
	HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iShippingService.list(user.getId(), pageNum, pageSize);
	}
	
	
}
