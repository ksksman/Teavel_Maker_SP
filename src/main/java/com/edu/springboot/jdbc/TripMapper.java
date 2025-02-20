package com.edu.springboot.jdbc;

import com.edu.springboot.dto.TripRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface TripMapper {

    // 먼저 TRIP_SEQ.NEXTVAL을 조회하여 tripId에 할당한 후, insert문에 사용
    @SelectKey(statement = "SELECT TRIP_SEQ.NEXTVAL FROM DUAL", keyProperty = "tripId", before = true, resultType = int.class)
    @Insert("INSERT INTO TRIP (TRIP_ID, USER_ID, TRIP_TITLE, START_DATE, END_DATE, CREATED_DATE) " +
            "VALUES (#{tripId}, #{userId}, #{title}, TO_DATE(#{startDate}, 'YYYY-MM-DD'), " +
            "TO_DATE(#{endDate}, 'YYYY-MM-DD'), CURRENT_TIMESTAMP)")
    void insertTrip(TripRequestDto tripRequest);
}
