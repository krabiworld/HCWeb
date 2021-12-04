package org.headcrab.web.controller;

import org.headcrab.web.model.User;
import org.headcrab.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AccountController {

	private final UserService userService;

	@Autowired
	public AccountController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/account")
	public String accountPage(Model model) {
		model.addAttribute("user", new User());
		return "account";
	}

	@PostMapping("/account/changeusername")
	public String changeUsernamePageLogic(@ModelAttribute User user, Model model,
										  RedirectAttributes attributes, Principal principal) {
		Optional<User> optionalUser = userService.findByUsername(user.getUsername());

		if (optionalUser.isEmpty()) {
			String username = principal.getName();

			Optional<User> optionalUserObject = userService.findByUsername(username);

			if (optionalUserObject.isEmpty()) {
				model.addAttribute("msg", "Error: You are not authorized.");
				return "account";
			}

			User userObject = optionalUserObject.get();
			userObject.setUsername(user.getUsername());
			userService.save(userObject);

			SecurityContextHolder.clearContext();

			attributes.addAttribute("msg", "Done! New username: " + user.getUsername());
			return "redirect:/login";
		} else {
			model.addAttribute("msg", "Error: This username already exists in the database.");
			return "account";
		}
	}

}
