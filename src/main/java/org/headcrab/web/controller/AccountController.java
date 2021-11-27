package org.headcrab.web.controller;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.headcrab.web.util.DB.conn;

@Data
class ChangeUsernameData {
	private String username;
}

@Controller
public class AccountController {

	@GetMapping("/account")
	public String accountPage(Model model) {
		model.addAttribute("changeUsernameData", new ChangeUsernameData());
		return "account";
	}

	@PostMapping("/account/changeusername")
	public String changeUsernamePageLogic(@ModelAttribute ChangeUsernameData data,
										  Model model,
										  RedirectAttributes attributes,
										  Authentication authentication) throws Exception {
		Statement statement = conn().createStatement();

		ResultSet check = statement.executeQuery(String.format(
			"select count(username) from users where username = '%s'", data.getUsername()
		));

		if (check.next()) {
			if (check.getInt(1) > 0) {
				model.addAttribute("msg", "Error: This username already exists in the database.");
				return "account";
			}
		}

		String username = authentication.getName();

		statement.executeUpdate(String.format(
			"update users set username = '%s' where username = '%s'",
			data.getUsername(), username
		));

		statement.close();

		SecurityContextHolder.getContext().setAuthentication(null);

		attributes.addAttribute("msg", "Done! New username: " + data.getUsername());
		return "redirect:/login";
	}

}
