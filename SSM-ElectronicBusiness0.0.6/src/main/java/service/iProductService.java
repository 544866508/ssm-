package service;

import common.ServerResponse;
import pojo.Product;

public interface iProductService {
	//后台操作
	//新增或更新产品
	ServerResponse saveOrUpdateProduct(Product product);
	
	//修改产品销售状态
	ServerResponse setSaleStatus(Integer productId,Integer status);
	
	//获取产品详细信息vo，已经下架的产品也会列出
	ServerResponse manageProductDetail(Integer productId);
	
	//列出所有产品的基本信息vo
	ServerResponse getProductList(int pageNum,int pageSize);
	
	//列出所有产品的基本信息vo
	ServerResponse searchProduct(String productName,Integer productId,int pageNum,int pageSize);
	
	//前台操作
	//获取产品详细信息vo，已经下架的产品就不会列出了
	ServerResponse getProductDetail(Integer productId);
	
	//根据关键字和categoryId搜索产品
	ServerResponse getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
