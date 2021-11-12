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
class RecoveryData {
    private String email;
}
@Data
class RecoveryDoneData {
	private String password;
	private String retypePassword;
}

@Controller
public class RecoveryController {
    @GetMapping("/recovery")
    public String restorePage(Model model) {
        model.addAttribute("recoveryData", new RecoveryData());
        return "recovery";
    }

    @PostMapping("/recovery")
    public String restorePageLogic(@ModelAttribute RecoveryData data, Model model) throws Exception {
		Statement statement = conn().createStatement();

        ResultSet check = statement.executeQuery(
                String.format("select id, email from users where email = '%s'", data.getEmail())
        );

        int id;

        if (check.next()) {
            id = check.getInt("id");
        } else {
            model.addAttribute("msg", "Error: Email not found.");
            return "recovery";
        }

        String token = Utils.generateUUID();

        statement.executeUpdate(String.format(
            "insert into confirmation_tokens (id, token, type) values (%d, '%s', 'recovery')",
            id, token
        ));

		statement.close();

		Utils.sendEmail("Recovery: http://localhost:8080/recovery/done?id=" + id + "&token=" + token);

        model.addAttribute("done", "Link send to email: " + data.getEmail());
        return "done";
    }

	// Password recovery step two
	@GetMapping(value = "/recovery/done")
	public String recoverPassword(@RequestParam int id, @RequestParam String token, Model model) throws Exception {
		Statement statement = conn().createStatement();

		ResultSet check = statement.executeQuery(String.format(
			"select * from confirmation_tokens where id = %d and token = '%s' and type = 'recovery'",
			id, token
		));

		if (!check.next()) {
			model.addAttribute("error", "Token invalid");
			return "error";
		}

		statement.executeUpdate(String.format("delete from confirmation_tokens where id = %d", id));

		statement.close();

		model.addAttribute("id", id);
		model.addAttribute("recoverData", new RecoveryDoneData());
		return "passwordrecovery";
	}

	@PostMapping(value = "/recovery/done")
	public String recoverPasswordLogic(@RequestParam int id, @ModelAttribute RecoveryDoneData data, Model model) throws Exception {
		if (!data.getPassword().equals(data.getRetypePassword())) {
			model.addAttribute("id", id);
			model.addAttribute("msg", "Error: Password mismatch.");
			return "passwordrecovery";
		}

		Statement statement = conn().createStatement();

		String newPassword = Utils.hashPassword(data.getPassword());
		data.setPassword(newPassword);

		statement.executeUpdate(String.format(
			"update users set password = '%s' where id = %d",
			data.getPassword(), id
		));

		statement.close();

		model.addAttribute("done", "You password changed. <a href='/login'>Login</a>");
		return "done";
	}
}
