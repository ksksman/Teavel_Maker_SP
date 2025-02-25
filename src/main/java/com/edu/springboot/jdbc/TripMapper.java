package com.edu.springboot.jdbc;

import com.edu.springboot.dto.ItineraryDto;
import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.dto.TripResponseDto;
import com.edu.springboot.dto.TripReviewDto;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TripMapper {

    @SelectKey(statement = "SELECT TRIP_SEQ.NEXTVAL FROM DUAL", keyProperty = "tripId", before = true, resultType = int.class)
    @Insert("INSERT INTO TRIP (TRIP_ID, USER_ID, TRIP_TITLE, START_DATE, END_DATE, CREATED_DATE) " +
            "VALUES (#{tripId}, #{userId}, #{title}, TO_DATE(#{startDate}, 'YYYY-MM-DD'), " +
            "TO_DATE(#{endDate}, 'YYYY-MM-DD'), CURRENT_TIMESTAMP)")
    void insertTrip(TripRequestDto tripRequest);

    // 수정된 getAllTrips: 내가 생성한 여행 OR 내가 참여한 여행 모두 조회
    @Select("SELECT DISTINCT t.TRIP_ID as tripId, t.USER_ID as userId, t.TRIP_TITLE as tripTitle, " +
            "TO_CHAR(t.START_DATE, 'YYYY-MM-DD') as startDate, TO_CHAR(t.END_DATE, 'YYYY-MM-DD') as endDate, " +
            "NVL(r.STATUS, '계획중') as status, r.IMAGE as image " +
            "FROM TRIP t " +
            "LEFT JOIN TRIP_REVIEW r ON t.TRIP_ID = r.TRIP_ID " +
            "WHERE t.USER_ID = #{userId} " +
            "   OR t.TRIP_ID IN (SELECT tp.TRIP_ID FROM TRIP_PARTICIPANTS tp WHERE tp.USER_ID = #{userId}) " +
            "ORDER BY t.TRIP_ID DESC")
    List<TripResponseDto> getAllTrips(@Param("userId") int userId);

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

    // 여행 생성 시 기본 리뷰 행 삽입
    @Insert("INSERT INTO TRIP_REVIEW (TRIP_ID, REVIEW, RATING, LAST_UPDATED, STATUS, IMAGE) " +
            "VALUES (#{tripId}, NULL, NULL, CURRENT_TIMESTAMP, '계획중', NULL)")
    int insertDefaultTripReview(@Param("tripId") int tripId);

    // 기본 일정 행 삽입 (PLACE_NAME은 '미정' 처리)
    @Insert("INSERT INTO TRIP_ITINERARY (ITINERARY_ID, TRIP_ID, ITINERARY_DATE, SEQ, CREATED_DATE, CONTENT_ID, PLACE_NAME, LAT, LNG) " +
            "VALUES (TRIP_ITINERARY_SEQ.NEXTVAL, #{tripId}, TO_DATE(#{startDate}, 'YYYY-MM-DD'), 1, CURRENT_TIMESTAMP, NULL, '미정', NULL, NULL)")
    int insertDefaultItinerary(@Param("tripId") int tripId, @Param("startDate") String startDate);

    @Update("UPDATE TRIP_REVIEW SET IMAGE = #{image} WHERE TRIP_ID = #{tripId}")
    int updateTripReviewImage(@Param("tripId") int tripId, @Param("image") String image);

    @Delete("DELETE FROM TRIP WHERE TRIP_ID = #{tripId}")
    int deleteTrip(@Param("tripId") int tripId);

    // 일정(관광지) 목록 조회 (별도 DTO ItineraryDto 사용)
    @Select("SELECT ITINERARY_ID, TRIP_ID, TO_CHAR(ITINERARY_DATE, 'YYYY-MM-DD') as itineraryDate, SEQ, PLACE_NAME as placeName " +
            "FROM TRIP_ITINERARY WHERE TRIP_ID = #{tripId} ORDER BY ITINERARY_DATE, SEQ")
    List<ItineraryDto> getItinerariesByTripId(@Param("tripId") int tripId);

}
