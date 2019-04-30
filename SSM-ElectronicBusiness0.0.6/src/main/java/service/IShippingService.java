package service;

import common.ServerResponse;
import pojo.Shipping;

public interface IShippingService {

	//新增地址
	ServerResponse addShipping(Integer userId, Shipping shipping);
	
	//删除地址
	ServerResponse deleteShipping(Integer userId,Integer shippingId);

	//更细地址
	ServerResponse updateShipping(Integer userId, Shipping shipping);
	
	//查找单一地址
	ServerResponse selectShipping(Integer userId,Integer shippingId);
	
	//列出当前用户的所有地址
	ServerResponse list(Integer userId, int pageNum, int pageSize);
	
}

