package dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import pojo.Order;

@Repository("orderMapper")
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    Order selectByUserIdAndOredrNo(@Param("userId")Integer userId, @Param("orderNo")Long orderNo);
    
    Order selectByOrderNo(Long orderNo);
}