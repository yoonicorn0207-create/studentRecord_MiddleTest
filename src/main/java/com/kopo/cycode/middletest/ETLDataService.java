package com.kopo.cycode.middletest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ETLDataService {

    @Autowired
    private DataSource dataSource;

    // 새로운 클라우드 DB 접속 정보
    private final String cloudUrl = "jdbc:mysql://my8003.gabiadb.com:3306/hkcodedb?serverTimezone=UTC&useSSL=false";
    private final String cloudUser = "hkcode";
    private final String cloudPass = "fintech1308!";

    public List<ETL_RaworderDTO> getRawOrders() {
        List<ETL_RaworderDTO> orders = new ArrayList<>();
        String sql = "SELECT * FROM yoon_raw_orders LIMIT 100";
        
        System.out.println("DEBUG: [ETLDataService] 데이터 조회 시작...");
        
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("DEBUG: [ETLDataService] DB 연결 성공! (URL: " + conn.getMetaData().getURL() + ")");
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    ETL_RaworderDTO order = new ETL_RaworderDTO();
                    try {
                        order.setOrderId(rs.getString(1));
                        order.setProductId(rs.getString(2));
                        
                        String qtyStr = rs.getString(3);
                        if (qtyStr != null) {
                            qtyStr = qtyStr.replace(",", "");
                            try {
                                order.setQty((int) Double.parseDouble(qtyStr));
                            } catch (Exception e) {
                                order.setQty(0);
                            }
                        }
                        
                        order.setStatus(rs.getString(4));
                        order.setCreatedAt(rs.getString(5));
                        
                        orders.add(order);
                    } catch (Exception e) {
                        System.err.println("DEBUG: [ETLDataService] 행 매핑 오류: " + e.getMessage());
                    }
                }
                System.out.println("DEBUG: [ETLDataService] 조회된 총 데이터 건수: " + count);
            }
        } catch (Exception e) {
            System.err.println("DEBUG: [ETLDataService] DB 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    public List<ETL_cleanSettlementDTO> getCleanSettlements() {
        List<ETL_cleanSettlementDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM yoon_clean_settlements LIMIT 100";
        
        // 1. 클라우드 DB 시도 (보안 옵션 추가)
        String cloudUrlWithOpt = cloudUrl + (cloudUrl.contains("?") ? "&" : "?") + "allowPublicKeyRetrieval=true";
        System.out.println("DEBUG: [ETLDataService] 1차 시도 (클라우드 DB): " + cloudUrlWithOpt);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(cloudUrlWithOpt, cloudUser, cloudPass);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                list = mapResultSetToCleanSettlement(rs);
                System.out.println("DEBUG: [ETLDataService] 클라우드 DB 조회 결과: " + list.size() + "건");
            }
        } catch (Exception e) {
            System.err.println("DEBUG: [ETLDataService] 클라우드 DB 접속 실패: " + e.getMessage());
        }

        // 2. 만약 클라우드 DB가 비어있다면, 로컬 DB(dataSource)에서도 확인해봅니다.
        if (list.isEmpty()) {
            System.out.println("DEBUG: [ETLDataService] 2차 시도 (로컬 DB 폴백 조회)...");
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                list = mapResultSetToCleanSettlement(rs);
                System.out.println("DEBUG: [ETLDataService] 로컬 DB 조회 결과: " + list.size() + "건");
            } catch (Exception e) {
                System.err.println("DEBUG: [ETLDataService] 로컬 DB 폴백 조회 실패 (테이블 없을 수 있음): " + e.getMessage());
            }
        }
        
        return list;
    }

    // ResultSet을 ETL_cleanSettlementDTO 리스트로 변환하는 공통 헬퍼 메서드
    private List<ETL_cleanSettlementDTO> mapResultSetToCleanSettlement(ResultSet rs) throws Exception {
        List<ETL_cleanSettlementDTO> list = new ArrayList<>();
        int columnCount = rs.getMetaData().getColumnCount();
        
        while (rs.next()) {
            ETL_cleanSettlementDTO item = new ETL_cleanSettlementDTO();
            try {
                item.setSeq(columnCount >= 1 ? rs.getString(1) : "");
                item.setStoreName(columnCount >= 2 ? rs.getString(2) : "");
                
                // 매출금액
                if (columnCount >= 3) {
                    String val = rs.getString(3);
                    if (val != null) {
                        val = val.replace(",", "");
                        try { item.setSalesAmount((int)Double.parseDouble(val)); } catch(Exception e) { item.setSalesAmount(0); }
                    }
                }
                // 중개수수료
                if (columnCount >= 4) {
                    String val = rs.getString(4);
                    if (val != null) {
                        val = val.replace(",", "");
                        try { item.setCommissionFee((int)Double.parseDouble(val)); } catch(Exception e) { item.setCommissionFee(0); }
                    }
                }
                // 정산금액
                if (columnCount >= 5) {
                    String val = rs.getString(5);
                    if (val != null) {
                        val = val.replace(",", "");
                        try { item.setFinalAmount((int)Double.parseDouble(val)); } catch(Exception e) { item.setFinalAmount(0); }
                    }
                }
                
                item.setOrderDate(columnCount >= 6 ? rs.getString(6) : "");
                item.setTransferDate(columnCount >= 7 ? rs.getString(7) : "");
                
                list.add(item);
            } catch (Exception e) {
                // 특정 행 매핑 실패 시 건너뛰고 계속 진행
            }
        }
        return list;
    }
}
