package com.edu.springboot.service;

import com.edu.springboot.dto.TripRequestDto;
import com.edu.springboot.jdbc.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

    @Autowired
    private TripMapper tripMapper;

    @Transactional
    public void createTrip(TripRequestDto tripRequest) {
        tripMapper.insertTrip(tripRequest);
    }
}
