package org.headcrab.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final DataSource dataSource;

	public WebSecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Autowired
	public void config(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.passwordEncoder(new BCryptPasswordEncoder())
			.dataSource(dataSource)
			.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE email = ?")
			.authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.authorizeRequests()
				.antMatchers("/", "/error", "/done", "/recovery", "/passwordrecovery").permitAll()
				.antMatchers("/signup").anonymous()
				.antMatchers("/welcome", "/account").authenticated()
				.antMatchers("/admin").hasAuthority("ADMIN")
				.antMatchers("/mod").hasAnyAuthority("ADMIN", "MOD")
				.and()
			.formLogin()
				.loginPage("/login").permitAll()
				.and()
			.logout().permitAll();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
			.antMatchers("/css/**", "/js/**");
	}
}
