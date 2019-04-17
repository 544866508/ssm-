package controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import common.Const;
import common.ResponseCode;
import common.ServerResponse;
import pojo.User;
import service.IUserService;
import service.iCategoryService;

@Controller
@RequestMapping("/manage/category")
public class CateGoryManageController {
	
	@Autowired
	private IUserService iUserService;
	@Autowired
	private iCategoryService iCategoryService;
	
	
	
	/**
	 * 添加品类
	 * @param session
	 * @param categoryName
	 * @param parentId
	 * @return
	 */
	@RequestMapping("add_category.do")
	@ResponseBody
	//@RequestParam(value = "parentId",defaultValue = "0")前端如果没有传参，那么parentId初始默认值为0
	public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		//校验是否是管理员
		if(iUserService.checkAdminRole(user).isSuccess()){
			//是管理员
			//增加处理分类的逻辑
			return iCategoryService.addCategory(categoryName, parentId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}
	
	
	/**
	 * 更新品类名称
	 * @param session
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	@RequestMapping("set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//是管理员
			//更新品类名
			return iCategoryService.updateCategoryName(categoryId, categoryName);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}		
	}
	
	/**
	 * 获取传入的品类id下的平级的子品类
	 * @param session
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("get_category.do")
	@ResponseBody
	public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId) {
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if(iUserService.checkAdminRole(user).isSuccess()){
			//是管理员
			//查询子节点的ctegory信息，不递归，保持平级
			return iCategoryService.getChildrenParallelCategory(categoryId);
		}else{
			return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
		}
	}
	
	
	/**
	 * 获取传入的品类id下的所有子品类，递归查询
	 * @param session
	 * @param categoryId
	 * @return
	 */
		@RequestMapping("get_deep_category.do")
		@ResponseBody
		public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId) {
			User user = (User)session.getAttribute(Const.CURRENT_USER);
			if(user == null){
				return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
			}
			if(iUserService.checkAdminRole(user).isSuccess()){
				//是管理员
				//查询当前节点的id和递归子节点的id
				return iCategoryService.selectCategoryAndChildrenById(categoryId);
			}else{
				return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
			}
		}
	
		
}
