package bigdecimaltest;
import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalTest {
	
	//使用方法总结
	//1.java中的计算方法只适合工程计算或科学计算，如果要进行商业计算，则需要采用bigdecimal数据类型
	//2.bigdecimal使用时一定要采用String类型的构造方法，也就是传参一定要传入字符串而不是数字，具体测试如下：

	@Test
	public void test1(){
		System.out.println(0.05+0.01);//结果将大于0.06
	}
	
	@Test
	public void test2(){
		BigDecimal b1 = new BigDecimal(0.05);
		BigDecimal b2 = new BigDecimal(0.01);
		System.out.println(b1.add(b2));//结果将大于0.06,并且小数位数更长
	}
	
	@Test
	public void test3(){
		BigDecimal b1 = new BigDecimal("0.05");
		BigDecimal b2 = new BigDecimal("0.01");
		System.out.println(b1.add(b2));//此时结果将等于0.06
	}
	
	
	
}
