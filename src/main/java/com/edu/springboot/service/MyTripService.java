package com.edu.springboot.service;

import com.edu.springboot.dto.MyTripDto;

import com.edu.springboot.dto.ParticipantDto;
import com.edu.springboot.mapper.MyTripMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MyTripService {
	
    private final MyTripMapper myTripMapper;

    public MyTripService(MyTripMapper myTripMapper) {
        this.myTripMapper = myTripMapper;
    }

    // 여행 정보만 가져오기
    public MyTripDto getTripById(Long tripId) {
        return myTripMapper.getTripById(tripId);
    }

    // 참여자 목록만 가져오기
    public List<ParticipantDto> getParticipantsByTripId(Long tripId) {
        return myTripMapper.getParticipantsByTripId(tripId);
    }
    
}