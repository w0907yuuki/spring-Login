package com.example.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignupMessage {

	SUCCEED(MessageConst.SIGNUP_RESIST_SUCCEED, false),

	EXISTED_LOGIN_ID(MessageConst.SIGNUP_EXISTED_LOGIN_ID, true),

	NO_MATCH_PASSWORD(MessageConst.NO_MATCH_PASSWORD, true);
	
	private String messageId;

	private boolean isError;
}