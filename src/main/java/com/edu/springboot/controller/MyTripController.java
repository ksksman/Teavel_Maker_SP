package com.edu.springboot.controller;

import com.edu.springboot.dto.MyTripDto;
import com.edu.springboot.dto.ParticipantDto;
import com.edu.springboot.service.MyTripService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class MyTripController {
	
    private final MyTripService myTripService;

    public MyTripController(MyTripService myTripService) {
        this.myTripService = myTripService;
    }

    // 여행 정보 API
    @GetMapping("/{tripId}")
    public MyTripDto getTripById(@PathVariable("tripId") Long tripId) {
        return myTripService.getTripById(tripId);
    }

    // 참여자 목록 API
    @GetMapping("/{tripId}/participants")
    public List<ParticipantDto> getParticipantsByTripId(@PathVariable("tripId") Long tripId) {
        return myTripService.getParticipantsByTripId(tripId);
    }
    

}