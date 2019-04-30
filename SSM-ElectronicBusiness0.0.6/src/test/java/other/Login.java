package other;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dao.CartMapper;
import dao.ShippingMapper;
import dao.UserMapper;
import pojo.Cart;
import pojo.Shipping;


@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件位置
@ContextConfiguration({ "classpath:spring/dao.xml", "classpath:spring/service.xml" })
public class Login {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private ShippingMapper shippingMapper;
	
	
	@Test
	public void test1(){

		Shipping shipping = new Shipping();
	
		int rowCount = shippingMapper.updateByShipping(shipping);
		
		
	}
	
	

}
