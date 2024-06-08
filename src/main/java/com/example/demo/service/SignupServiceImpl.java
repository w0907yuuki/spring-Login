package com.example.demo.service;

import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.AuthorityKind;
import com.example.demo.entity.User;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	private final UserRepository repository;
	
	private final Mapper mapper;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public Optional<User> resistUser(SignupForm signupForm){
				
		var userExsitOpt  = repository.findById(signupForm.getUserid());
		if(userExsitOpt.isPresent()) {
			return Optional.empty();
		}
		
		var user = mapper.map(signupForm, User.class);

		var encoderPassword = passwordEncoder.encode(signupForm.getPassword());
		user.setPassword(encoderPassword);
		user.setAuthority(AuthorityKind.ITEM_WATCHER.getAuthorityKind());
		
		/*var birthyear = signupform.getBirthyear();
		var birthmon = signupform.getBirthmon();
		var birthday = signupform.getBirthday();
		var databirthday = birthyear + birthmon + birthday;
		user.setFirstname(signupform.getFirstname());
		user.setLastname(signupform.getLastname());
		user.setBirthday(databirthday);
		user.setGender(signupform.getGender());
		user.setMailaddress(signupform.getMailaddress());
		user.setUserid(signupform.getUserid());
		user.setPassword(signupform.getPassword());*/

		
		return Optional.of(repository.save(user));
	}

}

