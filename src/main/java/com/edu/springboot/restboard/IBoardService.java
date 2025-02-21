package com.edu.springboot.restboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IBoardService {
	// 게시물 갯수
	public int totalCount();
	// 게시물 가져오기
	public ArrayList<BoardDTO> list(ParameterDTO parameterDTO);
	// 게시판의 총 게시물 갯수
	public int boardTotalLength(BoardDTO boardDTO);
	// 게시물 검색하기
	public ArrayList<BoardDTO> search(ParameterDTO parameterDTO);
	// 게시물 내용보기
	public BoardDTO view(ParameterDTO parameterDTO);
	// 게시물 작성하기
	public int write(BoardDTO boardDTO);
    // 게시물 수정하기
    public int updateBoard(BoardDTO boardDTO);
    // 게시물 삭제하기
    public int deleteBoard(@Param("board_idx") String board_idx);
    // 좋아요 증가
    public int increaseLikeCount(BoardDTO boardDTO);
    // 조회수 증가
    public int increaseViewCount(BoardDTO boardDTO);
    // 전체글 인기글 보기
    public ArrayList<BoardDTO> getPopularReviews(ParameterDTO parameterDTO);
    // 가장 좋아요(like_count)가 많은 3개 게시물 조회
    public ArrayList<BoardDTO> getTopLikedReviews();

}
