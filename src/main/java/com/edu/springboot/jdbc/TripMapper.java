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

    @SelectKey(statement = "SELECT TRIP_SEQ.NEXTVAL FROM DUAL", keyProperty = "tripId", before = true, resultType = int.class)
    @Insert("INSERT INTO TRIP (TRIP_ID, USER_ID, TRIP_TITLE, START_DATE, END_DATE, CREATED_DATE) " +
            "VALUES (#{tripId}, #{userId}, #{title}, TO_DATE(#{startDate}, 'YYYY-MM-DD'), " +
            "TO_DATE(#{endDate}, 'YYYY-MM-DD'), CURRENT_TIMESTAMP)")
    void insertTrip(TripRequestDto tripRequest);

    @Select("SELECT t.TRIP_ID as tripId, t.USER_ID as userId, t.TRIP_TITLE as tripTitle, " +
            "TO_CHAR(t.START_DATE, 'YYYY-MM-DD') as startDate, TO_CHAR(t.END_DATE, 'YYYY-MM-DD') as endDate, " +
            "NVL(r.STATUS, '계획중') as status, r.IMAGE as image " +
            "FROM TRIP t LEFT JOIN TRIP_REVIEW r ON t.TRIP_ID = r.TRIP_ID " +
            "ORDER BY t.TRIP_ID DESC")
    List<TripResponseDto> getAllTrips();

    @Select("SELECT t.TRIP_ID as tripId, t.USER_ID as userId, t.TRIP_TITLE as tripTitle, " +
            "TO_CHAR(t.START_DATE, 'YYYY-MM-DD') as startDate, TO_CHAR(t.END_DATE, 'YYYY-MM-DD') as endDate, " +
            "NVL(r.REVIEW, '') as review, NVL(r.RATING, 0) as rating, NVL(r.STATUS, '계획중') as status, r.IMAGE as image " +
            "FROM TRIP t LEFT JOIN TRIP_REVIEW r ON t.TRIP_ID = r.TRIP_ID " +
            "WHERE t.TRIP_ID = #{tripId}")
    TripResponseDto getTripById(int tripId);

    @Update("UPDATE TRIP_REVIEW SET REVIEW = #{review}, RATING = #{rating}, STATUS = '여행완료', LAST_UPDATED = CURRENT_TIMESTAMP " +
            "WHERE TRIP_ID = #{tripId}")
    int updateTripReview(@Param("tripId") int tripId,
                         @Param("review") String review,
                         @Param("rating") double rating);

    @Insert("INSERT INTO TRIP_REVIEW (TRIP_ID, REVIEW, RATING, LAST_UPDATED, STATUS, IMAGE) " +
            "VALUES (#{tripId}, #{review}, #{rating}, CURRENT_TIMESTAMP, '여행완료', NULL)")
    int insertTripReview(@Param("tripId") int tripId,
                         @Param("review") String review,
                         @Param("rating") double rating);

    // 여행 생성 시 기본 리뷰 행 삽입 (리뷰, 평점, 이미지는 NULL, 상태는 '계획중')
    @Insert("INSERT INTO TRIP_REVIEW (TRIP_ID, REVIEW, RATING, LAST_UPDATED, STATUS, IMAGE) " +
            "VALUES (#{tripId}, NULL, NULL, CURRENT_TIMESTAMP, '계획중', NULL)")
    int insertDefaultTripReview(@Param("tripId") int tripId);

    // TRIP_ITINERARY 테이블에 기본 행 삽입
    // ITINERARY_DATE는 여행 시작일, PLACE_NAME은 빈 문자열로 처리 (또는 '미정' 등으로 처리)
    @Insert("INSERT INTO TRIP_ITINERARY (ITINERARY_ID, TRIP_ID, ITINERARY_DATE, PLACE_NAME, SEQ, CREATED_DATE, CONTENT_ID) " +
            "VALUES (TRIP_ITINERARY_SEQ.NEXTVAL, #{tripId}, TO_DATE(#{startDate}, 'YYYY-MM-DD'), '미정', 1, CURRENT_TIMESTAMP, NULL)")
    int insertDefaultItinerary(@Param("tripId") int tripId, @Param("startDate") String startDate);

    @Update("UPDATE TRIP_REVIEW SET IMAGE = #{image} WHERE TRIP_ID = #{tripId}")
    int updateTripReviewImage(@Param("tripId") int tripId, @Param("image") String image);

    @Delete("DELETE FROM TRIP WHERE TRIP_ID = #{tripId}")
    int deleteTrip(@Param("tripId") int tripId);
}
