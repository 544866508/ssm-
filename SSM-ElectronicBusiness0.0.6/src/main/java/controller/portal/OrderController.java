package controller.portal;


import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import pojo.User;
import service.IOrderService;

@Controller
@RequestMapping("/order/")
public class OrderController {

	@Autowired
	private IOrderService iOrderService;
	
	private final static Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	//支付（根据前端传来的购买需求，将生成的二维码和数据库中的订单信息，产品信息返回给前端，让买家扫码付款给第三方）
	@RequestMapping("pay.do")
	@ResponseBody
	public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		String path = request.getSession().getServletContext().getRealPath("upload");
		return iOrderService.pay(orderNo, user.getId(), path);
	}
	
	//回调（付款后，第三方平台会返回一个异步通知，服务端对异步回调信息进行核对，从而确认订单是否交易成功，如果交易成功，需要向第三方返回其要求的内容）
	@RequestMapping("alipay_callback.do")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request){
		//接收回调信息，54-77行
		/**重要说明：
		 * 支付宝回调内容会全部放入request中，我们需要自己取。
		 * 回调内容是键值对的形式，并且一个键可能对应了多个值，采用一个不定义键值类型的map去取
		 * 接收回调信息后，将一对多转化为一对一，就是将map<String,String[]>转换为map<String,String>的形式（将String[]数组遍历并且以逗号隔开拼接	，变成一个String）
		 * 将转换好的一对一键值对信息存入一个新的map中，这样就完成了对支付宝回调信息的接收
		 */
		//声明一个map<String, String>，63-74行转换好的一对一键值对信息就存在这个map中
		Map<String, String> params = Maps.newHashMap();
		
		//声明一个map（不定义键和值的类型），用来接收支付宝的回调信息，回调信息可能一键对多值		
		Map requestParams = request.getParameterMap();
		//遍历map中每个键对应的多个值，放入一个string中以逗号隔开，然后将该键与string组成键值对存入新的map<String, String>中
		for(Iterator iterator = requestParams.keySet().iterator();iterator.hasNext();){
			String name = (String) iterator.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr ="";
			for(int i = 0; i < values.length; i++){
				valueStr = (i == values.length -1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		//完成一对多转化为一对一后，打印一下日志输出回调信息
		logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
	
		
		//验证回调信息
		//验证回调信息信息来源正确性81-95行（验证回调信息正确性，确实是支付宝发的，并且还要避免重复通知）	
		
		//验证信息时要去掉sign和sign_type（）
		params.remove("sign_type");
		try {
			//用第三方公钥解密，判断回调信息是否是由第三方支付平台私钥加密
			boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
			
			if(!alipayRSACheckedV2){
				//如果验证失败，说明信息被篡改，不继续执行，返回错误信息
				return ServerResponse.createByErrorMessage("非法请求，验证不通过，岂可修");
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝回调验证异常",e);
		}
		
		//验证回调信息内容正确性，97-104行
		//如果验证通过，则继续验证回调信息内容正确性（验证各种数据，就是预下单时发给前端，再由前端发送到第三方的有关订单内容的各种信息）
		ServerResponse serverResponse = iOrderService.aliCallBack(params);
		if(serverResponse.isSuccess()){
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	
	
	//服务端向前台返回二维码后，前端就开始轮询的查询第三方是否传来支付成功的信息，如果付款成功，就访问后端。
	//后端查询数据库，如果数据库的订单支付状态也是成功，那么后端向前端返回true，否则返回false（根据前后端约定的返回）。
	//然后前端根据后端传来的信息决定下一步给予买家的后续操作指引
	@RequestMapping("query_order_pay_status.do")
	@ResponseBody
	public ServerResponse queryOrderPayStatys(Session session, Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
		if(serverResponse.isSuccess()){
			return ServerResponse.createBySuccess(true);
		}
		return ServerResponse.createBySuccess(false);
	}
	
	
	
	
}
