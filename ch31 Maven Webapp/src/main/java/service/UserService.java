package service;
 
import javax.annotation.Resource;
 
import org.springframework.stereotype.Service;

import entity.User;
import mapper.UserMapper;
 
@Service("UserService")
public class UserService {
 
	@Resource(name="UserMapper")
	private UserMapper userMapper;
 
	public User findUserById(int id) {
		return userMapper.findUserById(id);
 
	}
 
}
