package com.edu.springboot.jdbc;

import com.edu.springboot.dto.TripParticipantDto;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TripParticipantMapper {

    @Insert("INSERT INTO TRIP_PARTICIPANTS (TRIP_ID, USER_ID) VALUES (#{tripId}, #{userId})")
    int insertTripParticipant(@Param("tripId") int tripId, @Param("userId") int userId);

    @Select("SELECT u.USER_ID AS userId, u.NICKNAME, u.EMAIL " +
            "FROM TRIP_PARTICIPANTS tp JOIN USERS u ON tp.USER_ID = u.USER_ID " +
            "WHERE tp.TRIP_ID = #{tripId}")
    List<TripParticipantDto> getTripParticipants(@Param("tripId") int tripId);
}
