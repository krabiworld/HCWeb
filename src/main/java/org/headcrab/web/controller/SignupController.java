package org.headcrab.web.controller;

import lombok.Data;
import org.headcrab.web.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.headcrab.web.util.DB.conn;

@Data
class SignupData {
    private String username;
    private String password;
	private String retypePassword;
    private String email;
}

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupData", new SignupData());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPageLogic(@ModelAttribute SignupData data, Model model) throws Exception {
		if (!data.getPassword().equals(data.getRetypePassword())) {
			model.addAttribute("msg", "Error: Password mismatch.");
			return "signup";
		}

		String newPassword = Utils.hashPassword(data.getPassword());
		data.setPassword(newPassword);

		Statement statement = conn().createStatement();

        ResultSet check = statement.executeQuery(String.format(
			"SELECT COUNT(*) FROM users WHERE username = '%s' or email = '%s'",
			data.getUsername(), data.getEmail()
		));

        if (check.next()) {
            if (check.getInt(1) > 0) {
                model.addAttribute("msg", "Error: This username or email is already registered.");
                return "signup";
            }
        }

        statement.executeUpdate(String.format(
                "insert into users (username, password, email) values ('%s', '%s', '%s')",
                data.getUsername(), data.getPassword(), data.getEmail()
        ));

		ResultSet idQuery = statement.executeQuery(String.format(
			"select id from users where email = '%s'", data.getEmail()
		));

		int id = 0;
		String token = Utils.generateUUID();

		if (idQuery.next()) id = idQuery.getInt("id");

		statement.executeUpdate(String.format(
			"insert into confirmation_tokens (id, token, type) values (%d, '%s', 'signup')",
			id, token
		));

		statement.close();

		Utils.sendEmail("Sign Up: http://localhost:8080/signup/done?" + id + "&token=" + token);

        model.addAttribute("done", "Link send to email: " + data.getEmail());
        return "done";
    }

	@GetMapping("/signup/done")
	public String signupDonePage(@RequestParam int id, @RequestParam String token, Model model) throws Exception {
		Statement statement = conn().createStatement();

		ResultSet check = statement.executeQuery(String.format(
			"select * from confirmation_tokens where id = %d and token = '%s' and type = 'signup'",
			id, token
		));

		if (!check.next()) {
			model.addAttribute("error", "Token invalid.");
			return "error";
		}

		statement.executeUpdate("delete from confirmation_tokens where id = " + id);
		statement.executeUpdate("update users set enabled = true where id = " + id);

		statement.close();

		model.addAttribute("done", "Your account activated! <a href=\"/login\">Login</a>");
		return "done";
	}

}
