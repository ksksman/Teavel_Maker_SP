package com.edu.springboot.mapper;

import com.edu.springboot.dto.TripItineraryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TripItineraryMapper {
    // 특정 여행의 일정 조회 (위도, 경도 포함)
    List<TripItineraryDTO> getItineraryByTripId(@Param("tripId") Long tripId);

    // 새로운 일정 추가 (위도, 경도 포함)
    void addItinerary(TripItineraryDTO itinerary);

    // 특정 날짜의 일정 삭제 (여행 ID & 날짜 기준)
    void deleteItineraryByDate(@Param("tripId") Long tripId, @Param("itineraryDate") String itineraryDate);

    // 일정 삭제
    void deleteItinerary(@Param("itineraryId") Long itineraryId);

    // ✅ 특정 날짜의 다음 SEQ 조회 (현재 날짜에 등록된 일정 개수 + 1)
    Integer getNextSeqForDate(@Param("tripId") Long tripId, @Param("itineraryDate") LocalDate itineraryDate);

    // 일정 순서 변경
    void updateItineraryOrder(@Param("itineraryId") Long itineraryId, @Param("seq") Integer seq);

    // 중복 체크 (위도, 경도 포함 & NULL 비교 지원)
    int checkDuplicateItinerary(
        @Param("tripId") Long tripId, 
        @Param("itineraryDate") LocalDate itineraryDate, 
        @Param("placeName") String placeName,
        @Param("lat") Double lat,  
        @Param("lng") Double lng  
    );
}
