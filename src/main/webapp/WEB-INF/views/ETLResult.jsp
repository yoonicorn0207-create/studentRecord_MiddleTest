<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>ETL Result</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen flex items-center justify-center p-4">
    <div class="max-w-md w-full bg-white rounded-3xl p-8 shadow-xl border border-gray-100 text-center">
        <div class="mb-6 inline-flex items-center justify-center w-20 h-20 rounded-full ${result.status == 'SUCCESS' || result.status == 'success' ? 'bg-green-100 text-green-600' : 'bg-red-100 text-red-600'} mb-4">
            <c:choose>
                <c:when test="${result.status == 'SUCCESS' || result.status == 'success'}">
                    <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"></path>
                    </svg>
                </c:when>
                <c:otherwise>
                    <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12"></path>
                    </svg>
                </c:otherwise>
            </c:choose>
        </div>

        <h1 class="text-2xl font-bold text-gray-900 mb-2">ETL 처리 결과</h1>
        
        <div class="mb-6">
            <span class="inline-flex items-center px-4 py-1.5 rounded-full text-sm font-bold ${result.status == 'SUCCESS' || result.status == 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}">
                ${result.status}
            </span>
        </div>
        
        <div class="bg-gray-50 rounded-2xl p-6 border border-gray-100 mb-8 text-left">
            <p class="text-xs text-gray-400 uppercase font-bold mb-2 tracking-wider">Response Message</p>
            <p class="text-gray-700 leading-relaxed">${result.message}</p>
        </div>
        
        <div class="flex flex-col gap-3">
            <c:if test="${result.status == 'SUCCESS' || result.status == 'success'}">
                <a href="${pageContext.request.contextPath}/yoon/etl/download/csv" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 rounded-xl transition duration-200">
                    정제된 CSV 데이터 다운로드
                </a>
            </c:if>
            <a href="${pageContext.request.contextPath}/yoon/etl/main" class="w-full bg-gray-900 hover:bg-black text-white font-bold py-4 rounded-xl transition duration-200">
                메인 화면으로 돌아가기
            </a>
            <button onclick="history.back()" class="w-full text-gray-500 hover:text-gray-700 font-medium py-2 transition duration-200">
                이전 페이지
            </button>
        </div>
    </div>
</body>
</html>
