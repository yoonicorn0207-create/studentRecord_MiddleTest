package com.kopo.cycode.middletest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ETLFastApiClientService {

    public Map<String, Object> triggerFastApiEtl() {
        // 1. 실제 연동할 파이썬 FastAPI의 이관 엔드포인트 주소
        String url = "http://127.0.0.1:8000/api/v1/etl/trigger";
        
        // 2. 외부 HTTP API를 호출하기 위한 스프링 내장 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        
        // 3. 컨트롤러로 돌려줄 결과를 담을 HashMap 바구니 생성
        Map<String, Object> resultMap = new HashMap<>();
        
        try {
            // 4. FastAPI 서버로 POST 요청을 날립니다. (보낼 Body 데이터가 없으므로 null 전달)
            // 파이썬이 응답하는 JSON 데이터를 자바의 Map(HashMap) 구조로 자동으로 변환해서 받아옵니다.
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
            
            if (response != null) {
                // 파이썬 FastAPI가 돌려준 status와 message를 그대로 HashMap에 매핑합니다.
                resultMap.put("status", response.get("status"));
                resultMap.put("message", response.get("message"));
            } else {
                resultMap.put("status", "FAIL");
                resultMap.put("message", "FastAPI 서버로부터 응답 데이터(Body)가 오지 않았습니다.");
            }
            
        } catch (Exception e) {
            // 5. 파이썬 서버가 꺼져있거나, 주소가 틀렸거나, 네트워크 연결에 실패했을 때의 예외 처리
            resultMap.put("status", "FAIL");
            resultMap.put("message", "FastAPI 서버 연결 실패 에러: " + e.getMessage());
        }
        
        // 컨트롤러에게 최종 가공된 HashMap을 반환합니다.
        return resultMap;
    }
}