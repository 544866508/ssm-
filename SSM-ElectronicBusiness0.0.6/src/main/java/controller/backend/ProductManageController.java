package controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import pojo.Product;
import pojo.User;
import service.IFileService;
import service.IUserService;
import service.iProductService;
import util.PropertiesUtil;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
	
	@Autowired
	private IUserService iUserService;
	@Autowired
	private iProductService iProductService;
	@Autowired
	private IFileService iFileService;
	
	
	//添加或更新产品，传入产品id即为更新，不传入id就是添加
	@RequestMapping("save.do")
	@ResponseBody
	public ServerResponse productSave(HttpSession session,Product product) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			return iProductService.saveOrUpdateProduct(product);
		}else{
			return ServerResponse.createByErrorMessage("非管理员，无权限操作");
		}
	}
	
	//修改产品销售状态
	@RequestMapping("set_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			return iProductService.setSaleStatus(productId, status);
		}else{
			return ServerResponse.createByErrorMessage("非管理员，无权限操作");
		}
	}
	
	
	//获取产品详细信息vo
	@RequestMapping("get_detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			return iProductService.manageProductDetail(productId);
		}else{
			return ServerResponse.createByErrorMessage("非管理员，无权限操作");
		}
	}
	
	//列出所有产品的基本信息
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse getList(HttpSession session,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pagesize",defaultValue = "10") int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			return iProductService.getProductList(pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("非管理员，无权限操作");
		}
	}
	
	
	//通过name或id进行产品搜索
	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse productSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pagesize",defaultValue = "10") int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			return iProductService.searchProduct(productName, productId, pageNum, pageSize);
		}else{
			return ServerResponse.createByErrorMessage("非管理员，无权限操作");
		}
	}
	
	//文件上传
	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file") MultipartFile file,HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			//获取webapp下upload文件夹的路径
			String path = request.getSession().getServletContext().getRealPath("upload");
			//获取上传好的文件的名字
			//通过service接口将文件先传入web服务器下的upload暂存文件夹中，然后再上传到ftp服务器中，并删除upload中的暂存文件
			String targetFileName = iFileService.upload(file, path);
			
			//通过mmall.properties文件下键为"ftp.server.http.prefix"的值，得到ftp服务器的url
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			//声明一个fileMap,将上传的文件名和ftp服务器url存入其中
			Map fileMap = Maps.newHashMap();
			fileMap.put("uri",targetFileName);
			fileMap.put("url", url);
			//将fileMap返回到前端
			return ServerResponse.createBySuccess(fileMap);
		}else{
			return ServerResponse.createByErrorMessage("非管理员，无权限操作");
		}

	}
	
	
	//富文本上传
	//此接口只支持simditor
	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map richtextImgUpload(HttpSession session,@RequestParam(value = "upload_file") MultipartFile file,HttpServletRequest request,HttpServletResponse response) {
		Map resultMap = Maps.newHashMap();
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			resultMap.put("success", false);
			resultMap.put("msg", "请登录管理员");
			return resultMap;
		}
		//富文本中对于返回值有自己的要求，我们使用的是simditor，所以按照simditor的要求进行返回
//		{
//			"success": true/false,
//			"msg": "error message", # optional
//			"file_path": "[real file path]"
//		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//填充我们增加产品的业务逻辑
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			if(StringUtils.isBlank(targetFileName)){
				resultMap.put("success", false);
				resultMap.put("msg", "上传失败");
				return resultMap;
			}
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
			resultMap.put("success", true);
			resultMap.put("msg", "上传成功");
			resultMap.put("file_path", url);
			//修改response的Header
			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
			return resultMap;
		}else{
			resultMap.put("success", false);
			resultMap.put("msg", "无权限操作");
			return resultMap;
		}

	}
	
	
	
	
}
