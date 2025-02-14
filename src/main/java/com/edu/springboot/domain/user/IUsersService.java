package com.edu.springboot.domain.user;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IUsersService {
	// 이메일로 사용자 조회
	@Select("SELECT * FROM Users WHERE email = #{email}")
	User findByEmail(String email);

	// 모든 사용자 조회 (테스트용)
	@Select("SELECT * FROM Users")
	public ArrayList<User> findAllUsers();

	// 새로운 사용자 등록
	@Insert("INSERT INTO Users (email, password, nickname, age, gender, phone_number, marketing_consent, recommended_friend, updated_at) "
			+ "VALUES (#{email}, #{password}, #{nickname}, #{age}, #{gender}, #{phoneNumber}, #{marketingConsent}, #{recommendedFriend}, SYSDATE)")
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	void insertUser(User user);

	// 사용자 정보 업데이트
	@Update("UPDATE Users SET nickname = #{nickname}, age = #{age}, gender = #{gender}, phone_number = #{phoneNumber}, "
			+ "marketing_consent = #{marketingConsent}, recommended_friend = #{recommendedFriend}, updated_at = SYSDATE "
			+ "WHERE user_id = #{userId}")
	void updateUser(User user);

	// 사용자 삭제
	@Delete("DELETE FROM Users WHERE user_id = #{userId}")
	void deleteUser(Long userId);
}
