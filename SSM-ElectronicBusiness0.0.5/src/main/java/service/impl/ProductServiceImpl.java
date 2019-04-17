package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import dao.CategoryMapper;
import dao.ProductMapper;
import pojo.Category;
import pojo.Product;
import service.iCategoryService;
import service.iProductService;
import sun.net.www.content.text.plain;
import util.DateTimeUtil;
import util.PropertiesUtil;
import vo.ProductDetailVo;
import vo.ProductListVo;

@Service("iProductService")
public class ProductServiceImpl implements iProductService{
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private iCategoryService iCategoryService;
	
	
	//后台操作
	//添加或更新product
	@Override
	public ServerResponse saveOrUpdateProduct(Product product) {
		
		//设置mainImage
		if(product != null){
			if(StringUtils.isNotBlank(product.getSubImages())){
				//根据subImages图片字符串中的逗号，将其分割成字符串数组
				String[] subImageArray = product.getSubImages().split(",");
				if(subImageArray.length > 0){
					//将subImage字符串数组中的第一个字符串作为mainImage
					product.setMainImage(subImageArray[0]);
				}
				
			}
			if(product.getId() != null){
				int rowCount = productMapper.updateByPrimaryKeySelective(product);
				if(rowCount > 0){
					return ServerResponse.createBySuccessMessage("更新产品成功");
				}else{
					ServerResponse.createByErrorMessage("更新产品失败");
				}

			}else{
				int rowCount = productMapper.insertSelective(product);
				if(rowCount > 0){
				return ServerResponse.createBySuccessMessage("新增产品成功");
				}else{
					ServerResponse.createByErrorMessage("新增产品失败");
				}
			}
		}
		
		return ServerResponse.createByErrorMessage("新增或新增产品参数错误");
	}
	
	
	//修改产品销售状态status
	@Override
	public ServerResponse setSaleStatus(Integer productId,Integer status) {
		if(productId == null || status == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int rowCount = productMapper.updateByPrimaryKeySelective(product);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
		}
		return ServerResponse.createByErrorMessage("修改产品销售状态失败");
	}
	
