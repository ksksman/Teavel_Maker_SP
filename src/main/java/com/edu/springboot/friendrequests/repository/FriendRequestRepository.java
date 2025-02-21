package com.edu.springboot.friendrequests.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edu.springboot.friendrequests.entity.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // ✅ PENDING 상태의 친구 요청 조회 (이미 있는 코드)
    List<FriendRequest> findByReceiver_UserIdAndStatus(Long userId, String status);

    // ✅ ACCEPTED 상태의 친구 목록 조회 (쿼리 수정)
    @Query("SELECT f FROM FriendRequest f WHERE (f.requester.id = :userId OR f.receiver.id = :userId) AND f.status = 'ACCEPTED'")
    List<FriendRequest> findFriendsByUserId(@Param("userId") Long userId);
}
