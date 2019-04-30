package service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import service.IFileService;
import util.ClientUploadToWEBUtil;
import util.WEBUploadToFTPUtil;


@Service("iFileService")
public class FileServiceImpl implements IFileService {
	
	private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	public String upload(MultipartFile file,String path) {
		
		
		File targetFile = null;
		
		try {
			//上传源文件到WEB服务器
			targetFile = ClientUploadToWEBUtil.uploadFile(file,path);
			
			//将上传在WEB服务器上的文件，再上传到FTP服务器上
			WEBUploadToFTPUtil.uploadFile(Lists.newArrayList(targetFile));//上传到ftp服务器完毕
			
			//上传完成之后，删除WEB服务器上的文件
			targetFile.delete();
			
		} catch (IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		
		return targetFile.getName();
		
	}
}
