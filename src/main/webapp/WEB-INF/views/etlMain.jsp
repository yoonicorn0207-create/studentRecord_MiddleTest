<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ETL(AWS) Dashboard - 중간고사 (최창윤)</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; }
    </style>
</head>
<body class="bg-gray-50 min-h-screen">
    <div class="max-w-6xl mx-auto px-4 py-8">
        <!-- Header -->
        <div class="flex justify-between items-center mb-8 bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">ETL 데이터 관리시스템</h1>
                <p class="text-gray-500 text-sm">로컬 DB(KOPODB) To 클라우드DB(AWS_KOPO) -> ETL 이후 데이터 이관</p>
            </div>
            
            <button id="refineBtn" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-xl transition duration-200 shadow-lg shadow-blue-200 flex items-center gap-2">
                <span id="btnText">정제하기 (ETL Start)</span>
                <svg id="loadingSpinner" class="hidden animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
            </button>
        </div>

        <div class="grid grid-cols-1 gap-8">
            <!-- Raw Data Table -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                <div class="px-6 py-4 border-b border-gray-100 bg-gray-50 flex justify-between items-center">
                    <h2 class="font-semibold text-gray-700">원천 데이터 (KOPODB - yoon_raw_orders)</h2>
                    <span class="text-xs font-medium px-2.5 py-0.5 rounded-full bg-blue-100 text-blue-800">Total: ${rawOrders.size()}</span>
                </div>
                
                <div class="overflow-x-auto max-h-64">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 text-gray-500 uppercase text-xs font-bold sticky top-0">
                            <tr>
                                <th class="px-6 py-4">주문번호</th>
                                <th class="px-6 py-4">상호명</th>
                                <th class="px-6 py-4 text-center">주문금액</th>
                                <th class="px-6 py-4">주문상태</th>
                                <th class="px-6 py-4">주문발생일시</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100">
                            <c:forEach var="order" items="${rawOrders}">
                                <tr class="hover:bg-gray-50 transition duration-150">
                                    <td class="px-6 py-4 text-sm font-medium text-gray-900">${order.orderId}</td>
                                    <td class="px-6 py-4 text-sm text-gray-600">${order.productId}</td>
                                    <td class="px-6 py-4 text-sm text-gray-600 text-center">
                                        <span class="font-mono font-bold text-blue-600">
                                            <fmt:formatNumber value="${order.qty}" type="number" />
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 text-sm">
                                        <c:choose>
                                            <c:when test="${order.status == 'SUCCESS'}">
                                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold bg-green-100 text-green-800">
                                                    ${order.status}
                                                </span>
                                            </c:when>
                                            <c:when test="${order.status == 'CANCELED'}">
                                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold bg-red-100 text-red-800">
                                                    ${order.status}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold bg-gray-100 text-gray-800">
                                                    ${order.status}
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 text-sm text-gray-400 italic">${order.createdAt}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Cleaned Data List (Cloud DB) -->
            <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                <div class="px-6 py-4 border-b border-gray-100 bg-gray-900 text-white flex justify-between items-center">
                    <div class="flex items-center gap-4">
                        <h2 class="font-semibold">ETL 결과 데이터 (AWS_KOPO DB - yoon_clean_settlements)</h2>
                        <c:if test="${not empty cleanSettlements}">
                            <div class="flex gap-2">
                                <a href="${pageContext.request.contextPath}/yoon/etl/download/csv" class="bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold py-1.5 px-4 rounded-lg transition duration-200 flex items-center gap-1">
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a2 2 0 002 2h12a2 2 0 002-2v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                                    </svg>
                                    CSV 다운로드
                                </a>
                                <a href="${pageContext.request.contextPath}/yoon/etl/download/excel" class="bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-bold py-1.5 px-4 rounded-lg transition duration-200 flex items-center gap-1">
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                                    </svg>
                                    Excel 다운로드
                                </a>
                            </div>
                        </c:if>
                    </div>
                    <span class="text-xs font-medium px-2.5 py-0.5 rounded-full bg-blue-500 text-white">Cloud Synced</span>
                </div>
                
                <div class="overflow-x-auto max-h-80">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 text-gray-500 uppercase text-xs font-bold sticky top-0">
                            <tr>
                                <th class="px-6 py-4">일련번호</th>
                                <th class="px-6 py-4">가맹점명</th>
                                <th class="px-6 py-4 text-center">매출금액</th>
                                <th class="px-6 py-4 text-center">중개수수료</th>
                                <th class="px-6 py-4 text-center">정산금액</th>
                                <th class="px-6 py-4">정산기준일</th>
                                <th class="px-6 py-4">정제&이관일시</th>
                            </tr>
                        </thead>
                        <tbody id="cleanListBody" class="divide-y divide-gray-100">
                            <c:forEach var="item" items="${cleanSettlements}">
                                <tr class="hover:bg-blue-50/30 transition duration-150">
                                    <td class="px-6 py-4 text-sm font-medium text-gray-900">${item.getSeq()}</td>
                                    <td class="px-6 py-4 text-sm text-gray-600">${item.getStoreName()}</td>
                                    <td class="px-6 py-4 text-sm text-gray-600 text-center font-bold text-blue-600">
                                        <fmt:formatNumber value="${item.getSalesAmount()}" type="number" />
                                    </td>
                                    <td class="px-6 py-4 text-sm text-red-500 text-center font-bold">
                                        <fmt:formatNumber value="${item.getCommissionFee()}" type="number" />
                                    </td>
                                    <td class="px-6 py-4 text-sm text-green-600 text-center font-bold">
                                        <fmt:formatNumber value="${item.getFinalAmount()}" type="number" />
                                    </td>
                                    <td class="px-6 py-4 text-sm text-gray-500">${item.getOrderDate()}</td>
                                    <td class="px-6 py-4 text-sm text-gray-400 italic">${item.getTransferDate()}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty cleanSettlements}">
                                <tr id="noResultRow">
                                    <td colspan="7" class="px-6 py-10 text-center text-gray-400 italic">
                                        클라우드 DB에 정제된 데이터가 없습니다.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <footer class="mt-8 text-center text-gray-400 text-sm">
            &copy; 2026 ETL System Made By - [스마트 금융9기]최창윤. Connected to Local DB.
        </footer>
    </div>

    <script>
        const refineBtn = document.getElementById('refineBtn');
        const btnText = document.getElementById('btnText');
        const loadingSpinner = document.getElementById('loadingSpinner');
        const cleanListBody = document.getElementById('cleanListBody');
        const noResultRow = document.getElementById('noResultRow');

        refineBtn.addEventListener('click', async () => {
            // UI 상태 변경
            refineBtn.disabled = true;
            refineBtn.classList.add('opacity-75', 'cursor-not-allowed');
            btnText.innerText = '정제 중...';
            loadingSpinner.classList.remove('hidden');

            try {
                // Controller의 RequestMapping이 /yoon/etl 이므로 URL을 수정합니다.
                const response = await fetch('${pageContext.request.contextPath}/yoon/etl/start-ajax', {
                    method: 'POST'
                });

                if (!response.ok) throw new Error('서버 통신 실패 (HTTP ' + response.status + ')');

                const data = await response.json();
                
                const isSuccess = data.status === 'SUCCESS' || data.status === 'success';

                if (isSuccess) {
                    alert('정제가 성공적으로 완료되었습니다! 클라우드 데이터를 갱신합니다.');
                    location.reload();
                } else {
                    alert('정제 실패: ' + (data.message || '알 수 없는 오류가 발생했습니다.'));
                }

            } catch (error) {
                alert('에러 발생: ' + error.message);
            } finally {
                refineBtn.disabled = false;
                refineBtn.classList.remove('opacity-75', 'cursor-not-allowed');
                btnText.innerText = '정제하기 (ETL Start)';
                loadingSpinner.classList.add('hidden');
            }
        });
    </script>
</body>
</html>
