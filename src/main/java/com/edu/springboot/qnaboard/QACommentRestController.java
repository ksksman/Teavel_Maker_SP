package com.edu.springboot.qnaboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QACommentRestController {
	 @Autowired
    private IQACommentService commentService;

    // 댓글 리스트 조회
    @GetMapping("/getComments.do")
    public List<QACommentDTO> getComments(@RequestParam("qa_id") int qa_id) {
        return commentService.getComments(qa_id);
    }

    // 댓글 추가
    @PostMapping("/addComment.do")
    public Map<String, Object> addComment(@RequestBody QACommentDTO comment) {
        int result = commentService.addComment(comment);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result > 0);
        return response;
    }

    // 댓글 수정
    @PutMapping("/updateComment.do")
    public Map<String, Object> updateComment(@RequestBody QACommentDTO comment) {
        int result = commentService.updateComment(comment);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result > 0);
        return response;
    }

    // 댓글 삭제
    @DeleteMapping("/deleteComment.do")
    public Map<String, Object> deleteComment(@RequestParam("comment_idx") int comment_idx) {
        int result = commentService.deleteComment(comment_idx);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result > 0);
        return response;
    }
}
