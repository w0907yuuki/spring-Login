package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.constant.UrlConst;
import com.example.demo.constant.ViewNameConst;
import com.example.demo.constant.db.AuthorityKind;

@Controller
public class TopmenuController {

	@GetMapping(UrlConst.MENU)
	public String view(@AuthenticationPrincipal User user, Model model) {
		var hasUserManageAuth = user.getAuthorities().stream()
				.allMatch(authority -> authority.getAuthority()
						.equals(AuthorityKind.ITEM_AND_USER_MANAGER.getCode()));
		model.addAttribute("hasUserManageAuth", hasUserManageAuth);

		return ViewNameConst.MENU;
	}
}
