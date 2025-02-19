package com.edu.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor //  모든 필드를 포함하는 생성자 자동 생성
public class PlaceResponseDto {
    private String id;
    private String name;
    private double lat;
    private double lng;
    private String address;
    private String imageUrl;

    // 기본 생성자 추가 
    public PlaceResponseDto() {}
}
