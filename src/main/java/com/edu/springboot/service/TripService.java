package com.edu.springboot.service;

import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.dto.TripResponseDto;
import com.edu.springboot.dto.TripReviewDto;
import com.edu.springboot.jdbc.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripMapper tripMapper;

    @Transactional
    public void createTrip(TripRequestDto tripRequest) {
        // TRIP 테이블에 여행 정보 저장
        tripMapper.insertTrip(tripRequest);
        // TRIP_REVIEW 테이블에 기본 리뷰 행 삽입 (후기는 NULL, 상태는 '계획중')
        tripMapper.insertDefaultTripReview(tripRequest.getTripId());
        // TRIP_ITINERARY 테이블에 기본 일정 행 삽입 (ITINERARY_DATE는 startDate)
        tripMapper.insertDefaultItinerary(tripRequest.getTripId(), tripRequest.getStartDate());
    }

    public List<TripResponseDto> getAllTrips() {
        return tripMapper.getAllTrips();
    }

    public TripResponseDto getTripById(int tripId) {
        return tripMapper.getTripById(tripId);
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
}
