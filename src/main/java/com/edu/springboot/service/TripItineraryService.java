package com.edu.springboot.service;

import com.edu.springboot.dto.TripItineraryDTO;
import com.edu.springboot.mapper.TripItineraryMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TripItineraryService {
    private final TripItineraryMapper itineraryMapper;

    public TripItineraryService(TripItineraryMapper itineraryMapper) {
        this.itineraryMapper = itineraryMapper;
    }

    // 특정 여행 일정 조회
    public List<TripItineraryDTO> getItineraryByTripId(Long tripId) {
        return itineraryMapper.getItineraryByTripId(tripId);
    }

    // ✅ 관광지 일정 추가 (날짜별 SEQ 1부터 증가)
    @Transactional
    public void addItinerary(TripItineraryDTO itinerary) {
        try {
            if (itinerary.getItineraryDate() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "🚨 일정 날짜가 비어있습니다.");
            }

            LocalDate itineraryDate = itinerary.getItineraryDate();

            // ✅ 해당 날짜(itineraryDate)에서 SEQ 값 결정 (1부터 시작)
            int nextSeq = itineraryMapper.getNextSeqForDate(itinerary.getTripId(), itineraryDate);
            itinerary.setSeq(nextSeq);

            // ✅ 위도/경도 값을 소수점 6자리로 반올림하여 저장
            Double lat = (itinerary.getLat() != null) 
                ? BigDecimal.valueOf(itinerary.getLat()).setScale(6, RoundingMode.HALF_UP).doubleValue() 
                : null;

            Double lng = (itinerary.getLng() != null) 
                ? BigDecimal.valueOf(itinerary.getLng()).setScale(6, RoundingMode.HALF_UP).doubleValue() 
                : null;

            System.out.println(" 중복 체크 - tripId: " + itinerary.getTripId());
            System.out.println(" 중복 체크 - itineraryDate: " + itineraryDate);
            System.out.println(" 중복 체크 - placeName: " + itinerary.getPlaceName());
            System.out.println(" 중복 체크 - lat: " + lat + ", lng: " + lng);
            System.out.println(" 🔢 새롭게 설정된 SEQ: " + nextSeq);

            // ✅ 중복 체크 실행 (소수점 6자리까지만 비교)
            int existsCount = itineraryMapper.checkDuplicateItinerary(
                itinerary.getTripId(), itineraryDate, itinerary.getPlaceName(), lat, lng
            );

            if (existsCount > 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "⚠ 이미 추가된 관광지입니다.");
            }

            // ✅ 소수점 6자리로 변환한 좌표를 DTO에 다시 저장
            itinerary.setLat(lat);
            itinerary.setLng(lng);

            itineraryMapper.addItinerary(itinerary);
            System.out.println(" 일정이 성공적으로 추가되었습니다!");

        } catch (ResponseStatusException e) {
            // ⚠ ResponseStatusException은 스택 트레이스를 찍지 않도록 설정 (깔끔한 에러 처리)
            System.out.println(" " + e.getReason());
            throw e;
        } catch (Exception e) {
            System.err.println(" 일정 추가 오류: " + e.getMessage());
            throw new RuntimeException(" 일정 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 일정 삭제
    @Transactional
    public void deleteItinerary(Long itineraryId) {
        itineraryMapper.deleteItinerary(itineraryId);
    }

    @Transactional
    public void deleteItineraryByDate(Long tripId, String itineraryDate) {
        itineraryMapper.deleteItineraryByDate(tripId, itineraryDate);
    }

    // 일정 순서 변경
    @Transactional
    public void updateItineraryOrder(Long itineraryId, Integer seq) {
        itineraryMapper.updateItineraryOrder(itineraryId, seq);
    }
 //  일정 순서 일괄 변경 (드래그앤드롭 적용)
    @Transactional
    public void updateItineraryOrderBatch(List<TripItineraryDTO> updatedItineraryList) {
        try {
            for (TripItineraryDTO itinerary : updatedItineraryList) {
                if (itinerary.getItineraryId() == null) {
                    throw new IllegalArgumentException("❌ 오류: itineraryId가 null입니다.");
                }
                System.out.println("📌 업데이트할 일정: ID=" + itinerary.getItineraryId() + ", SEQ=" + itinerary.getSeq());
                itineraryMapper.updateItineraryOrder(itinerary.getItineraryId(), itinerary.getSeq());
            }
            System.out.println("✅ 일정 순서 변경 완료!");
        } catch (Exception e) {
            System.err.println("❌ 일정 순서 업데이트 오류: " + e.getMessage());
            throw new RuntimeException("🚨 일정 순서 업데이트 중 오류 발생: " + e.getMessage());
        }
    }



}
