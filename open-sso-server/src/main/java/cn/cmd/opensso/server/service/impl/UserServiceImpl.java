package cn.cmd.opensso.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.cmd.opensso.server.model.User;
import cn.cmd.opensso.server.service.UserService;
import org.springframework.stereotype.Service;

import cn.cmd.opensso.client.rpc.Result;
import cn.cmd.opensso.client.rpc.SsoUser;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static List<User> userList;
	
	static {
		userList = new ArrayList<>();
		userList.add(new User(1, "管理员", "admin", "123456"));
	}
	
	@Override
	public Result<SsoUser> login(String username, String password) {
		for (User user : userList) {
			if (user.getUsername().equals(username)) {
				if(user.getPassword().equals(password)) {
					return Result.createSuccess(new SsoUser(user.getId(), user.getUsername()));
				}
				else {
					return Result.createError("密码有误");
				}
			}
		}
		return Result.createError("用户不存在");
	}
}
