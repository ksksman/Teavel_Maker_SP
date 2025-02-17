package com.edu.springboot.restboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BoardRestController {
	
	// JDBC 작업을 위해 자동 주입
	@Autowired
	IBoardService dao;
	
	@GetMapping("/restBoardList.do")
	public List<BoardDTO> restBoardList(ParameterDTO parameterDTO) {
		// 한 페이지에 출력할 게시물의 수 (하드 코딩)
		int pageSize = 10;
		// 페이지 번호
		int pageNum = parameterDTO.getPageNum()==null ? 1 : 
			Integer.parseInt(parameterDTO.getPageNum());
		// 게시물의 구간 계산
		int start = (pageNum-1) * pageSize + 1;
		int end = pageNum * pageSize;
		// DTO에 계산 결과 저장
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		
		// 게시판 카테고리 설정
		parameterDTO.setBoard_cate(parameterDTO.getBoard_cate());
		
		// DAO의 메서드 호출
		List<BoardDTO> boardList = dao.list(parameterDTO);
		
		// List를 반환하므로 JSON 배열로 화면에 출력된다.
		return boardList;
	}
	
	@GetMapping("/restBoardSearch.do")
	public List<BoardDTO> restBoardSearch(HttpServletRequest req,
			ParameterDTO parameterDTO){
		// searchField 는 parameterDTO 가 받음
		// searchWord 는 별도로 저장해야 함.
		if(req.getParameter("searchWord")!=null) {
			String[] sTxtArray = req.getParameter("searchWord")
					.split(" ");
			parameterDTO.getSearchWord().clear();
			for(String str : sTxtArray) {
				System.out.println(str);
				parameterDTO.getSearchWord().add(str);
			}
		}
		// 게시판 카테고리 설정
		parameterDTO.setBoard_cate(parameterDTO.getBoard_cate());
		
		List<BoardDTO> searchList = dao.search(parameterDTO);
		return searchList;
	}
	
	@GetMapping("/restBoardView.do")
	public BoardDTO restBoardView(ParameterDTO parameterDTO) {
		BoardDTO boardDTO = dao.view(parameterDTO);
		return boardDTO;
	}
	
	@PostMapping("/restBoardWrite.do")
	public Map<String, Integer> restBoardWrite(BoardDTO boardDTO) {
		int result = dao.write(boardDTO);
		Map<String, Integer> map = new HashMap<>();
		map.put("result", result);
		return map;
		
	}

	@PatchMapping("/increaseLikeCount.do")
    public Map<String, Integer> increaseLikeCount(BoardDTO boardDTO) {
        int result = dao.increaseLikeCount(boardDTO);
        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);
        return map;
    }

    @PatchMapping("/increaseViewCount.do")
    public Map<String, Integer> increaseViewCount(BoardDTO boardDTO) {
        int result = dao.increaseViewCount(boardDTO);
        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);
        return map;
    }

    @PutMapping("/updateBoard.do")
    public Map<String, Integer> updateBoard(@RequestBody BoardDTO boardDTO) {
        int result = dao.updateBoard(boardDTO);
        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);
        return map;
    }

    @DeleteMapping("/deleteBoard.do")
    public Map<String, Integer> deleteBoard(@RequestParam("board_idx") String board_idx) {
        int result = dao.deleteBoard(board_idx);
        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);
        return map;
    }
    
    @GetMapping("/popularReviews.do")
    public List<BoardDTO> getPopularReviews(ParameterDTO parameterDTO) {
    	// 한 페이지에 출력할 게시물의 수 (하드 코딩)
		int pageSize = 10;
		// 페이지 번호
		int pageNum = parameterDTO.getPageNum()==null ? 1 : 
			Integer.parseInt(parameterDTO.getPageNum());
		// 게시물의 구간 계산
		int start = (pageNum-1) * pageSize + 1;
		int end = pageNum * pageSize;
        parameterDTO.setStart(start);
        parameterDTO.setEnd(end);

        return dao.getPopularReviews(parameterDTO);
    }
}



















