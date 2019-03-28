package service;

import java.util.List;

import javax.servlet.http.HttpSession;

import common.ServerResponse;
import pojo.Category;

public interface iCategoryService {
	
	//添加品类
	ServerResponse addCategory(String categoryName,Integer parentId);
	
	//更新品类名称
	ServerResponse updateCategoryName(Integer categoryId,String categoryName);
	
	//获取传入的品类id下的平级的子品类  
	ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
	
	//获取传入的品类id下的所有子品类  
	ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
