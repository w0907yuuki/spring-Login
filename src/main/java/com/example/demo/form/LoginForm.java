package com.example.demo.form;

import lombok.Data;

/**
 * ログイン画面 form
 * 
 * @author ys-fj
 *
 */
@Data
public class LoginForm {

	/** ログインID */
	private String userid;

	/** パスワード */
	private String password;
}