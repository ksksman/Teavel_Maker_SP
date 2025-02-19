package com.edu.springboot.controller;

import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/create")
    public ResponseEntity<String> createTrip(@RequestBody TripRequestDto tripRequest) {
        tripService.createTrip(tripRequest);
        return ResponseEntity.ok("여행이 저장되었습니다.");
    }
}
