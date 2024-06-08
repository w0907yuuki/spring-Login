package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="user")
@Data
@AllArgsConstructor
public class User {

	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;
	
	@Column(name="birthyear")
	private String birthyear;
	
	@Column(name="birthmon")
	private String birthmon;
	
	@Column(name="birthday")
	private String birthday;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="mailaddress")
	private String mailaddress;
	
	@Id
	@Column(name="userid")
	private String userid;
	
	@Column(name="password")
	private String password;	
	
	/** ログイン失敗回数 */
	@Column(name = "login_failure_count")
	private int loginFailureCount = 0;

	/** アカウントロック日時 */
	@Column(name = "account_locked_time")
	private LocalDateTime accountLockedTime;

	/** 利用可能か(true:利用可能) */
	@Column(name = "is_disabled")
	private boolean isDisabled;

	private String authority;
	public User() {
	}

	/**
	 * ログイン失敗回数をインクリメントする
	 * 
	 * @return ログイン失敗回数がインクリメントされたUserInfo
	 */
	public User incrementLoginFailureCount() {
		return new User(firstname,lastname,birthyear,birthmon,birthday,gender,mailaddress,userid, password, ++loginFailureCount, accountLockedTime, isDisabled,authority);
	}

	/**
	 * ログイン失敗情報をリセットする
	 * 
	 * @return ログイン失敗情報がリセットされたUserInfo
	 */
	public User resetLoginFailureInfo() {
		return new User(firstname,lastname,birthyear,birthmon,birthday,gender,mailaddress,userid, password, 0, null, isDisabled,authority);
	}

	/**
	 * アカウントロック状態に更新する
	 * 
	 * @return ログイン失敗階位数、アカウントロック日時が更新されたUserInfo
	 */
	public User updateAccountLocked() {
		return new User(firstname,lastname,birthyear,birthmon,birthday,gender,mailaddress,userid, password, 0, LocalDateTime.now(), isDisabled,authority);
	}
	
}
