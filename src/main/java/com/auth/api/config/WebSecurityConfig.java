package com.auth.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth.api.security.AuthorizeFilter;
import com.auth.api.security.CustomAuthenticationFilter;
import com.auth.api.security.CustomAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	ObjectMapper objectMapper;
	
	/*@Bean
	@Order(1)
    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        return new CustomAuthenticationFilter("/account/login", authenticationManager(), objectMapper);
    }*/
	
	/*
	 * @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	 * 
	 * @Override public AuthenticationManager authenticationManagerBean() throws
	 * Exception { return super.authenticationManagerBean(); }
	 */
	
	@SuppressWarnings("deprecation")
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()		
		
		.authorizeRequests().antMatchers(HttpMethod.POST, "/account/login").permitAll()
				.anyRequest().authenticated().and().httpBasic().and()
				.addFilterAfter(new CustomAuthenticationFilter("/account/login", authenticationManager(), objectMapper),BasicAuthenticationFilter.class);
				
		
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(customAuthenticationProvider);
	}

}
