package com.example.demo.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignupForm {
	
	@NotNull
	private String firstname;
	@NotNull
	private String lastname;
	@NotNull
	@NotEmpty
	private String birthyear;
	@NotNull
	@NotEmpty
	private String birthmon;
	@NotNull
	@NotEmpty
	private String birthday;
	
	private String gender;
	@NotNull
	private String mailaddress;
	
	/** ログインID */
	@NotNull
	private String userid;

	/** パスワード */
	@NotNull
	private String password;

	
	private String checkpassword;
}
