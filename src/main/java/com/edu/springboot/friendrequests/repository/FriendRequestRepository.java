package com.edu.springboot.friendrequests.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.edu.springboot.friendrequests.entity.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {


    // ✅ receiver의 userId를 사용해야 함
    List<FriendRequest> findByReceiver_UserIdAndStatus(Long receiverId, String status);

    // ✅ 중복 친구 요청 확인 로직 수정
    boolean existsByRequester_UserIdAndReceiver_UserId(Long requesterUserId, Long receiverUserId);

    
    // ✅ 특정 사용자의 친구 목록 조회
    @Query("SELECT f FROM FriendRequest f WHERE (f.requester.id = :userId OR f.receiver.id = :userId) AND f.status = 'ACCEPTED'")
    List<FriendRequest> findFriendsByUserId(@Param("userId") Long userId);
}
