package com.example.demo.controller;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.SignupMessage;
import com.example.demo.constant.UrlConst;
import com.example.demo.entity.User;
import com.example.demo.form.SignupForm;
import com.example.demo.service.SignupServiceImpl;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {
	
	private final SignupServiceImpl service;
	
	private final MessageSource messageSource;
	
	//private final PasswordEncoder passwordEncoder;
	
	
	@GetMapping("signup")
	public String view(Model model, SignupForm signupForm) {
		
		return UrlConst.SIGNUP;
	}
	
	@PostMapping("signup")
	public void signup(Model model, @Validated SignupForm signupForm, BindingResult bdResult) {
		if (bdResult.hasErrors()) {
			editGuideMessage(model, MessageConst.FORM_ERROR, true);
			return;
		}

		var userOpt = service.resistUser(signupForm);
		var signupMessage = judgeMessageKey(userOpt, signupForm);
		editGuideMessage(model, signupMessage.getMessageId(), signupMessage.isError());
	}

	/**
	 * 画面に表示するガイドメッセージを設定する
	 * 
	 * @param model モデル
	 * @param messageId メッセージID
	 * @param isError エラー有無
	 */
	private void editGuideMessage(Model model, String messageId, boolean isError) {
		var message = AppUtil.getMessage(messageSource, messageId);
		model.addAttribute("message", message);
		model.addAttribute("isError", isError);
	}

	private SignupMessage judgeMessageKey(Optional<User> userOpt, SignupForm signupForm ) {
		if(signupForm.getPassword().equals(signupForm.getCheckpassword())) {
			if (userOpt.isEmpty()) {
				return SignupMessage.EXISTED_LOGIN_ID;
			} else {
				return SignupMessage.SUCCEED;
			}
		}
		else {
			return SignupMessage.NO_MATCH_PASSWORD;
		}
		}
	}