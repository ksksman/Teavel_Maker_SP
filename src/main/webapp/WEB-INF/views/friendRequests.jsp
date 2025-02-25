<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>친구 요청 목록</title>
    <script>
        function acceptFriendRequest(requestId) {
            fetch('/api/friends/accept', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ requestId: requestId })
            })
            .then(response => response.json()) // 응답을 JSON으로 변환
            .then(data => {
                alert(data.message);
                location.reload();
            })
            .catch(error => console.error('Error:', error));
        }

        function rejectFriendRequest(requestId) {
            fetch('/api/friends/reject', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ requestId: requestId })
            })
            .then(response => response.json()) 
            .then(data => {
                alert(data.message);
                location.reload();
            })
            .catch(error => console.error('Error:', error));
        }
    </script>
</head>
<body>

    <h1>친구 요청 목록</h1>

    <c:choose>
        <c:when test="${empty requests}">
            <p>친구 요청이 없습니다.</p>
        </c:when>
        <c:otherwise>
            <table border="1">
                <tr>
                    <th>닉네임</th>
                    <th>이메일</th>
                    <th>상태</th>
                    <th>액션</th>
                </tr>
                <c:forEach var="request" items="${requests}">
                    <tr>
                        <td>${request.requesterNickname}</td>
                        <td>${request.requesterEmail}</td>
                        <td>${request.status}</td>
                        <td>
                            <button onclick="acceptFriendRequest(${request.requestId})">수락</button>
                            <button onclick="rejectFriendRequest(${request.requestId})">거절</button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>
