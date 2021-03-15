/*package com.fasset.security;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.User;
import com.fasset.exceptions.LoginAndPasswordCredentialsException;
import com.fasset.exceptions.LoginCredentialsException;
import com.fasset.utils.MD5Encrypter;
import com.fasset.utils.StringUtil;

@Service("CustomAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private IUserDAO userDAO;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional(noRollbackFor = { AuthenticationException.class }, readOnly = false)
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		if ((StringUtil.isEmptyOrNullValue(username)) || (StringUtil.isEmptyOrNullValue(password))) {
			LoginAndPasswordCredentialsException excep = new LoginAndPasswordCredentialsException("");

			if (StringUtil.isEmptyOrNullValue(username)) {
				excep.setNoLogin("");
			}

			if (StringUtil.isEmptyOrNullValue(password)) {
				excep.setNoPasswd("");
			}
			throw excep;
		}
		password = MD5Encrypter.encrypt(password);

		User user = null;
		try {
			user = userDAO.loadUserByUsername(username);

			if (user == null) {
				throw new LoginCredentialsException("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), true, true, true,true,
				getGrantedAuthorities(user));

		return new UsernamePasswordAuthenticationToken(user1, password, null);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();		
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRole_name()));
		return authorities;
	}
}*/