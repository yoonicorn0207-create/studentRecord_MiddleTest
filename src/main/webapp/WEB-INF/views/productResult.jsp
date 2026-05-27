<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>DB 연동</title>
<style>
body {
	font-family: sans-serif;
	text-align: center;
	padding-top: 50px;
}

.box {
	border: 2px solid #007bff;
	display: inline-block;
	padding: 20px;
	border-radius: 10px;
}

h2 {
	color: #007bff;
}
</style>
</head>
<body>
	<div class="box">
		<h2>지역별 최대 판매량 조회</h2>
		<hr>
		
		<!-- 1. 입력 폼 (결과 페이지에서도 바로 다시 검색할 수 있게 하려면 추가) -->
		<form action="getMaxQty" method="get">
			<input type="text" name="regionid" placeholder="지역 ID 입력" required>
			<button type="submit" style="background-color: #007bff; color: white; border: none; border-radius: 5px; padding: 5px 10px;">조회</button>
		</form>
		
		<hr>

		<!-- 2. 결과 출력 -->
		<p>
			<strong>조회 결과:</strong> ${msg}
		</p>
		
		<br>
		<button onclick="history.back()">뒤로가기</button>
	</div>
</body>
</html>