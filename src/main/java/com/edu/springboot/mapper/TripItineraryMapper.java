package com.edu.springboot.mapper;

import com.edu.springboot.dto.TripItineraryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
@Mapper
public interface TripItineraryMapper {
    // 특정 여행의 일정 조회
    List<TripItineraryDTO> getItineraryByTripId(@Param("tripId") Long tripId);

    // 새로운 일정 추가
    void addItinerary(TripItineraryDTO itinerary);

    // 일정 삭제
    void deleteItinerary(@Param("itineraryId") Long itineraryId);

    // 특정 날짜의 일정 삭제
    void deleteItineraryByDate(@Param("tripId") Long tripId, @Param("itineraryDate") String itineraryDate);

 // ✅ 단일 일정 순서 변경
    void updateItineraryOrder(@Param("itineraryId") Long itineraryId, @Param("seq") Integer seq);

    // ✅ 여러 일정 순서 일괄 변경
    void updateItineraryOrderBatch(@Param("itineraryList") List<TripItineraryDTO> itineraryList);


    // 특정 날짜에서 SEQ 최대값 가져오기
    Integer getNextSeqForDate(@Param("tripId") Long tripId, @Param("itineraryDate") LocalDate itineraryDate);

    // 중복 체크
    int checkDuplicateItinerary(
        @Param("tripId") Long tripId, 
        @Param("itineraryDate") LocalDate itineraryDate, 
        @Param("placeName") String placeName,
        @Param("lat") Double lat,  
        @Param("lng") Double lng
    );
}
