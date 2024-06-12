package com.example.demo.service;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.MessageConst;
import com.example.demo.constant.SignupResult;
import com.example.demo.constant.db.AuthorityKind;
import com.example.demo.constant.db.UserStatusKind;
import com.example.demo.dto.SignupInfo;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.common.MailSendService;
import com.example.demo.util.AppUtil;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー登録画面Service実装クラス
 * 
 * @author ys-fj
 *
 */
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	/** メール送信Serviceクラス */
	private final MailSendService mailSendService;

	/** ユーザー情報テーブルRepositoryクラス */
	private final UserRepository repository;

	/** パスワードエンコーダー */
	private final PasswordEncoder passwordEncoder;

	/** メッセージソース */
	private final MessageSource messageSource;

	/** ワンタイムコード有効時間 */
	@Value("${one-time-code.valid-time}")
	private Duration oneTimeCodeValidTime = Duration.ZERO;

	/** ワンタイムコードの長さ */
	@Value("${one-time-code.length}")
	private int oneTimeCodeLength = 0;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SignupResult signup(SignupInfo dto) {
		var userInfoOpt = repository.findById(dto.getUserid());
		if (userInfoOpt.isPresent()) {
			var userInfo = userInfoOpt.get();
			if (userInfo.isSignupCompleted()) {
				return SignupResult.FAILURE_BY_ALREADY_COMPLETED;
			}

			return SignupResult.FAILURE_BY_SIGNUP_PROCEEDING;
		}

		var oneTimeCode = generateOneTimeCode();
		var signupInfo = editSignupInfo(dto, oneTimeCode);
		try {
			repository.save(signupInfo);
		} catch (Exception e) {
			return SignupResult.FAILURE_BY_DB_ERROR;
		}

		var mailTextBase = AppUtil.getMessage(messageSource, MessageConst.SIGNUP_MAIL_TEXT);
		var mailText = MessageFormat.format(mailTextBase, oneTimeCode, oneTimeCodeValidTime.toMinutes());
		var mailSubject = AppUtil.getMessage(messageSource, MessageConst.SIGNUP_MAIL_SUBJECT);
		var canSendMail = mailSendService.sendMail(dto.getMailaddress(), mailSubject, mailText);
		if (!canSendMail) {
			var isDeleteFailure = deleteSignupInfo(dto.getUserid());
			if (!isDeleteFailure) {
				return SignupResult.FAILURE_BY_DB_ERROR;
			}
			return SignupResult.FAILURE_BY_MAIL_SEND_ERROR;
		}

		return SignupResult.SUCCEED;
	}

	/**
	 * ランダムな数字でワンタイムコードを生成します。
	 * 
	 * @return ワンタイムコード
	 */
	private String generateOneTimeCode() {
		var sb = new StringBuilder();
		for (int i = 0; i < oneTimeCodeLength; i++) {
			var randomNum = ThreadLocalRandom.current().nextInt(10);
			sb.append(randomNum);
		}

		return sb.toString();
	}

	/**
	 * ユーザー登録情報を作成する。
	 * 
	 * @param dto ユーザー登録画面Service入力情報
	 * @param oneTimeCode ワンタイムコード
	 * @return ユーザー登録情報
	 */
	private User editSignupInfo(SignupInfo dto, String oneTimeCode) {
		var userInfo = new User();
		userInfo.setUserid(dto.getUserid());
		userInfo.setPassword(passwordEncoder.encode(dto.getPassword()));
		userInfo.setMailaddress(dto.getMailaddress());
		userInfo.setBirthyear(dto.getBirthyear());
		userInfo.setBirthmon(dto.getBirthmon());	
		userInfo.setBirthday(dto.getBirthday());
		userInfo.setFirstname(dto.getFirstname());
		userInfo.setLastname(dto.getLastname());
		userInfo.setOneTimeCode(passwordEncoder.encode(oneTimeCode));
		userInfo.setOneTimeCodeSendTime(LocalDateTime.now());
		userInfo.setUserStatusKind(UserStatusKind.ENABLED);
		userInfo.setAuthorityKind(AuthorityKind.ITEM_WATCHER);
		userInfo.setSignupCompleted(false);
		userInfo.setCreateTime(LocalDateTime.now());
		userInfo.setUpdateTime(LocalDateTime.now());
		userInfo.setUpdateUser(dto.getUserid());

		return userInfo;
	}

	/**
	 * DBのユーザー登録情報から対象のログインIDに紐づくレコードを削除します。
	 * 
	 * @param loginId ログインID
	 * @return 結果ステータス(成功ならtrue)
	 */
	private boolean deleteSignupInfo(String userid) {
		try {
			repository.deleteById(userid);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}