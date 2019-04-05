package service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import service.IFileService;
import util.FTPUtil;


@Service("iFileService")
public class FileServiceImpl implements IFileService {
	
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	public String upload(MultipartFile file,String path) {
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
		
		
		try {
			//上传文件
			file.transferTo(targetFile);//上传到tomcat完毕
			
			//将targetFile上传到FTP服务器
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));//上传到ftp服务器完毕
			
			//上传完成之后，删除upload下的文件
			targetFile.delete();
			
		} catch (IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		return targetFile.getName();
		
	}
}
