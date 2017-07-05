package com.peu.springboot.service;


import java.util.List;

import com.peu.springboot.model.User;

public interface UserService {
	
	User findById(long id);
	
	User findByName(String name);
	
	void add(User user);
	
	void updateUser(User user);
	
	User login(String name, String password);
	
	void logoff(long id);
	
	void deleteUserById(long id);

	List<User> findAllUsers();
		
	boolean isUserExist(User user);
	
}
