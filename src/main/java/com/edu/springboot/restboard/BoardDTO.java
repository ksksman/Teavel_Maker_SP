package com.edu.springboot.restboard;

import java.time.LocalDate;

import lombok.Data;

// 모델1 방식의 회원제 게시판에서 생성한 board테이블 사용
@Data
public class BoardDTO {
	private String board_idx;
	private int board_cate;
	private String nickname;
	private String title;
	private String content;
	private LocalDate post_date;
	private String view_count;
	private String like_count;
	private byte[] attached_file;
}
