package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.db.AuthorityKind;
import com.example.demo.constant.db.UserStatusKind;
import com.example.demo.entity.User;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserRepository;
import com.github.dozermapper.core.Mapper;

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

	/** ユーザー情報テーブルRepositoryクラス */
	private final UserRepository repository;

	/** Dozer Mapper */
	private final Mapper mapper;

	/** パスワードエンコーダー */
	private final PasswordEncoder passwordEncoder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<User> resistUserInfo(SignupForm form) {
		var userInfoExistedOpt = repository.findById(form.getUserid());
		if (userInfoExistedOpt.isPresent()) {
			return Optional.empty();
		}

		var userInfo = mapper.map(form, User.class);
		var encodedPassword = passwordEncoder.encode(form.getPassword());
		userInfo.setPassword(encodedPassword);
		userInfo.setUserStatusKind(UserStatusKind.ENABLED);
		userInfo.setAuthorityKind(AuthorityKind.ITEM_WATCHER);
		userInfo.setCreateTime(LocalDateTime.now());
		userInfo.setUpdateTime(LocalDateTime.now());
		userInfo.setUpdateUser(form.getUserid());

		return Optional.of(repository.save(userInfo));
	}
}