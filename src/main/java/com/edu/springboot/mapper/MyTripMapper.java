package com.edu.springboot.mapper;

import com.edu.springboot.dto.MyTripDto;

import com.edu.springboot.dto.ParticipantDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyTripMapper {
    MyTripDto getTripById(@Param("tripId") Long tripId);
    List<ParticipantDto> getParticipantsByTripId(@Param("tripId") Long tripId);
  
}