package com.edu.springboot.friendrequests.controller;

import com.edu.springboot.friendrequests.dto.FriendRequestDto;
import com.edu.springboot.friendrequests.entity.FriendRequest;
import com.edu.springboot.friendrequests.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")  // ✅ REST API 경로 통일
public class FriendRequestRestController {

    private final FriendRequestService friendRequestService;
    
    public FriendRequestRestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    // 📌 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<Map<String, String>> sendFriendRequest(@RequestBody Map<String, Object> requestData) {
        Long requesterId = Long.valueOf(requestData.get("requesterId").toString());
        Long receiverId = Long.valueOf(requestData.get("receiverId").toString());

        System.out.println("🔥 요청자 ID: " + requesterId + ", 수신자 ID: " + receiverId); // ✅ 디버깅 로그 추가

        Map<String, String> response = new HashMap<>();
        if (requesterId == null || receiverId == null) {
            response.put("message", "❌ 요청자 또는 수신자 ID가 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            response.put("message", friendRequestService.sendFriendRequest(requesterId, receiverId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "❌ 친구 요청 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    // 📌 받은 친구 요청 목록 조회 (GET /api/friends/requests?userId=1)
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDto>> getReceivedRequests(@RequestParam(name = "userId") Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(friendRequestService.getReceivedRequests(userId));
    }

 // 📌 친구 요청 수락 (PATCH)
    @PatchMapping("/{id}/accept")
    public ResponseEntity<Map<String, String>> acceptFriendRequest(@PathVariable("id") Long id) {  // 🔥 수정된 부분
        Map<String, String> response = new HashMap<>();

        try {
            response.put("message", friendRequestService.acceptFriendRequest(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "❌ 친구 요청 수락 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 📌 친구 요청 거절 (PATCH)
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectFriendRequest(@PathVariable("id") Long id) {  // 🔥 수정된 부분
        Map<String, String> response = new HashMap<>();

        try {
            response.put("message", friendRequestService.rejectFriendRequest(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "❌ 친구 요청 거절 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

 // 📌 친구 목록 조회 (ACCEPTED 상태만 반환)
    @GetMapping("/list")
    public ResponseEntity<List<FriendRequest>> getFriendList(@RequestParam(name = "userId") Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<FriendRequest> acceptedFriends = friendRequestService.getFriendList(userId);
        return ResponseEntity.ok(acceptedFriends); // ✅ 수정된 메서드 호출
    }




}
