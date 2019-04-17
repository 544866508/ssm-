package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import pojo.Cart;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    
    //通过购物车中的用户id和产品id，查找唯一对应的cart
    Cart selectCartByUserIdProductId(@Param("userId")Integer userId,@Param("productId")Integer productId);
    
    //列出所有该用户id的cart
    List<Cart> selectCartByUserId(Integer userId);
    
    //查找当前用户id下的购物车是否都被选中，如果是，返回的int = 0，不是，返回的int > 0(通过count（1）查询未选中产品的个数，如果是0，那么就是全部选中)
    int selectCartProductCheckedStatusByUserId(Integer userId);
    
    //删除购物车中的产品，返回删除条数
    int deleteByUserIdProductIds(@Param("userId")Integer userId,@Param("productIdList")List<String> productIdList);
    
    //全选或全反选产品
    int checkedOrUncheckedProduct(@Param("userId")Integer userId,@Param("productId")Integer productId,@Param("checked")Integer checked);
    
    //查询购物车中的产品数量
    //当购物车里没东西的时候返回的数量就是null，int的值不能为null，所以采用integer
    Integer selectCartProductCount(@Param("userId")Integer userId);
    
}