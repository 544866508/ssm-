package util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	private static Properties props;

	
	//项目启动时加载mmall.properties
	//静态代码块
	//执行顺序，静态代码块static{} > 普通代码块{} > 构造代码块public ClassName{}
	static{
		String fileName = "mmall.properties";
		props = new Properties();

		try {
			//通过当前类的加载器，获取src下filename文件的输入流，然后交给java.util.Properties类加载
			props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
		} catch (IOException e) {
			logger.error("配置文件读取异常",e);
		}

	}
	
	public static String getProperty(String key){
		String value = props.getProperty(key.trim());
		if(StringUtils.isBlank(value)){
			return null;
		}
		return value.trim();
	}
	
	public static String getProperty(String key,String defaultValue){
		String value = props.getProperty(key.trim());
		if(StringUtils.isBlank(value)){
			value = defaultValue;
		}
		return value.trim();
	}
	
	
}


