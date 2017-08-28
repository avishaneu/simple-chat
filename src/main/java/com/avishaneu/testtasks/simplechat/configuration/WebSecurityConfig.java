package com.avishaneu.testtasks.simplechat.configuration;

import com.avishaneu.testtasks.simplechat.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	AuthenticationService authService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("Configuring web security...");
		http
			.csrf()
				.disable()
			.formLogin()
				.loginPage("/join.html")
				.loginProcessingUrl("/join")
				.defaultSuccessUrl("/index.html")
				.permitAll()
				.and()
			.logout().logoutUrl("/leave").logoutSuccessUrl("/").and()
			.authorizeRequests()
				.antMatchers("/assets/**", "/webjars/**", "/join.html").permitAll()
				.anyRequest().authenticated();
	}

	@Autowired
	public void global(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(new AuthenticationProvider() {
			@Override
			public boolean supports(Class<?> authentication) {
				return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
			}
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
				String username = authService.authenticateUser(token.getName());
				return new UsernamePasswordAuthenticationToken(username, token.getCredentials(), null);
			}
		});
	}
}
