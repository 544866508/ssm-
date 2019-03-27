package mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import entity.User;

@Mapper
@Repository("UserMapper")
public interface UserMapper {
 
	User findUserById(int id);
 
}
