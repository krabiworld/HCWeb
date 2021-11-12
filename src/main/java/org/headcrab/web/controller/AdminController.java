package org.headcrab.web.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.headcrab.web.util.DB.conn;

@Data
class ChangeUserRoleData {
	private String username;
	private String role;
}

@Controller
public class AdminController {
	@GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("changeUserRoleData", new ChangeUserRoleData());
		return "admin";
	}

	@PostMapping("/admin/changeuserrole")
	public String changeUserRolePageLogic(@ModelAttribute ChangeUserRoleData data, Model model) throws Exception {
		Statement statement = conn().createStatement();

		ResultSet check = statement.executeQuery(String.format("select role from users where username = '%s'", data.getUsername()));

		if (check.next()) {
			if (check.getString("role").equals("ADMIN")) {
				model.addAttribute("msg", "Error: You cannot give out a role higher or equal to your role.");
				return "admin";
			}
		} else {
			model.addAttribute("msg", "Error: Username not found.");
			return "admin";
		}

		statement.executeUpdate(String.format(
			"update users set role = '%s' where username = '%s'",
			data.getRole(), data.getUsername()
		));

		statement.close();

		model.addAttribute("msg", "Done! New Role: " + data.getRole());
		return "admin";
	}
}
