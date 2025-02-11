<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/webjars/jquery/3.7.1/jquery.js"></script>
<script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.css" />
<script>
$(function(){
	$('#btnBoard').click(function(){
		$.ajax({
			type : 'get',		
			url : './restBoardView.do'
			// 파라미터 : 조회할 게시물의 일련번호.
			data : {num : $('#num').val()},
			contentType : "text/html;charset:utf-8",
			dataType : "json",
			
			success : sucCallBack,
			error : errCallBack
		});
	});
	$('#btnBoard').trigger('click');
});
// 성공시 호출되는 콜백함수
function sucCallBack(resData){
	let tableData = "";
	// 각 항목에 맞도록 파싱한 후 삽입한다.
	$('#td1').html(resData.num);
	$('#td2').html(resData.id);
	$('#td3').html(resData.postdate);
	$('#td4').html(resData.visitcount);
	$('#td5').html(resData.title);
	$('#td6').html(resData.content);
}
// 요청에 실패했을 때 호출되는 콜백함수
function errCallBack(errData) {
	console.log(errData.status+":"+errData.statusText);
}
</script>
</head>
<body>
<div class="container">
	<h2>게시판 API 활용하여 니용 출력하기</h2>
	<table class="table table-bordered">
		<tr>
			<th>번호</th><td id="td1"></td>
			<th>아이디</th><td id="td2"></td>
		</tr>
		<tr>
			<th>작성일</th><td id="td3"></td>
			<th>조회수</th><td id="td4"></td>
		</tr>
		<tr>
			<th>제목</th><td colspan="3" id="td5"></td>
		</tr>
		<tr>
			<th>제목</th><td colspan="3" id="td6"></td>
		</tr>
		<tbody id="show_data"> </tbody>
	</table>
	<div>
		<input type="number" value="47" id="num" />
		<input type="button" value="내용불러오기" id="btnBoard" />
	</div>
</div>
</body>
</html>

















