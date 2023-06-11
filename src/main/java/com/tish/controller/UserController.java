package com.tish.controller;

import com.tish.entity.User;
import com.tish.entity.UserResult;
import com.tish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = {"/user"})
public class UserController {

	private final UserService userService;

	public UserController(@Autowired UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String openLoginPage(Model model) {
		model.addAttribute("user", new User());
		return "login-page";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("user") User user, BindingResult result, Model model) {

		User existingUser = userService.checkIfUserExists(user.getLogin());
		if (existingUser == null) {
			result.rejectValue("login", null,
					"This account is not exists or your login is wrong");
		} else if (!existingUser.getPassword().equals(user.getPassword())) {
			result.rejectValue("password", null,
					"Wrong password");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "login-page";
		}

		userService.loginByUser(existingUser);

		return "redirect:/material/index";
	}

	@GetMapping("/logout")
	public String logout(@RequestParam String login) {

		userService.logoutByEmail(login);

		return "redirect:/material/index";
	}

	@GetMapping("/register")
	public String openRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register-page";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user, BindingResult result, Model model) {

		User existingUser = userService.checkIfUserExists(user.getLogin());
		if (existingUser != null && existingUser.getLogin() != null && !existingUser.getLogin().isEmpty()) {
			result.rejectValue("login", null,
					"There is already an account registered with the same email");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "register-page";
		}

		userService.registerUser(user);

		return "redirect:/user/login";
	}

	@GetMapping("/account")
	public String openUserPage(Model model, @RequestParam String login) {
		User user = userService.checkIfUserExists(login);
		model.addAttribute("user", user);

		model.addAttribute("resultList", userService.readUserResultsByUserId(user.getId()));

		return "user-page";
	}

	@PostMapping("/account/update")
	public String saveUserDataChanges(@ModelAttribute("user") User user, Model model) {
		userService.updateUserData(user);

		return "redirect:/user/account?login=" + user.getLogin();
	}


}
