package com.tish.controller;

import com.tish.entity.User;
import com.tish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/user"})
public class UserController {

	private final UserService userService;

	public UserController(@Autowired UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String openLoginPage() {
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

	@GetMapping("/register")
	public String openRegisterPage() {
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
}
