package controller.portal;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import pojo.User;
import service.ICartService;




@Controller
@RequestMapping("/cart/")
public class CartController {
	
	
	@Autowired
	private ICartService iCartService;
	
	
	//购物车添加产品
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse addProduct(HttpSession session,Integer count,Integer productId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.add(user.getId(), productId, count);
	}
	
	//购物车更新产品
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse updateProduct(HttpSession session,Integer count,Integer productId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.update(user.getId(), productId, count);
	}
	
	//购物车删除产品
	@RequestMapping("delete.do")
	@ResponseBody
	public ServerResponse deteleProduct(HttpSession session,String productIds) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.deteleProduct(user.getId(), productIds);
	}
	
	//查询购物车产品
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse listProduct(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.listProduct(user.getId());
	}
	
	//全选产品
	//传入productId的话就是单独选或单独反选，不传就是全选或者全反选
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse selectAll(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.selectOrUnselect(user.getId(), null, Const.Cart.CHECKED);
	}
	
	//全反选产品
	//传入productId的话就是单独选或单独反选，不传就是全选或者全反选
	@RequestMapping("un_select_all.do")
	@ResponseBody
	public ServerResponse unSelectAll(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.selectOrUnselect(user.getId(), null, Const.Cart.UN_CHECKED);
	}
	
	//单独选
	//传入productId的话就是单独选或单独反选，不传就是全选或者全反选
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse select(HttpSession session,Integer productId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.selectOrUnselect(user.getId(), productId, Const.Cart.CHECKED);
	}
	
	//单独反选产品
	//传入productId的话就是单独选或单独反选，不传就是全选或者全反选
	@RequestMapping("un_select.do")
	@ResponseBody
	public ServerResponse unSelect(HttpSession session,Integer productId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		//业务
		return iCartService.selectOrUnselect(user.getId(), productId, Const.Cart.UN_CHECKED);
	}
	

	//查询当前用户的购物车里的产品数量（不按产品种类算，按产品个数算，如果一种产品的个数为10个，那么数量就是10，而不是1）
	@RequestMapping("get_cart_product_count.do")
	@ResponseBody
	public ServerResponse getCartProductCount(HttpSession session) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			//未登录用户查询购物车不返回错误，只返回一个date为0的serverResponse(未登录用户的购物车数据啥都没有，所以给他一个0)
			return ServerResponse.createBySuccess(0);
		}
		//业务
		return iCartService.getCartProductCount(user.getId());
	}
	

	

	


	
	
}
