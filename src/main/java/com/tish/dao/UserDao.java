package com.tish.dao;

import com.tish.entity.User;

public interface UserDao {

	User checkIfUserExists(String email);

	void loginByUser(User user);

	User checkIfLoggedUserExists();

	void registerUser(User user);

}
