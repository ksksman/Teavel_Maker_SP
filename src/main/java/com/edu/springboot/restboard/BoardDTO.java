package com.edu.springboot.restboard;

import java.time.LocalDate;

import lombok.Data;

// boards 테이블DTO
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

	private String attached_file;
	private Integer tripId;
}
