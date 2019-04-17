package service;

import common.ServerResponse;
import pojo.Product;

public interface ICartService {
	
	//往购物车里添加商品，传入用户id，产品id，产品数量
	ServerResponse add(Integer userId,Integer productId,Integer count);
	
	//更新购物车里的商品
	ServerResponse update(Integer userId,Integer productId,Integer count);
	
	//从购物车中删除产品
	ServerResponse deteleProduct(Integer userId,String productIds);
	
	//查询购物车中的产品
	ServerResponse listProduct(Integer userId);
	
	//全选产品或者全反选产品
	ServerResponse selectOrUnselect(Integer userId,Integer productId,Integer checked);
	
	//查询购物车内的产品总数 
	ServerResponse getCartProductCount(Integer userId);
}
