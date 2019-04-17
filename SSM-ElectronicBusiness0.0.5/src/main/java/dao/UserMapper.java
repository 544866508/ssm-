package dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import common.ServerResponse;
import pojo.User;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    
    
    //mybatis传递多个参数时，需要用到param注解，注解中的参数对应mappers中paramType的值
    User selectLogin(@Param("username") String username,@Param("password") String password);
    
    int checkUsername(String username);
    
    int checkEmail(String email);
    
    String selectQuestionByUsername(String username);
    
	public int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username,@Param("passwordNew") String passwordNew);
    
    int checkPassword(@Param("password") String password,@Param("Integer") Integer userId); 
   
    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
    
    
}