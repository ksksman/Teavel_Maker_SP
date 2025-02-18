package com.edu.springboot.qnaboard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IQACommentService {
    List<QACommentDTO> getComments(@Param("qa_id") int qa_id);
    public int addComment(QACommentDTO comment);
    int updateComment(QACommentDTO comment);
    int deleteComment(@Param("comment_idx") int comment_idx);

}
