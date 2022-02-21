package com.mcit.cvbuilder;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mcit.cvbuilder.models.MyUserDetails;
import com.mcit.cvbuilder.models.User;
import com.mcit.resumebuilder.dto.UserRegistrationDto;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public MyUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

//	@Override
	public User save(UserRegistrationDto registrationDTO) {
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		SimpleGrantedAuthority role1 = new SimpleGrantedAuthority("ROLE_USER");
		authorities.add(role1);
		
		String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

		User user = new User(registrationDTO.getUserName(),encodedPassword,registrationDTO.isEnabled(),registrationDTO.getRoles(),registrationDTO.getFirstName(),registrationDTO.getLastName(),registrationDTO.getEmail());

		return userRepository.save(user);

	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(userName);

		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

		return user.map(MyUserDetails::new).get();
	}
}