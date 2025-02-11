package com.edu.springboot.restboard;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IBoardService {
	// 게시물 갯수
	public int totalCount();
	// 게시물 가져오기
	public ArrayList<BoardDTO> list(ParameterDTO parameterDTO);
	// 게시물 검색하기
	public ArrayList<BoardDTO> search(ParameterDTO parameterDTO);
	// 게시물 내용보기
	public BoardDTO view(ParameterDTO parameterDTO);
	// 게시물 작성하기
	public int write(BoardDTO boardDTO);
}
