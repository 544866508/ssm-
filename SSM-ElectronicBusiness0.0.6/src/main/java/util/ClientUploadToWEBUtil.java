package util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

//此util的作用是：上传源文件到WEB服务器
public class ClientUploadToWEBUtil {
	
	//打印日志
	private static Logger logger = LoggerFactory.getLogger(ClientUploadToWEBUtil.class);

	//上传源文件到WEB服务器中自定义的暂存目录
	public static File uploadFile(MultipartFile file,String path) throws IOException {
		//获得上传文件的原始文件名
		String fileName = file.getOriginalFilename();
		//获取原始文件扩展名
		//例如：abc.jpg --> jpg
		String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
		//将上传后的文件名定义为UUID字符串+原始文件扩展名
		String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
		//输出上传文件的日志
		logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
		//声明一个文件对象，路径为上传路径
		File fileDir = new File(path);
		//如果该文件不存在
		if(!fileDir.exists()){
			//给文件对象赋予写权限
			fileDir.setWritable(true);
			//创建文件夹
			fileDir.mkdirs();
		}
		
		File targetFile = new File(path,uploadFileName);
		//上传file文件到WEB服务器的targetFile
		file.transferTo(targetFile);
		//上传到tomcat完毕
		
		//返回上传好的targetFile
		return targetFile;

}
}
