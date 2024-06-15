package com.auth.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.auth.api.domain.UserLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private ObjectMapper objectMapper;
	
	private AuthenticationManager authManager;
	
	final long EXPIRATIONTIME = 864_000_000; // 10 days
	final String SECRET = "ThisIsASecret";
	final String TOKEN_PREFIX = "Bearer";
	final String HEADER_STRING = "Authorization";
	
	public CustomAuthenticationFilter(String loginUrl,
			AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
		super(loginUrl);
		
		this.authManager = authenticationManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		UserLogin userLogin = objectMapper.readValue(request.getReader(), UserLogin.class);

		System.out.println(userLogin);
		
		UsernamePasswordAuthenticationToken uTokenDTO = new UsernamePasswordAuthenticationToken(userLogin.getUserName(),
				userLogin.getPassword());

		System.out.println("Call registered auth provider from auth filter using auth manager");
		return authManager.authenticate(uTokenDTO);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		/*claim("authorities", auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))*/
		
		String JWT = Jwts.builder().setSubject("sunil")				
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		response.setContentType("text/html");
		Cookie cookie = new Cookie("accessToken", JWT);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setDomain("xyz.com");
      //  response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addCookie(cookie);
        //response.addHeader("Set-Cookie",cookie);
		
		
		//response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
		System.out.println("Add token to the response header and call chain.doFilter");
		

	}

}
