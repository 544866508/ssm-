package service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.Select;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import dao.CartMapper;
import dao.ProductMapper;
import dao.UserMapper;
import pojo.Cart;
import pojo.Product;
import service.ICartService;
import util.BigDecimalUtil;
import util.PropertiesUtil;
import vo.CartProductVo;
import vo.CartVo;



@Service("iCartService")
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	//往购物车添加产品
	@Override
	public ServerResponse add(Integer userId,Integer productId,Integer count) {
		//判断是否传入参数
		if(productId == null || count == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		
		//首先看购物车里是否有这个产品
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart == null){
			//这个产品不在这个购物车里，需要新增一个这个产品的记录
			Cart cartItem = new Cart();
			cartItem.setQuantity(count);
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setProductId(productId);
			cartItem.setUserId(userId);
			
			cartMapper.insert(cartItem);
		}else{
			//这个产品已经在购物车里了
			//如果产品已经存在，那么数量相加
			count = cart.getQuantity() + count;
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		
		//然后通过getCartVoLimit方法获得从数据库中计算得到CartVo（就是该用户的购物车），将其返回给前端
		//getCartVolimit方法已经封装在listProduct中
		return this.listProduct(userId);
	}
	
	
	//此方法我做了修改，不知道是不是可以让程序更严谨，或者我只是加了一句罗里吧嗦的废话
	//更新购物车产品,传入用户id，产品id，更新后的产品数量
	@Override
	public ServerResponse update(Integer userId,Integer productId,Integer count) {
		//判断是否传入参数
		if(productId == null || count == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		//首先看购物车里是否有这个产品
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);

		if(cart != null){
			//如果有，更新产品的数量
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
			//更新成功后返回购物车给前端
			return this.listProduct(userId);
		}
		//如果没有查到，返回错误信息
		return ServerResponse.createByErrorMessage("购物车更新失败");

	}
	
	
	//此方法我做了修改，不知道是不是可以让程序更严谨，或者我只是加了一句罗里吧嗦的废话	
	//从购物车中删除产品
	@Override
	public ServerResponse deteleProduct(Integer userId,String productIds) {
		//分割前端传递过来的字符串（该字符串是需要删除的产品，以逗号分割）
		//采用google的guava提供的 十分方便 的分割字符串方法
		List<String> productIdList = Splitter.on(",").splitToList(productIds);
		if(CollectionUtils.isEmpty(productIdList)){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		int deleteCount = cartMapper.deleteByUserIdProductIds(userId, productIdList);
		
		if(deleteCount == 0){
			return ServerResponse.createByErrorMessage("删除产品失败");
		}
		
		//删除成功后返回购物车给前端
		return this.listProduct(userId);
		
	}
	
	//查询购物车产品
	@Override
	public ServerResponse listProduct(Integer userId) {
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}
	
	//产品勾选，全选、全反选、单独选、单独反选
	//传入productId的话就是单独选或单独反选，不传就是全选或者全反选
	@Override
	public ServerResponse selectOrUnselect(Integer userId,Integer productId,Integer checked) {
		cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
		return this.listProduct(userId);
	}
	
	
	//查询购物车内产品数量
	@Override	
	public ServerResponse getCartProductCount(Integer userId){
		if(userId == null){
			//未登录用户查询购物车不返回错误，只返回一个date为0的serverResponse(未登录用户的购物车数据啥都没有，所以给他一个0)
			return ServerResponse.createBySuccess(0);
		}
	    //当购物车里没东西的时候返回的数量就是null，int的值不能为null，所以采用integer
		Integer productNum = cartMapper.selectCartProductCount(userId);
		if(productNum == null){
			productNum = 0;
		}
		return ServerResponse.createBySuccess(productNum);
	}
	
	
//--------------------------------------------------------------------------------------	

	//此方法将数据库中的信息通过 各种计算 或者 直接 添加到cartVo中，并返回cartVo
	private CartVo getCartVoLimit(Integer userId){
		//声明一个总vo（此vo包含volist，volist的总价，和产品的imageHost）
		CartVo cartVo = new CartVo();
		//声明vo
		List<CartProductVo> cartProductVoList = Lists.newArrayList();
		//初始化volist的总价
		BigDecimal cartTotalPrice = new BigDecimal("0");
		
		
		//以下操作将pojo转化成vo
		//查询同一个用户id下的所有pojo
		List<Cart> cartList = cartMapper.selectCartByUserId(userId);
		if(CollectionUtils.isNotEmpty(cartList)){//CollectionUtils.isNotEmpty() 包含null,size=0等多种情况
			for(Cart cartItem : cartList){
				//先将pojo放入vo中
				CartProductVo cartProductVo = new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserId(cartItem.getUserId());
				cartProductVo.setProductId(cartItem.getProductId());
				//再通过pojo中的产品id，查询产品的详细信息放入vo中
				Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
				if(product != null){
					cartProductVo.setProductMainImage(product.getMainImage());
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductPrice(product.getPrice());
					cartProductVo.setProductStock(product.getStock());
					//判断库存
					//初始化用户购买数量
					int buyLimitCount = 0;
					if(product.getStock() >= cartItem.getQuantity()){
						//库存大于等于购买数量，在vo的setLimitQuantity中放入字符串表示veryOK
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
						//buyLimitCount设为用户的购买数量
						buyLimitCount = cartItem.getQuantity();
					}else{
						//否则，在vo的setLimitQuantity中放入字符串表示库存不够
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
						//buyLimitCount设为产品的库存数量
						buyLimitCount = product.getStock();
						//然后将用户的购买数量更新到数据库的cart中
						Cart cartForQuantity = new Cart();
						cartForQuantity.setId(cartItem.getId());
						cartForQuantity.setQuantity(buyLimitCount);
						cartMapper.updateByPrimaryKeySelective(cartForQuantity);

					}
					cartProductVo.setQuantity(buyLimitCount);
					//计算购物车中某一个产品的总价，单价*数量
					cartProductVo.setProductTotalPrice(BigDecimalUtil.multiply(product.getPrice().doubleValue(), cartProductVo.getQuantity().doubleValue()));
					cartProductVo.setProductChecked(cartItem.getChecked());
				}
				
				//判断vo中的Checked值是否为被选中
				if(cartItem.getChecked()  == Const.Cart.CHECKED){
					//如果已经勾选，将该商品的总价增加到购物车的总价中
					cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
				}
				//将以上所有从奇怪的地方计算出来的vo放入声明好的list中，一会要将这一大堆产品扔进购物车
				cartProductVoList.add(cartProductVo);
				
			}
		}
		
		cartVo.setCartTotalPrice(cartTotalPrice);
		cartVo.setCartProductVoList(cartProductVoList);
		cartVo.setAllChecked(getAllCheckedStatus(userId));
		cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		
		return cartVo;
	}
	
	//判断购物车是否全选的方法
	private boolean getAllCheckedStatus(Integer userId) {
		if(userId == null){
			return false;
		}
		//查找当前用户id下的购物车是否都被选中，如果是，返回的int = 0，不是，返回的int > 0
		return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
	}
	
}
