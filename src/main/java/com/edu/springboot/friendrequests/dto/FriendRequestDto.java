package com.edu.springboot.friendrequests.dto;

import com.edu.springboot.friendrequests.entity.FriendRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestDto {
    private Long requestId;
    private Long requesterUserId;
    private String requesterNickname;
    private String requesterEmail;
    private String status;

    /** 📌 FriendRequest 엔티티를 DTO로 변환 */
    public FriendRequestDto(FriendRequest request) {
        this.requestId = request.getId(); // ✅ 수정된 부분
        this.requesterUserId = (request.getRequester() != null) ? request.getRequester().getUserId() : null;
        this.requesterNickname = (request.getRequester() != null) ? request.getRequester().getNickname() : null;
        this.requesterEmail = (request.getRequester() != null) ? request.getRequester().getEmail() : null;
        this.status = request.getStatus();
    }

    /** 📌 별도 필드 기반 생성자 추가 */
    public FriendRequestDto(Long requestId, Long requesterUserId, String requesterNickname, String requesterEmail, String status) {
        this.requestId = requestId;
        this.requesterUserId = requesterUserId;
        this.requesterNickname = requesterNickname;
        this.requesterEmail = requesterEmail;
        this.status = status;
    }
}
