package com.edu.springboot.controller;

import com.edu.springboot.dto.TripParticipantDto;
import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.dto.TripResponseDto;
import com.edu.springboot.dto.TripReviewDto;
import com.edu.springboot.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTrip(@RequestBody TripRequestDto tripRequest) {
        tripService.createTrip(tripRequest);
        return ResponseEntity.ok(Map.of("message", "여행이 생성되었습니다.", "tripId", tripRequest.getTripId()));
    }

    // 수정: 사용자 ID를 받아서 생성한 여행과 참여한 여행을 모두 조회하도록 함
    @GetMapping
    public ResponseEntity<List<TripResponseDto>> getAllTrips(@RequestParam("userId") int userId) {
        return ResponseEntity.ok(tripService.getAllTrips(userId));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDto> getTripDetail(@PathVariable("tripId") int tripId) {
        TripResponseDto trip = tripService.getTripById(tripId);
        if (trip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}/review")
    public ResponseEntity<String> updateTripReview(@PathVariable("tripId") int tripId,
                                                   @RequestBody TripReviewDto reviewDto) {
        tripService.updateTripReview(tripId, reviewDto);
        return ResponseEntity.ok("후기가 저장되었습니다.");
    }

    @PutMapping("/{tripId}/review/image")
    public ResponseEntity<String> updateTripReviewImage(@PathVariable("tripId") int tripId,
                                                        @RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("image");
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("유효한 이미지 URL이 제공되지 않았습니다.");
        }
        tripService.updateTripReviewImage(tripId, imageUrl);
        return ResponseEntity.ok("이미지가 업데이트되었습니다.");
    }
    
    @DeleteMapping("/{tripId}")
    public ResponseEntity<String> deleteTrip(@PathVariable("tripId") int tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok("여행이 삭제되었습니다.");
    }
    
    @PostMapping("/{tripId}/participants")
    public ResponseEntity<Map<String, Object>> addTripParticipant(
            @PathVariable("tripId") int tripId,
            @RequestBody Map<String, Integer> payload) {
        int userId = payload.get("userId");
        tripService.addTripParticipant(tripId, userId);
        return ResponseEntity.ok(Map.of("message", "동행자가 추가되었습니다."));
    }
    
    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<TripParticipantDto>> getTripParticipants(@PathVariable("tripId") int tripId) {
        List<TripParticipantDto> participants = tripService.getTripParticipants(tripId);
        return ResponseEntity.ok(participants);
    }
    @GetMapping("/tripWithItinerary/{tripId}")
    public ResponseEntity<TripResponseDto> getTripWithItinerary(@PathVariable("tripId") int tripId) {
        TripResponseDto trip = tripService.getTripWithItinerary(tripId);
        if (trip == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(trip);
    }

}
