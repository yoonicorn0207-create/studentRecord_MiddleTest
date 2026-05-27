package com.kopo.cycode.middletest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/yoon/etl")
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class ETLController {

    @Autowired
    private ETLFastApiClientService ETLService;
    
    @Autowired
    private ETLDataService dataService;

    // 메인 화면 (GET): 원천 데이터와 클라우드 정제 데이터를 모두 조회하여 전달
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String etlMainPage(Model model) {
        model.addAttribute("rawOrders", dataService.getRawOrders());
        model.addAttribute("cleanSettlements", dataService.getCleanSettlements());
        return "etlMain";
    }

    // 2. 정제하기 버튼 (POST): ETL 프로세스 실행 후 ETLResult.jsp로 이동 (기존 방식)
    @PostMapping("/start")
    public String startEtlProcess(Model model) { 
        Map<String, Object> result = ETLService.triggerFastApiEtl();
        model.addAttribute("result", result);
        return "ETLResult"; 
    }
    
    // CSV 다운로드 엔드포인트
    @RequestMapping(value = "/download/csv", method = RequestMethod.GET)
    public void downloadCsv(HttpServletResponse response) throws IOException {
        List<ETL_cleanSettlementDTO> settlements = dataService.getCleanSettlements();
        
        // 엑셀에서 한글 깨짐 방지를 위해 MS949 인코딩 설정
        response.setContentType("text/csv; charset=MS949");
        response.setHeader("Content-Disposition", "attachment; filename=\"clean_settlements.csv\"");
        
        PrintWriter writer = response.getWriter();
        // CSV 헤더 작성
        writer.println("번호,가맹점명,매출금액,수수료,최종정산금액,주문일자,이관일시");
        
        for (ETL_cleanSettlementDTO item : settlements) {
            writer.printf("%s,%s,%d,%d,%d,%s,%s\n",
                item.getSeq(),
                item.getStoreName(),
                item.getSalesAmount(),
                item.getCommissionFee(),
                item.getFinalAmount(),
                item.getOrderDate(),
                item.getTransferDate()
            );
        }
        writer.flush();
        writer.close();
    }

    // Excel 다운로드 엔드포인트 (HTML Table 방식)
    @RequestMapping(value = "/download/excel", method = RequestMethod.GET)
    public void downloadExcel(HttpServletResponse response) throws IOException {
        List<ETL_cleanSettlementDTO> settlements = dataService.getCleanSettlements();
        
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"clean_settlements.xls\"");
        
        PrintWriter writer = response.getWriter();
        // HTML 형식으로 작성하면 엑셀에서 표 형식으로 열립니다.
        writer.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        writer.println("<table border='1'>");
        writer.println("<tr style='background-color:#eeeeee; font-weight:bold;'>");
        writer.println("<td>번호</td><td>가맹점명</td><td>매출금액</td><td>수수료</td><td>최종정산금액</td><td>주문일자</td><td>이관일시</td>");
        writer.println("</tr>");
        
        for (ETL_cleanSettlementDTO item : settlements) {
            writer.println("<tr>");
            writer.printf("<td>%s</td><td>%s</td><td>%d</td><td>%d</td><td>%d</td><td>%s</td><td>%s</td>\n",
                item.getSeq(),
                item.getStoreName(),
                item.getSalesAmount(),
                item.getCommissionFee(),
                item.getFinalAmount(),
                item.getOrderDate(),
                item.getTransferDate()
            );
            writer.println("</tr>");
        }
        writer.println("</table>");
        writer.flush();
        writer.close();
    }

    // 3. AJAX 요청 전용 엔드포인트: 결과를 JSON으로 반환
    @PostMapping("/start-ajax")
    @ResponseBody
    public Map<String, Object> startEtlProcessAjax() {
        return ETLService.triggerFastApiEtl();
    }

    // 4. 모든 데이터를 JSON으로 반환하는 API (Node.js 프론트엔드용)
    @RequestMapping(value = "/api/data", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllData() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("rawOrders", dataService.getRawOrders());
        data.put("cleanSettlements", dataService.getCleanSettlements());
        return data;
    }
}
