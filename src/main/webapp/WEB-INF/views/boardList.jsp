<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 부트스트랩, jQuery 사용을 위한 CDN 추가 -->
<script src="/webjars/jquery/3.7.1/jquery.js"></script>
<script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.css" />
<script>
//jQuery의 엔트리 포인트 정의
$(function(){
	// 이 버튼을 클릭하면 ajax함수를 호출
	$('#btnBoard').click(function(){
		$.ajax({
			type : 'get',		// 전송방식은 get방식
			url : './restBoardList.do',		// 요청URL
			data : {pageNum : $('#pageNum').val()},	// 요청시 전송할 파라미터(페이지번호)
			contentType : "json",		// 콜백데이터의 타입은 JSON
			// 성공, 실패시 호출되는 콜백함수는 외부 JS함수로 정의
			success : sucCallBack,
			error : errCallBack
		});
	});
	// 페이지의 로드가 완료되면 click 이벤트를 트리거한다. 즉 이벤트를 자동실행한다.
	$('#btnBoard').trigger('click');
});
// 성공시 호출되는 콜백함수
function sucCallBack(resData){
	let tableData = "";
	// 콜백데이터의 크기만큼 자동으로 반복해서 <tr> 태그를 추가한다.
	$(resData).each(function(index, data){
		tableData += ""
		+ "<tr>"
		+ "		<td>" + data.num + "</td>"
		+ "		<td>" + data.title + "</td>"
		+ "		<td>" + data.id + "</td>"
		+ "		<td>" + data.postdate + "</td>"
		+ "		<td>" + data.visitcount + "</td>"
		+ "<tr>";
	});
	// #show_data 영역에 삽입한다.
	$('#show_data').html(tableData)
}
// 요청에 실패했을 때 호출되는 콜백함수
function errCallBack(errData) {
	console.log(errData.status+":"+errData.statusText);
}
</script>
</head>
<body>
<div class="container">
	<h2>게시판 API 활용하여 목록 출력하기</h2>
	<table class="table table-bordered">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>아이디</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<tbody id="show_data"> </tbody>
	</table>
	<div>
		<select id="pageNum">
		<c:forEach begin="1" end="10" var="num">
			<option value="${num }">${num }page</option>
		</c:forEach>
		</select>
		<input type="button" value="목록불러오기" id="btnBoard" />
	</div>
</div>
</body>
</html>

















