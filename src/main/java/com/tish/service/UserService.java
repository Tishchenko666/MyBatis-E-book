package com.tish.service;

import com.tish.dao.UserDao;
import com.tish.entity.User;
import com.tish.entity.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserDao userDao;

	public UserService(@Autowired UserDao userDao) {
		this.userDao = userDao;
	}

	public User checkIfUserExists(String email) {
		return userDao.checkIfUserExists(email);
	}

	public void loginByUser(User user) {
		userDao.loginByUser(user);
	}

	public void logoutByEmail(String email) {
		userDao.logoutByEmail(email);
	}

	public User checkIfLoggedUserExists() {
		return userDao.checkIfLoggedUserExists();
	}

	public void registerUser(User user) {
		userDao.registerUser(user);
	}

	public List<UserResult> readUserResultsByUserId(Integer id) {
		return userDao.readUserResultsById(id);
	}

	public void updateUserData(User user) {
		userDao.updateUserData(user);
	}


}
