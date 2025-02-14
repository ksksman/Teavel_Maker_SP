package com.edu.springboot.qnaboard;

import java.time.LocalDate;

import lombok.Data;

@Data
public class QACommentDTO {
	private int comment_idx;
    private int qa_id;
    private String nickname;
    private String content;
    private LocalDate comment_date;
}
