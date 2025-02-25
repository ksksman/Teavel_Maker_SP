package com.edu.springboot.service;

import com.edu.springboot.dto.ItineraryDto;
import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.dto.TripResponseDto;
import com.edu.springboot.dto.TripReviewDto;
import com.edu.springboot.jdbc.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TripService {

    @Autowired
    private TripMapper tripMapper;

    @Transactional
    public void createTrip(TripRequestDto tripRequest) {
        // 1. TRIP 테이블에 여행 정보 저장
        tripMapper.insertTrip(tripRequest);
        // 2. TRIP_REVIEW 테이블에 기본 리뷰 행 삽입 (후기는 NULL, 상태는 '계획중')
        tripMapper.insertDefaultTripReview(tripRequest.getTripId());
        // 3. TRIP_ITINERARY 테이블에 기본 일정 행 삽입 (ITINERARY_DATE는 startDate, PLACE_NAME은 '미정')
        tripMapper.insertDefaultItinerary(tripRequest.getTripId(), tripRequest.getStartDate());
    }

    public List<TripResponseDto> getAllTrips() {
        return tripMapper.getAllTrips();
    }

    // 날짜 범위 생성 함수
    private List<String> generateDateRange(String start, String end) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            while (!cal.getTime().after(endDate)) {
                result.add(sdf.format(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TripResponseDto getTripById(int tripId) {
        // 1) 여행 기본 정보 조회
        TripResponseDto trip = tripMapper.getTripById(tripId);
        if (trip == null) {
            return null;
        }

        // 2) 일정 목록 조회
        List<ItineraryDto> itineraryList = tripMapper.getItinerariesByTripId(tripId);

        // 3) 날짜별로 placeName을 그룹화
        Map<String, List<String>> itineraryMap = new HashMap<>();
        for (ItineraryDto it : itineraryList) {
            itineraryMap
                .computeIfAbsent(it.getItineraryDate(), k -> new ArrayList<>())
                .add(it.getPlaceName());
        }
        trip.setItinerary(itineraryMap);

        // 4) 여행 기간 전체 날짜 범위 생성
        List<String> fullRange = generateDateRange(trip.getStartDate(), trip.getEndDate());

        // 5) DB에 있는 일정 날짜 목록
        Set<String> usedDates = itineraryMap.keySet(); // 실제 일정이 있는 날짜들

        // 6) fullRange + usedDates 병합 후 정렬
        Set<String> mergedSet = new HashSet<>(fullRange);
        mergedSet.addAll(usedDates);
        List<String> mergedDates = new ArrayList<>(mergedSet);
        Collections.sort(mergedDates);

        // 7) TripResponseDto에 일정 날짜 목록 세팅
        trip.setItineraryDates(mergedDates);

        return trip;
    }

    @Transactional
    public void updateTripReview(int tripId, TripReviewDto reviewDto) {
        int rows = tripMapper.updateTripReview(tripId, reviewDto.getReview(), reviewDto.getRating());
        if (rows == 0) {
            rows = tripMapper.insertTripReview(tripId, reviewDto.getReview(), reviewDto.getRating());
            if (rows == 0) {
                throw new RuntimeException("후기 업데이트 실패: 리뷰 데이터 삽입 실패");
            }
        }
    }

    @Transactional
    public void updateTripReviewImage(int tripId, String imageUrl) {
        tripMapper.updateTripReviewImage(tripId, imageUrl);
    }

    @Transactional
    public void deleteTrip(int tripId) {
        int rows = tripMapper.deleteTrip(tripId);
        if (rows == 0) {
            throw new RuntimeException("해당 여행을 찾을 수 없어 삭제할 수 없습니다.");
        }
    }
    
    public TripResponseDto getTripWithItinerary(int tripId) {
    	// 1) 여행 기본 정보 조회
        TripResponseDto trip = tripMapper.getTripById(tripId);
        if (trip == null) {
            return null;
        }

        // 2) 일정 목록 조회
        List<ItineraryDto> itineraryList = tripMapper.getItinerariesByTripId(tripId);
        
        // ✅ 일정 데이터가 없으면 빈 리스트 할당
        if (itineraryList == null) {
            itineraryList = new ArrayList<>();
        }

        // 3) 날짜별로 placeName을 그룹화
        Map<String, List<String>> itineraryMap = new HashMap<>();
        for (ItineraryDto it : itineraryList) {
            // ✅ null 체크 후 처리
            if (it != null && it.getItineraryDate() != null) {
                itineraryMap
                    .computeIfAbsent(it.getItineraryDate(), k -> new ArrayList<>())
                    .add(it.getPlaceName() != null ? it.getPlaceName() : "미정");
            }
        }
        trip.setItinerary(itineraryMap);

        // 4) 여행 기간 전체 날짜 범위 생성
        List<String> fullRange = generateDateRange(trip.getStartDate(), trip.getEndDate());

        // 5) DB에 있는 일정 날짜 목록
        Set<String> usedDates = itineraryMap.keySet(); // 실제 일정이 있는 날짜들

        // 6) fullRange + usedDates 병합 후 정렬
        Set<String> mergedSet = new HashSet<>(fullRange);
        mergedSet.addAll(usedDates);
        List<String> mergedDates = new ArrayList<>(mergedSet);
        Collections.sort(mergedDates);

        // 7) TripResponseDto에 일정 날짜 목록 세팅
        trip.setItineraryDates(mergedDates);

        return trip;
    }

}
