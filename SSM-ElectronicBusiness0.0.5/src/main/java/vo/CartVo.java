package vo;

import java.math.BigDecimal;
import java.util.List;


//抽象对象：购物车CartVo
public class CartVo {

	
	private List<CartProductVo> cartProductVoList;
	private BigDecimal cartTotalPrice;
	private Boolean allChecked;//购物车里的每个CartProductVo是否都勾选了
	private String imageHost;
	
	
	

public List<CartProductVo> getCartProductVoList() {
		return cartProductVoList;
	}
	public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
		this.cartProductVoList = cartProductVoList;
	}
	public BigDecimal getCartTotalPrice() {
		return cartTotalPrice;
	}
	public void setCartTotalPrice(BigDecimal cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}
	public Boolean getAllChecked() {
		return allChecked;
	}
	public void setAllChecked(Boolean allChecked) {
		this.allChecked = allChecked;
	}
	public String getImageHost() {
		return imageHost;
	}
	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}

}

