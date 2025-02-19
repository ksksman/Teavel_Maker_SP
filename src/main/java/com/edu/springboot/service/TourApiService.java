package com.edu.springboot.service;

import com.edu.springboot.dto.PlaceResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class TourApiService {

    private final RestTemplate restTemplate;

    // ✅ application.properties에서 API 키 불러오기
    @Value("${api.key}")
    private String apiKey;

    public TourApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 🔍 관광지 검색 (키워드 기반, 페이지네이션 적용)
    public List<PlaceResponseDto> searchTouristPlaces(String keyword, int pageNo, int numOfRows) {
        List<PlaceResponseDto> places = new ArrayList<>();

        try {
            // 🔹 API Key와 검색어 인코딩
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            // 🔹 API URL 구성
            String apiUrl = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1"
                    + "?serviceKey=" + apiKey
                    + "&MobileOS=ETC"
                    + "&MobileApp=AppName"
                    + "&_type=xml"
                    + "&numOfRows=" + numOfRows  
                    + "&pageNo=" + pageNo        
                    + "&contentTypeId=12"
                    + "&keyword=" + encodedKeyword;

            // 🔹 API 요청 보내기
            URI uri = URI.create(apiUrl);
            String responseXml = restTemplate.getForObject(uri, String.class);

            // 🔹 응답이 없는 경우 예외 처리
            if (responseXml == null || responseXml.isEmpty()) {
                throw new RuntimeException("API 응답이 비어 있습니다.");
            }

            places = parseXmlResponse(responseXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return places;
    }

    // 🔍 특정 관광지 개요 조회 (contentId 이용)
    public String getTouristPlaceOverview(String contentId) {
        try {
            // 🔹 API URL 구성
            String apiUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1"
                    + "?serviceKey=" + apiKey
                    + "&MobileOS=ETC"
                    + "&MobileApp=AppName"
                    + "&_type=xml"
                    + "&contentId=" + contentId
                    + "&overviewYN=Y"; //  콘텐츠 개요 조회

            // 🔹 API 요청 보내기
            URI uri = URI.create(apiUrl);
            String responseXml = restTemplate.getForObject(uri, String.class);

            // 🔹 응답이 없는 경우 예외 처리
            if (responseXml == null || responseXml.isEmpty()) {
                throw new RuntimeException("API 응답이 비어 있습니다.");
            }

            return parseOverviewFromXml(responseXml);
        } catch (Exception e) {
            e.printStackTrace();
            return "설명 없음";
        }
    }

    // 🔄 XML 응답에서 콘텐츠 개요 추출
    private String parseOverviewFromXml(String xml) {
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            doc.getDocumentElement().normalize();

            return getTagValue("overview", doc.getDocumentElement());
        } catch (Exception e) {
            e.printStackTrace();
            return "설명 없음";
        }
    }

    // 🔍 특정 태그 값 가져오기
    private String getTagValue(String tag, org.w3c.dom.Element element) {
        org.w3c.dom.NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) return "설명 없음";
        return nodeList.item(0).getTextContent();
    }

    // 🔄 XML 응답을 파싱하여 DTO 변환
    private List<PlaceResponseDto> parseXmlResponse(String xml) {
        List<PlaceResponseDto> places = new ArrayList<>();
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            doc.getDocumentElement().normalize();

            org.w3c.dom.NodeList nodeList = doc.getElementsByTagName("item");

            if (nodeList.getLength() == 0) {
                System.out.println("⚠️ 응답에 데이터가 없습니다!");
            }

            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) nodeList.item(i);

                String id = getTagValue("contentid", element);
                String name = getTagValue("title", element);
                double lat = Double.parseDouble(getTagValue("mapy", element));
                double lng = Double.parseDouble(getTagValue("mapx", element));
                String address = getTagValue("addr1", element);
                String imageUrl = getTagValue("firstimage", element);

                places.add(new PlaceResponseDto(id, name, lat, lng, address, imageUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }
}
