package com.edu.springboot.jdbc;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface IMemberService {
	
	public List<MemberDTO> select();
	public int insert(MemberDTO memberDTO);
	public MemberDTO selectOne(MemberDTO memberDTO);
	public int update(MemberDTO memberDTO);
	public int delete(MemberDTO memberDTO);

	// ✅ 로그인 메서드 추가 (이게 없으면 오류 발생)
	public boolean login(MemberDTO memberDTO);
	MemberDTO selectByNickname(String nickname);
}
