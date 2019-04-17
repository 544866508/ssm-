package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import pojo.Shipping;
import pojo.User;

@Repository
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    int deleteByUserIdAndShippingId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);

    int updateByShipping(Shipping shipping);
    
    Shipping selectByUserIdAndShippingId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);

    List<Shipping> selectByUserId(@Param("userId")Integer userId);
}