	//获取产品详细信息vo
	@Override
	public ServerResponse manageProductDetail(Integer productId) {
		if(productId == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if(product == null){
			return ServerResponse.createByErrorMessage("产品已下架或者删除");
		}
		//vo对象--value object
		//pojo->bo(business object)->vo(view object)
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}
	
	private ProductDetailVo assembleProductDetailVo(Product product){
		ProductDetailVo productDetailVo = new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setSubImage(product.getSubImages());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setName(product.getName());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setStock(product.getStock());
		
		//imageHost
		productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happmmall.com/"));
		
		//parentCategory
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if(category == null){
			productDetailVo.setCategoryId(0);//默认为根节点
		}else{
			productDetailVo.setParentCategoryId(category.getParentId());
		}
		
		//createTime
		productDetailVo.setCreateTime(DateTimeUtil.DateToStr(product.getCreateTime()));
		
		//updateTime
		productDetailVo.setUpdateTime(DateTimeUtil.DateToStr(product.getUpdateTime()));
		
		
		return productDetailVo;
	}
	
	
	//列出所有产品的基本信息
	@Override
	public ServerResponse getProductList(int pageNum,int pageSize) {
		//startPage--start
		PageHelper.startPage(pageNum,pageSize);
		//填写自己的sql查询逻辑
		List<Product> productList = productMapper.selectList();
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for(Product product : productList){
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		//pageHelper-收尾
		/*
		* 通过pageHelper 的底层原理是基于aop的,aop需要切点,可以理解为执行dao层就是这个切点
		* 需要通过执行dao层,才可以动态的进行分页信息的添加
		* vo层的productListVoList没有dao层切点，无法触发pageHelper的动态分页
		* 又因为vo层的productListVoList和pojo层的productList所需的分页信息一致
		* 所以只能通过pojo层的productList执行dao层，触发pageHelper的动态分页
		* 具体操作方法如下：
		* 1.使用productList进行 pageInfo的初始化 ，触发pageHelper的动态分页,将分页信息储存在pageInfo中
		* 2. 替换需要显示的list , 因为productListVoList和productList所需的分页信息一致
		*/
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
		
		
		
	}
	
	
	private ProductListVo assembleProductListVo(Product product){
		ProductListVo productListVo = new ProductListVo();

		productListVo.setId(product.getId());
		productListVo.setName(product.getName());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happmmall.com/"));
		productListVo.setMainImage(product.getSubImages());
		productListVo.setPrice(product.getPrice());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setStatus(product.getStatus());
		
		return productListVo;
		
	}
	
	//根据名称或id对产品查找
	@Override	
	public ServerResponse searchProduct(String productName,Integer productId,int pageNum,int pageSize) {
		//startPage--start
		PageHelper.startPage(pageNum,pageSize);
		//填写自己的sql查询逻辑
		if(StringUtils.isNotBlank(productName)){
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for(Product product : productList){
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		//pageHelper-收尾
		/*
		* 通过pageHelper 的底层原理是基于aop的,aop需要切点,可以理解为执行dao层就是这个切点
		* 需要通过执行dao层,才可以动态的进行分页信息的添加
		* vo层的productListVoList没有dao层切点，无法触发pageHelper的动态分页
		* 又因为vo层的productListVoList和pojo层的productList所需的分页信息一致
		* 所以只能通过pojo层的productList执行dao层，触发pageHelper的动态分页
		* 具体操作方法如下：
		* 1.使用productList进行 pageInfo的初始化 ，触发pageHelper的动态分页,将分页信息储存在pageInfo中
		* 2. 替换需要显示的list , 因为productListVoList和productList所需的分页信息一致
		*/
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(pageResult);
	}
	
	//前台操作
	//获取产品详细信息vo，已经下架的产品就不会列出了
	@Override	
	public ServerResponse getProductDetail(Integer productId) {
		if(productId == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if(product == null){
			return ServerResponse.createByErrorMessage("产品已删除");
		}
		if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
			return ServerResponse.createByErrorMessage("产品已下架");
		}
		//vo对象--value object
		//pojo->bo(business object)->vo(view object)
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}
	
	//根据关键字和categoryId搜索的方法
	public ServerResponse getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy) {
		if(StringUtils.isBlank(keyword) && categoryId ==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		List<Integer> categoryIdList = new ArrayList<Integer>();
		
		if(categoryId != null){
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if(category == null && StringUtils.isBlank(keyword)){
				//没有该分类，又没有关键字，就返回一个空结果集
				PageHelper.startPage(pageNum,pageSize);
				List<ProductListVo> productListVoList = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(productListVoList);
				return ServerResponse.createBySuccess(pageInfo);
			}
			categoryIdList =(List<Integer>) iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
		}
		if(StringUtils.isNotBlank(keyword)){
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		//分页开始
		PageHelper.startPage(pageNum,pageSize);
		//分页的排序处理，约定前端传入排序方式为String类型，格式为“要排序的字段_排序方式”，例如“price_desc”以价格降序
		//通过前端传入的String类型的orderBy判断根据什么排序，升序还是降序
		//如果orderBy为空，那么pagehelper默认按照升序排列
		if(StringUtils.isNotBlank(orderBy)){
			if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
				String[] orderByArray = orderBy.split("_");
				//拆分字符串后，加入pagehelper中
				PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
			}
		}
		//查询数据库中的产品，pojo类型
		//触发pagehelper的aop切点,对pojo类型的产品，动态的进行分页信息的添加
		List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);
		
		//将pojo类型的产品转换成vo类型的产品
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for(Product product : productList){
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}
		//获得pojo类型产品的分页信息
		PageInfo pageInfo = new PageInfo(productList);
		//将分页信心中的pojo类型的产品，替换成vo类型的产品
		//最终得到vo类型产品的分页信息
		pageInfo.setList(productListVoList);
		
		//将分页信息返回给前端
		return ServerResponse.createBySuccess(pageInfo);
	}
	
}
