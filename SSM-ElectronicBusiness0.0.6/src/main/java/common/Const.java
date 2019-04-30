package common;

import java.util.Set;

import com.google.common.collect.Sets;

public class Const {

	 public static final String CURRENT_USER = "currentUser";
	 
	 public static final String EMAIL = "email";
	 public static final String USERNAME = "username";
	 
	 public interface ProductListOrderBy{
		Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
	}

	 public interface role{
		int ROLE_CUSTOMER = 0;  //普通用户
		int ROLE_ADMIN = 1;		//管理员
	}
	 
	 public interface Cart{
		int CHECKED = 1;//购物车选中状态
		int UN_CHECKED = 0;//购物车未选中状态
		
		String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
		
	}
	 
	 public enum OrderStatusEnum{
		 
		CANCELED(0,"已取消"),
		NO_PAY(10,"未支付"),
		PAIED(20,"已付款"),
		SHIPPED(40,"已发货"),
		ORDER_SUCCESS(50,"订单完成"),
		ORDER_CLOSE(60,"订单关闭");
		 
		private String value;
		private int code;
		OrderStatusEnum(int code,String value){
			this.code = code;
			this.value = value;
		}
			
		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}
	}
	 
	 
	 public enum ProductStatusEnum{
		ON_SALE(1,"在线");

		private String value;
		private int code;
		ProductStatusEnum(int code,String value){
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}
		

		
//		public void setValue(String value) {
//		this.value = value;
//	}
//		public void setCode(int code) {
//			this.code = code;
//		}

	}
	 
	 
		
	public interface AlipayCallback{
		String TRADE_STATUS_WAIT_BUYER_PAYString = "WAIT_BUYER_PAY";
		String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
		
		String RESPONSE_SUCCESS = "success";
		String RESPONSE_FAILED = "failed";
	}
	 
	 
	//支付平台枚举
	public enum PayPlatformEnum{
		
		ALIPAY(1,"支付宝");
		//其它支付平台尚未集成
		
		private String value;
		private int code;
		PayPlatformEnum(int code,String value){
			this.code = code;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}
	}
	 
}
