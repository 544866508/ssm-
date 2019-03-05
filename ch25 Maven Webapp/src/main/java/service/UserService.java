package service;
 
import javax.annotation.Resource;
 
import org.springframework.stereotype.Service;
 

import mapper.UserMapper;
 
@Service("UserService")
public class UserService {
 
	@Resource(name="UserMapper")
	private UserMapper userMapper;
 
	public String findUserById(int id) {
		return userMapper.findUserById(id);
 
	}
 
}
