package service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

import common.ResponseCode;
import common.ServerResponse;
import dao.ShippingMapper;
import pojo.Shipping;
import service.IShippingService;
import sun.security.provider.certpath.ResponderId;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService{
	
	@Autowired
	private ShippingMapper shippingMapper;
	
	
	//添加收货地址
	@Override
	public ServerResponse addShipping(Integer userId, Shipping shipping) {
		//将session中的userId覆盖前端传入的shipping中的userId，防止客户端横向越权使用别人的userId
		shipping.setUserId(userId);
		//插入地址到数据库
		//此处需要将插入数据库时的自增id返回给前端，因此需要使用id回填，将插入后的id回填到pojo类中
		//操作方法：在mapper.xml文件中找到此插入语句，在insert标签中插入 useGeneratedKeys="true" KeyProperty="id" （意思是使用主键回填，回填的主键是id）
		int rowCount = shippingMapper.insert(shipping);
		if(rowCount > 0){
			//插入成功的话，将回填好的id插入一个新建的map中，组成一个键值对格式返回给前端
			Map result = Maps.newHashMap();
			result.put("shippingId", shipping.getId());
			return ServerResponse.createBySuccessMessage("新建地址成功",result);
		}
		//插入失败
		return ServerResponse.createByErrorMessage("新建地址失败");
	}
	
	
	//删除收货地址
	@Override
	public ServerResponse deleteShipping(Integer userId,Integer shippingId) {
		int rowCount = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
		if(rowCount > 0){
			
			return ServerResponse.createBySuccessMessage("删除地址成功");
		}
		return ServerResponse.createBySuccessMessage("删除地址失败");

	}
	
	//更新收货地址
	@Override
	public ServerResponse updateShipping(Integer userId, Shipping shipping) {
		//将session中的userId覆盖前端传入的shipping中的userId，防止客户端横向越权使用别人的userId
		shipping.setUserId(userId);
		//插入地址到数据库
		//插入时候需要id回填，更新不需要，但是更新时候要注意update的时候不要更新用户id，用户id是不能变的。
		//直接从session中获得用户id，然后和前端传入的送货地址id一起，作为数据库更新的条件
		int rowCount = shippingMapper.updateByShipping(shipping);
		
		if(rowCount > 0){
			return ServerResponse.createBySuccessMessage("更新地址成功");
		}
		return ServerResponse.createByErrorMessage("更新地址失败");
	}
	
	
	//查找单一收货地址
	@Override
	public ServerResponse selectShipping(Integer userId,Integer shippingId) {
		Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
		if(shipping != null){
			return ServerResponse.createBySuccessMessage("查询成功", shipping);
		}
		return ServerResponse.createByErrorMessage("无法查询到改地址");
	}
	
	//列出当前用户的所有收货地址
	public ServerResponse list(Integer userId, int pageNum, int pageSize) {
		//分页开始
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
		//添加分页信息
		PageInfo pageInfo = new PageInfo(shippingList);
		//返回分页信息
		return ServerResponse.createBySuccess(pageInfo);
		
		
	}
}
