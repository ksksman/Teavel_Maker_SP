package com.edu.springboot.friendrequests.service;

import com.edu.springboot.friendrequests.dto.FriendRequestDto;
import com.edu.springboot.friendrequests.entity.FriendRequest;
import com.edu.springboot.friendrequests.entity.User;
import com.edu.springboot.friendrequests.repository.FriendRequestRepository;
import com.edu.springboot.friendrequests.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    

    // 📌 친구 요청 보내기
    @Transactional
    public String sendFriendRequest(Long requesterUserId, Long receiverUserId) {
        Optional<User> requesterOpt = userRepository.findByUserId(requesterUserId);
        Optional<User> receiverOpt = userRepository.findByUserId(receiverUserId);

        if (requesterOpt.isEmpty() || receiverOpt.isEmpty()) {
            return "❌ 유효하지 않은 사용자 ID";
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requesterOpt.get());
        friendRequest.setReceiver(receiverOpt.get());
        friendRequest.setStatus("PENDING");

        friendRequestRepository.save(friendRequest);
        return "✅ 친구 요청을 보냈습니다.";
    }

    // 📌 받은 친구 요청 목록 조회
    public List<FriendRequestDto> getReceivedRequests(Long userId) {
        return friendRequestRepository.findByReceiver_UserIdAndStatus(userId, "PENDING")
                .stream()
                .map(FriendRequestDto::new)
                .collect(Collectors.toList());
    }

    // 📌 친구 요청 수락
    @Transactional
    public String acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ 요청이 존재하지 않습니다."));

        request.setStatus("ACCEPTED");
        friendRequestRepository.save(request);
        return "✅ 친구 요청을 수락했습니다.";
    }

    // 📌 친구 요청 거절
    @Transactional
    public String rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ 요청이 존재하지 않습니다."));

        request.setStatus("REJECTED");
        friendRequestRepository.save(request);
        return "✅ 친구 요청을 거절했습니다.";
    }

    // 📌 친구 목록 조회
    public List<FriendRequest> getFriendList(Long userId) {
        return friendRequestRepository.findFriendsByUserId(userId);
    }
}
