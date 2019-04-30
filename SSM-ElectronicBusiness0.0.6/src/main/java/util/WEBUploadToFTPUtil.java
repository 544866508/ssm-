package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//此util的作用：上传WEB自定义暂存目录中的文件到FTP服务器
public class WEBUploadToFTPUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(WEBUploadToFTPUtil.class);
	
	//传参构造函数
	public  WEBUploadToFTPUtil(String ip,int port,String user,String pwd) {
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
	}
	
	
	//声明FTP服务器的地址、用户名、密码，并赋予PropertiesUtil中的基本信息
	private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
	private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
	private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");
	
	//连接FTP服务器并上传WEB服务器中暂存目录中的文件
	public static boolean uploadFile(List<File> fileList) throws IOException {
		WEBUploadToFTPUtil ftpUtil = new WEBUploadToFTPUtil(ftpIp, 21, ftpUser, ftpPass);
		logger.info("开始连接ftp服务器");
		boolean result = ftpUtil.uploadFile(PropertiesUtil.getProperty("ftp.server.remotePath"), fileList);
		logger.info("结束上传，上传结果：{}",result);
		
		return result;
	}
	
	//-----------------------------------------------------------------------------
	
	private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
		boolean uploaded = true;
		FileInputStream fis = null;
		//连接FTP服务器
		if(connectServer(this.getIp(), this.getPort(), this.getUser(), this.getPwd())){
			try {
				//切换上传路径到remotePath
				ftpClient.changeWorkingDirectory(remotePath);
				//缓冲区大小
				ftpClient.setBufferSize(1024);
				//编码格式
				ftpClient.setControlEncoding("utf-8");
				//文件类型设置成二进制文件类型
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				//被动模式
				ftpClient.enterLocalPassiveMode();
				//遍历所有文件
				for(File fileItem : fileList){
					fis = new FileInputStream(fileItem);
					//上传到FTP服务器
					ftpClient.storeFile(fileItem.getName(), fis);
					
				}
			} catch (IOException e) {
				logger.error("上传文件异常",e); 
				uploaded = false;
				e.printStackTrace();
			} finally {
				//关闭流
				fis.close();
				//关闭FTP连接
				ftpClient.disconnect();
			}
		}
		return uploaded;
	}
	
	//连接ftp服务器的函数
	private boolean connectServer(String ip,int port,String user,String pwd) {
		
		boolean isSuccess = false;
		
		ftpClient = new FTPClient();
		
		try {
			//输入URL连接FTP服务器
			ftpClient. connect(ip);
			//输入FTP虚拟宿主用户的账号密码
			isSuccess = ftpClient.login(user, pwd);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("连接FTP服务器异常",e);
			e.printStackTrace();
		}
		//返回布尔判断是否连接成功
		return isSuccess;
	}
	
	
	
	private String ip;
	private int port;
	private String user;
	private String pwd;
	private FTPClient ftpClient;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	
	
	
	
	
	
}
