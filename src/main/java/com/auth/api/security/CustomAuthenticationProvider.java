package com.auth.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// TODO Auto-generated method stub
		
		System.out.println("Authprovider : Authenticate user with credentials");
		
		String userName = auth.getName();
		String password = auth.getCredentials().toString();
		//fsdfsd
		List<GrantedAuthority> authoroties = new ArrayList<>();
		authoroties.add(new SimpleGrantedAuthority("ADMIN"));		
		return new UsernamePasswordAuthenticationToken(userName, null,authoroties);
		
	}

	@Override
	public boolean supports(Class<?> auth) {
		// TODO Auto-generated method stub
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
