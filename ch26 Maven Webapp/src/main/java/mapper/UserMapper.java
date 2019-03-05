package mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("UserMapper")
public interface UserMapper {
 
	String findUserById(int id);
 
}
