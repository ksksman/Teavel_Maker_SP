package com.edu.springboot.controller;

import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.dto.TripResponseDto;
import com.edu.springboot.dto.TripReviewDto;
import com.edu.springboot.dto.ImageDto;
import com.edu.springboot.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTrip(@RequestBody TripRequestDto tripRequest) {
        tripService.createTrip(tripRequest);
        // 생성 후 tripRequest의 tripId가 채워져 있음 (SelectKey 활용)
        return ResponseEntity.ok(Map.of("message", "여행이 저장되었습니다.", "tripId", tripRequest.getTripId()));
    }

    @GetMapping
    public ResponseEntity<List<TripResponseDto>> getTrips() {
        List<TripResponseDto> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
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
                                                        @RequestBody ImageDto imageDto) {
        String imageUrl = imageDto.getImage();
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
}
