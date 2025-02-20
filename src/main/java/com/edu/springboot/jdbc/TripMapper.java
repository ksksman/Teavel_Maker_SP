package com.edu.springboot.jdbc;

import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.dto.TripResponseDto;
import com.edu.springboot.dto.TripReviewDto;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface TripMapper {

    // 여행 생성: TRIP 테이블에 INSERT
    @SelectKey(statement = "SELECT TRIP_SEQ.NEXTVAL FROM DUAL", keyProperty = "tripId", before = true, resultType = int.class)
    @Insert("INSERT INTO TRIP (TRIP_ID, USER_ID, TRIP_TITLE, START_DATE, END_DATE, CREATED_DATE) " +
            "VALUES (#{tripId}, #{userId}, #{title}, TO_DATE(#{startDate}, 'YYYY-MM-DD'), " +
            "TO_DATE(#{endDate}, 'YYYY-MM-DD'), CURRENT_TIMESTAMP)")
    void insertTrip(TripRequestDto tripRequest);

    // 여행 목록 조회: TRIP 테이블과 TRIP_REVIEW의 IMAGE, STATUS 등 조회
    @Select("SELECT t.TRIP_ID as tripId, t.USER_ID as userId, t.TRIP_TITLE as tripTitle, " +
            "TO_CHAR(t.START_DATE, 'YYYY-MM-DD') as startDate, TO_CHAR(t.END_DATE, 'YYYY-MM-DD') as endDate, " +
            "NVL(r.STATUS, '계획중') as status, r.IMAGE as image " +
            "FROM TRIP t LEFT JOIN TRIP_REVIEW r ON t.TRIP_ID = r.TRIP_ID " +
            "ORDER BY t.TRIP_ID DESC")
    List<TripResponseDto> getAllTrips();

    // 여행 상세 조회: 특정 TRIP_ID의 데이터 조회
    @Select("SELECT t.TRIP_ID as tripId, t.USER_ID as userId, t.TRIP_TITLE as tripTitle, " +
            "TO_CHAR(t.START_DATE, 'YYYY-MM-DD') as startDate, TO_CHAR(t.END_DATE, 'YYYY-MM-DD') as endDate, " +
            "NVL(r.REVIEW, '') as review, NVL(r.RATING, 0) as rating, NVL(r.STATUS, '계획중') as status, r.IMAGE as image " +
            "FROM TRIP t LEFT JOIN TRIP_REVIEW r ON t.TRIP_ID = r.TRIP_ID " +
            "WHERE t.TRIP_ID = #{tripId}")
    TripResponseDto getTripById(int tripId);

    // TRIP_REVIEW 업데이트: 리뷰, 평점, 상태(여행완료) 업데이트
    @Update("UPDATE TRIP_REVIEW SET REVIEW = #{review}, RATING = #{rating}, STATUS = '여행완료', LAST_UPDATED = CURRENT_TIMESTAMP " +
            "WHERE TRIP_ID = #{tripId}")
    int updateTripReview(@Param("tripId") int tripId,
                         @Param("review") String review,
                         @Param("rating") double rating);

    // TRIP_REVIEW INSERT: 리뷰 데이터가 없을 경우 INSERT (STATUS '여행완료')
    @Insert("INSERT INTO TRIP_REVIEW (TRIP_ID, REVIEW, RATING, LAST_UPDATED, STATUS, IMAGE) " +
            "VALUES (#{tripId}, #{review}, #{rating}, CURRENT_TIMESTAMP, '여행완료', NULL)")
    int insertTripReview(@Param("tripId") int tripId,
                         @Param("review") String review,
                         @Param("rating") double rating);

    // 기본 리뷰 행 삽입: 여행 생성 시 TRIP_REVIEW에 기본 행 삽입 (리뷰, 평점은 NULL, 상태 '계획중', IMAGE NULL)
    @Insert("INSERT INTO TRIP_REVIEW (TRIP_ID, REVIEW, RATING, LAST_UPDATED, STATUS, IMAGE) " +
            "VALUES (#{tripId}, NULL, NULL, CURRENT_TIMESTAMP, '계획중', NULL)")
    int insertDefaultTripReview(@Param("tripId") int tripId);

    // 이미지 업데이트: TRIP_REVIEW 테이블의 IMAGE 컬럼 업데이트
    @Update("UPDATE TRIP_REVIEW SET IMAGE = #{image} WHERE TRIP_ID = #{tripId}")
    int updateTripReviewImage(@Param("tripId") int tripId, @Param("image") String image);
    
    @Delete("DELETE FROM TRIP WHERE TRIP_ID = #{tripId}")
    int deleteTrip(@Param("tripId") int tripId);

}
