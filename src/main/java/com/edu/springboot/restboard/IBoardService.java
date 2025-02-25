package com.edu.springboot.restboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface IBoardService {
	// 게시물 갯수
	public int totalCount();
	// 게시물 가져오기
	public ArrayList<BoardDTO> list(ParameterDTO parameterDTO);
	// 게시판의 총 게시물 갯수
	public int boardTotalLength(ParameterDTO parameterDTO);
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
//    public int increaseLikeCount(BoardDTO boardDTO);
    // 조회수 증가
    public int increaseViewCount(BoardDTO boardDTO);
    // 전체글 인기글 보기
    public ArrayList<BoardDTO> getPopularReviews(ParameterDTO parameterDTO);
    // 가장 좋아요(like_count)가 많은 6개 게시물 조회
    public ArrayList<BoardDTO> getTopLikedReviews();
    
    // 특정 사용자가 특정 게시물에 좋아요를 눌렀는지 확인
    @Select("SELECT COUNT(*) FROM user_likes WHERE user_id = #{userId} AND board_idx = #{boardIdx}")
    int checkIfUserLiked(@Param("userId") int userId, @Param("boardIdx") int boardIdx);

    // 좋아요 추가
    @Insert("INSERT INTO user_likes (like_id, user_id, board_idx) VALUES (user_likes_seq.NEXTVAL, #{userId}, #{boardIdx})")
    int addLike(@Param("userId") int userId, @Param("boardIdx") int boardIdx);

    // 좋아요 제거 (사용자가 좋아요 취소)
    @Delete("DELETE FROM user_likes WHERE user_id = #{userId} AND board_idx = #{boardIdx}")
    int removeLike(@Param("userId") int userId, @Param("boardIdx") int boardIdx);

    // 특정 사용자가 좋아요한 여행 목록 가져오기
    @Select("SELECT b.* FROM boards b JOIN user_likes ul ON b.board_idx = ul.board_idx WHERE ul.user_id = #{userId}")
    List<BoardDTO> getLikedReviews(@Param("userId") int userId);
    
    // 좋아요 수 증가
    @Update("UPDATE boards SET like_count = like_count + 1 WHERE board_idx = #{boardIdx}")
    int increaseLikeCount(@Param("boardIdx") int boardIdx);

    // 좋아요 수 감소
    @Update("UPDATE boards SET like_count = like_count - 1 WHERE board_idx = #{boardIdx} AND like_count > 0")
    int decreaseLikeCount(@Param("boardIdx") int boardIdx);
    
    // 사용자가 좋아요한 게시글 목록 가져오기
    public List<BoardDTO> getLikedPosts(@Param("userId") int userId);

}
