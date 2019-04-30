package controller.portal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import common.ServerResponse;
import service.iProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private iProductService iProductService;
	
	@RequestMapping("/detail.do")
	@ResponseBody
	public ServerResponse detail(Integer productId) {
		return iProductService.getProductDetail(productId);
	}
	
	@RequestMapping("/list.do")
	@ResponseBody
	public ServerResponse list(@RequestParam(value = "keyword",required = false)String keyword,
							   @RequestParam(value = "categoryId",required = false)Integer categoryId,
							   @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
							   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
							   @RequestParam(value = "orderBy",defaultValue = "")String orderBy){
		return iProductService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
	}
	

	
	
}
