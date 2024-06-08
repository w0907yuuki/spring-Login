package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

//DBにアクセスする
//Userクラスで設定したIDとパスワード(Sttring型)を取得
@Repository
public interface UserRepository extends JpaRepository<User, String>{

	
}
