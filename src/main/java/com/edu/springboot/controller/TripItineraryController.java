package com.edu.springboot.controller;

import com.edu.springboot.dto.TripItineraryDTO;
import com.edu.springboot.service.TripItineraryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/itinerary")
public class TripItineraryController {
    private final TripItineraryService itineraryService;

    public TripItineraryController(TripItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    // 특정 여행의 일정 조회
    @GetMapping("/{tripId}")
    public List<TripItineraryDTO> getItineraryByTripId(@PathVariable("tripId") Long tripId) {
        System.out.println("받은 tripId: " + tripId); // 디버깅용 로그
        return itineraryService.getItineraryByTripId(tripId);
    }

    // 관광지 일정 추가
    @PostMapping("/add")
    public void addItinerary(@RequestBody TripItineraryDTO itinerary) {
        itineraryService.addItinerary(itinerary);
    }

    // 일정 삭제
    @DeleteMapping("/delete/{itineraryId}")
    public void deleteItinerary(@PathVariable("itineraryId") Long itineraryId) {
        itineraryService.deleteItinerary(itineraryId);
    }

    // 일정 순서 변경
    @PutMapping("/update-order")
    public void updateItineraryOrder(
            @RequestParam("itineraryId") Long itineraryId,
            @RequestParam("seq") Integer seq) {
        itineraryService.updateItineraryOrder(itineraryId, seq);
    }
    
   
    // ✅ 드래그앤드롭으로 일정 순서 변경
    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateItineraryOrder(@RequestBody List<TripItineraryDTO> updatedItineraryList) {
        try {
            System.out.println("🔄 순서 변경 요청 도착! 데이터 확인:");
            for (TripItineraryDTO itinerary : updatedItineraryList) {
                System.out.println("📌 ID: " + itinerary.getItineraryId() + ", SEQ: " + itinerary.getSeq() + ", tripId: " + itinerary.getTripId());
                if (itinerary.getItineraryId() == null) {
                    throw new IllegalArgumentException("❌ 오류: itineraryId가 null입니다.");
                }
            }

            itineraryService.updateItineraryOrderBatch(updatedItineraryList);
            return ResponseEntity.ok("✅ 일정 순서가 변경되었습니다.");
        } catch (Exception e) {
            System.err.println("❌ 순서 변경 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ 순서 변경 실패: " + e.getMessage());
        }
    }


    //해당 날짜의 관광지 삭제
    @DeleteMapping("/delete-by-date")
    public ResponseEntity<?> deleteItineraryByDate(@RequestBody Map<String, String> requestBody) {
        Long tripId = Long.parseLong(requestBody.get("tripId"));
        String itineraryDate = requestBody.get("itineraryDate");

        if (tripId == null || itineraryDate == null) {
            return ResponseEntity.badRequest().body("🚨 tripId와 itineraryDate가 필요합니다.");
        }

        try {
            itineraryService.deleteItineraryByDate(tripId, itineraryDate);
            return ResponseEntity.ok(itineraryDate + "의 모든 관광지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패: " + e.getMessage());
        }
    }
    
    


}
