package com.edu.springboot.mapper;

import com.edu.springboot.dto.TravelPointDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelPointMapper {
    void insertTravelPoints(List<TravelPointDto> places);
}
