package com.edu.springboot.restboard;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.springboot.jdbc.TripMapper;

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
		
		return boardList;
	}
	
	@GetMapping("/boardTotalLength.do")
	public Map<String, Integer> boardTotalLength(ParameterDTO parameterDTO) {
		// 전체 게시글 개수 가져오기
	    int totalLength = dao.boardTotalLength(parameterDTO);

	    // 결과를 JSON 형태로 반환
	    Map<String, Integer> map = new HashMap<>();
	    map.put("totalCount", totalLength);
	    
	    return map;
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
	
	@Autowired
	TripMapper tripMapper;
	
	@PostMapping("/restBoardWrite.do")
	public Map<String, Integer> restBoardWrite(@RequestBody Map<String, Object> requestData) {
	    
	    BoardDTO boardDTO = new BoardDTO();
	    boardDTO.setTitle((String) requestData.get("title"));
	    boardDTO.setContent((String) requestData.get("content"));
	    boardDTO.setNickname((String) requestData.get("nickname"));
	    boardDTO.setBoard_cate((Integer) requestData.get("board_cate"));

	    // ✅ tripId 가져오기 (Qna 게시판은 tripId 없음)
	    Integer tripId = (Integer) requestData.get("tripId");

	    if (tripId != null) {
	        // ✅ 후기 게시판에서 TRIP_REVIEW의 IMAGE 가져오기
	        String image = tripMapper.getTripById(tripId).getImage();
	        boardDTO.setAttached_file(image != null ? image : "");  // null 방지 (빈 문자열로 저장)
	    } else {
	        boardDTO.setAttached_file("");  // Qna 게시판에서는 빈 문자열 저장
	    }

	    int result = dao.write(boardDTO);

	    Map<String, Integer> response = new HashMap<>();
	    response.put("result", result);
	    return response;
	}


//	@PatchMapping("/increaseLikeCount.do")
//    public Map<String, Integer> increaseLikeCount(BoardDTO boardDTO) {
//        int result = dao.increaseLikeCount(boardDTO);
//        Map<String, Integer> map = new HashMap<>();
//        map.put("result", result);
//        return map;
//    }

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
    
    // ALHomePage.jsx 에서 인기 Top6 후기글 요청하는 API
    @GetMapping("/topLikedReviews.do")
    public List<BoardDTO> getTopLikedReviews() {
        return dao.getTopLikedReviews();
    }
    
    // ✅ 특정 사용자가 게시물에 좋아요를 눌렀는지 확인
    @GetMapping("/checkLike")
    public boolean checkLike(@RequestParam("userId") int userId, @RequestParam("boardIdx") int boardIdx) {
        return dao.checkIfUserLiked(userId, boardIdx) > 0;
    }

    // ✅ 좋아요 추가 (boards 테이블의 like_count 증가)
    @PostMapping("/addLike")
    public ResponseEntity<String> addLike(@RequestParam("userId") int userId, @RequestParam("boardIdx") int boardIdx) {
        dao.addLike(userId, boardIdx);
        dao.increaseLikeCount(boardIdx);  // like_count 증가
        return ResponseEntity.ok("좋아요 추가 완료");
    }

    // ✅ 좋아요 취소 (boards 테이블의 like_count 감소)
    @DeleteMapping("/removeLike")
    public ResponseEntity<String> removeLike(@RequestParam("userId") int userId, @RequestParam("boardIdx") int boardIdx) {
        dao.removeLike(userId, boardIdx);
        dao.decreaseLikeCount(boardIdx);  // like_count 감소
        return ResponseEntity.ok("좋아요 취소 완료");
    }

    // ✅ 사용자가 좋아요한 여행 목록 가져오기
    @GetMapping("/likedReviews")
    public List<BoardDTO> getLikedReviews(@RequestParam("userId") int userId) {
        return dao.getLikedReviews(userId);
    }
    
    @GetMapping("/likedPosts.do")
    public List<BoardDTO> getLikedPosts(@RequestParam("userId") int userId) {
        return dao.getLikedPosts(userId);
    }

}



















