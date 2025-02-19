package com.edu.springboot.controller;

import com.edu.springboot.dto.PlaceResponseDto;
import com.edu.springboot.service.TourApiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class TourApiController {

    private final TourApiService tourApiService;

    public TourApiController(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    //  관광지 검색 API (React에서 호출)
    @GetMapping("/search")
    public List<PlaceResponseDto> searchPlaces(
        @RequestParam(name = "query") String query, // ✅ 명확하게 파라미터명 지정
        @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
        @RequestParam(name = "numOfRows", defaultValue = "10") int numOfRows
    ) {
        return tourApiService.searchTouristPlaces(query, pageNo, numOfRows);
    }

    @GetMapping("/overview")
    public String getPlaceOverview(@RequestParam(name = "contentId") String contentId) { // ✅ 명확하게 지정
        return tourApiService.getTouristPlaceOverview(contentId);
    }

}
