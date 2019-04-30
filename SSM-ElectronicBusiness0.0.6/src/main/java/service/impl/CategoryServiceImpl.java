package service.impl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import common.ServerResponse;
import dao.CategoryMapper;
import pojo.Category;
import service.iCategoryService;

@Service("iCategoryService")
public class CategoryServiceImpl implements iCategoryService{
	
	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryMapper categoryMapper; 

	//添加品类
	@Override
	public ServerResponse addCategory(String categoryName,Integer parentId) {
		if(parentId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("添加品类参数错误，或未添加");
		}
		
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);
		
		int rowCount = categoryMapper.insert(category);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMessage("添加品类成功");
		}
		return ServerResponse.createByErrorMessage("添加品类失败");
	}
	
	
	//更新品类名称
	@Override
	public ServerResponse updateCategoryName(Integer categoryId,String categoryName) {
		if(categoryId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMessage("更新品类参数错误，或未更新");
		}
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);
		
		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMessage("更新品类名字成功");
		}
		return ServerResponse.createByErrorMessage("更新品类名字失败");
	}
	
	
	//获取传入的品类id下的平级的子品类  
	@Override
	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		//判断是否为null，是否为空字符串
		if(CollectionUtils.isEmpty(categoryList)){
			logger.info("未找到当前分类的子分类");
		}
		return ServerResponse.createBySuccess(categoryList);
	}
	
	/**
	 * 获取传入的品类id下的所有子品类  
	 * @param categoryId
	 * @return
	 */
	@Override
	public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
		Set<Category> categorySet = Sets.newHashSet();
		findChildCategory(categorySet, categoryId);
		
		
		List<Integer> categoryList = Lists.newArrayList();
		if(categoryId != null){
			for(Category categoryItem : categorySet){
				categoryList.add(categoryItem.getId());
			}
		}
		return ServerResponse.createBySuccess(categoryList);
		
	}
	
	//递归算法，算出子节点
	private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
		//查询当前id的品类信息
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if(category != null){
			categorySet.add(category);
		}
		//查找以当前品类id为parentId的品类（就是查找当前品类的子品类）,存入list中
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		//然后对list中的所有品类进行for循环
		for(Category categoryItem : categoryList){
			//递归查询list中的每个category的信息
			findChildCategory(categorySet, categoryItem.getId());
		}
		//当没有一个品类的parentId为当前品类的id的时候，list里面就是空的，for循环就会停止，然后就会执行return
		return categorySet;
		
	}
	
	
	
}
