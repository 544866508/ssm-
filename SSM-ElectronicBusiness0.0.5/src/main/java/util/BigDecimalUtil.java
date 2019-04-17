package util;

import java.math.BigDecimal;


//传入两个double类型，转换成bigdecimal类型后计算，再传出bigdecimal类型
public class BigDecimalUtil {
	
	private BigDecimalUtil(){
		
	}
	
	//+
	public static BigDecimal add(double v1,double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2);
	}
	
	//-
	public static BigDecimal subtract(double v1,double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2);
	}
	
	//*
	public static BigDecimal multiply(double v1,double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}
	
	///
	public static BigDecimal divide(double v1,double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//除不尽则保留两位小数，尾数进行四舍五入
	}
	
}
