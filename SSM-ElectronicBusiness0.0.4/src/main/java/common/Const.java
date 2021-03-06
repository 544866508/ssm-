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
}
