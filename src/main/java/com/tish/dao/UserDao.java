package com.tish.dao;

import com.tish.entity.User;
import com.tish.entity.UserResult;

import java.util.List;

public interface UserDao {

	User checkIfUserExists(String email);

	void loginByUser(User user);

	void logoutByEmail(String email);

	User checkIfLoggedUserExists();

	void registerUser(User user);

	List<UserResult> readUserResultsById(Integer id);

	void updateUserData(User user);

}
