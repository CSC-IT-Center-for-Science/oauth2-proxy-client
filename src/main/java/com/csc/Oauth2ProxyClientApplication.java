package com.csc;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.context.SecurityContext;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class Oauth2ProxyClientApplication extends WebSecurityConfigurerAdapter{

	HashMap<String, String> uniqueToToken = new HashMap<String, String>();
	
	@RequestMapping("/register")
	public String register(OAuth2Authentication auth, @RequestParam("unique") String unique) {
		String accessToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
		
		this.uniqueToToken.put(unique, accessToken);
		return "Token: " + ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
	}

	@RequestMapping("/unique/{unique}")
	public String root(@PathVariable("unique") String unique) {
		return this.uniqueToToken.get(unique);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    	.antMatcher("/**")
			.authorizeRequests()
			  .antMatchers("/", "/login**", "/unique/**")
			  .permitAll()
			.anyRequest()
			  .authenticated();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Oauth2ProxyClientApplication.class, args);
	}
}
