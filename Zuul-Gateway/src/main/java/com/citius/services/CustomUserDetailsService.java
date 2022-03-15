
package com.citius.services;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.citius.models.User;
import com.citius.models.UserGroup;
import com.citius.models.UserModel;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		ParameterizedTypeReference<HashSet<UserGroup>> setTypeRef = new ParameterizedTypeReference<HashSet<UserGroup>>() {
		};

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		UserModel user = restTemplate.getForObject("http://USER-SERVICE/auth/" + username, UserModel.class);

		if (!user.getUsername().isEmpty()) {
			ResponseEntity<HashSet<UserGroup>> userGroups = restTemplate
					.exchange("http://USER-SERVICE/roleMapping/" + username, HttpMethod.GET, entity, setTypeRef);

			return new User(user, userGroups.getBody());
		} else
			throw new UsernameNotFoundException("User Not Found! Please Register Yourself");

	}

	public UserModel registerUser(UserModel user) {

		if (user != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		@SuppressWarnings("unchecked")
		HttpEntity requestEntity = new HttpEntity(user, headers);

		UserModel user1 = restTemplate.postForObject("http://USER-SERVICE/user", requestEntity,
				UserModel.class);

		return user1;

	}

	public HttpStatus passwordReset(String userName) {

		String res = restTemplate.getForObject("http://USER-SERVICE/reset/" + userName, String.class);
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
