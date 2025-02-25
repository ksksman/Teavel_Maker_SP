package com.edu.springboot.service;

import com.edu.springboot.dto.*;
import com.edu.springboot.jdbc.TripMapper;
import com.edu.springboot.jdbc.TripParticipantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TripService {

    @Autowired
    private TripMapper tripMapper;
    
    @Autowired
    private TripParticipantMapper tripParticipantMapper;

    @Transactional
    public void createTrip(TripRequestDto tripRequest) {
        tripMapper.insertTrip(tripRequest);
        tripMapper.insertDefaultTripReview(tripRequest.getTripId());
        tripMapper.insertDefaultItinerary(tripRequest.getTripId(), tripRequest.getStartDate());
    }

    @Transactional
    public void addTripParticipant(int tripId, int userId) {
        int rows = tripParticipantMapper.insertTripParticipant(tripId, userId);
        if (rows == 0) {
            throw new RuntimeException("동행자 추가 실패");
        }
    }

    public List<TripParticipantDto> getTripParticipants(int tripId) {
        return tripParticipantMapper.getTripParticipants(tripId);
    }

    // userId를 매개변수로 받아, 내가 생성했거나 참여한 여행을 모두 조회
    public List<TripResponseDto> getAllTrips(int userId) {
        List<TripResponseDto> trips = tripMapper.getAllTrips(userId);
        for (TripResponseDto trip : trips) {
            List<TripParticipantDto> participants = tripParticipantMapper.getTripParticipants(trip.getTripId());
            List<String> participantNames = new ArrayList<>();
            for (TripParticipantDto p : participants) {
                participantNames.add(p.getNickname());
            }
            trip.setParticipantNames(participantNames);
        }
        return trips;
    }

    private List<String> generateDateRange(String start, String end) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            while (!cal.getTime().after(endDate)) {
                result.add(sdf.format(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TripResponseDto getTripById(int tripId) {
        TripResponseDto trip = tripMapper.getTripById(tripId);
        if (trip == null) return null;

        List<ItineraryDto> itineraryList = tripMapper.getItinerariesByTripId(tripId);
        Map<String, List<String>> itineraryMap = new HashMap<>();
        for (ItineraryDto it : itineraryList) {
            itineraryMap.computeIfAbsent(it.getItineraryDate(), k -> new ArrayList<>())
                        .add(it.getPlaceName());
        }
        trip.setItinerary(itineraryMap);

        List<String> fullRange = generateDateRange(trip.getStartDate(), trip.getEndDate());
        Set<String> mergedSet = new HashSet<>(fullRange);
        mergedSet.addAll(itineraryMap.keySet());
        List<String> mergedDates = new ArrayList<>(mergedSet);
        Collections.sort(mergedDates);
        trip.setItineraryDates(mergedDates);

        List<TripParticipantDto> participants = tripParticipantMapper.getTripParticipants(tripId);
        List<String> participantNames = new ArrayList<>();
        for (TripParticipantDto p : participants) {
            participantNames.add(p.getNickname());
        }
        trip.setParticipantNames(participantNames);

        return trip;
    }

    @Transactional
    public void updateTripReview(int tripId, TripReviewDto reviewDto) {
        int rows = tripMapper.updateTripReview(tripId, reviewDto.getReview(), reviewDto.getRating());
        if (rows == 0) {
            rows = tripMapper.insertTripReview(tripId, reviewDto.getReview(), reviewDto.getRating());
            if (rows == 0) {
                throw new RuntimeException("후기 업데이트 실패");
            }
        }
    }

    @Transactional
    public void updateTripReviewImage(int tripId, String imageUrl) {
        tripMapper.updateTripReviewImage(tripId, imageUrl);
    }
    
    @Transactional
    public void deleteTrip(int tripId) {
        int rows = tripMapper.deleteTrip(tripId);
        if (rows == 0) {
            throw new RuntimeException("해당 여행을 찾을 수 없어 삭제할 수 없습니다.");
        }
    }
}
