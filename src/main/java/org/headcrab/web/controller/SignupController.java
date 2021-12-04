package org.headcrab.web.controller;

import org.headcrab.web.model.Token;
import org.headcrab.web.model.User;
import org.headcrab.web.service.TokenService;
import org.headcrab.web.service.UserService;
import org.headcrab.web.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class SignupController {

	private final UserService userService;
	private final TokenService tokenService;

	@Autowired
	public SignupController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPageLogic(@ModelAttribute User user, Model model) {
		if (!user.getPassword().equals(user.getRetypePassword())) {
			model.addAttribute("msg", "Error: Password mismatch.");
			return "signup";
		}

		user.setPassword(Utils.hashPassword(user.getPassword()));

		Optional<User> optionalUsername = this.userService.findByUsername(user.getUsername());

		if (optionalUsername.isPresent()) {
			model.addAttribute("msg", "Error: This username is already registered.");
			return "signup";
		}

		Optional<User> optionalEmail = this.userService.findByEmail(user.getEmail());

		if (optionalEmail.isPresent()) {
			model.addAttribute("msg", "Error: This email is already registered.");
			return "signup";
		}

		userService.save(user);

		String tokenUUID = Utils.generateUUID();

		Token token = new Token();
		token.setUser(user);
		token.setToken(tokenUUID);
		token.setType("signup");

		tokenService.save(token);

		Utils.sendEmail("Sign Up: http://localhost:8080/signup/done?id=" + token.getId() + "&token=" + tokenUUID);

        model.addAttribute("done", "Link send to email: " + user.getEmail());
        return "done";
    }

	@GetMapping("/signup/done")
	public String signupDonePage(@RequestParam int id, @RequestParam String token, Model model) {
		Optional<Token> optionalToken = tokenService.findByIdAndTokenAndType(id, token, "signup");

		if (optionalToken.isPresent()) {
			Token tokenObject = optionalToken.get();

			User user = tokenObject.getUser();
			user.setEnabled(true);

			userService.save(user);
			tokenService.delete(tokenObject);

			model.addAttribute("toLogin", "Your account activated!");
			return "done";
		} else {
			model.addAttribute("error", "Token invalid.");
			return "error";
		}
	}

}
