package service;

import java.util.Map;

import common.ServerResponse;

public interface IOrderService {

	//支付
	ServerResponse pay(Long orderNo, Integer userId, String path);
	
	//回调信息验证
	ServerResponse aliCallBack(Map<String, String> params);
	
	//
	ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
