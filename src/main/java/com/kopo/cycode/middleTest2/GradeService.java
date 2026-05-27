package com.kopo.cycode.middleTest2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [성적 관리 서비스 계층 (Business Logic)]
 * 성적 계산 로직 처리 및 JDBC를 이용한 데이터베이스 연동을 담당합니다.
 */
@Service
public class GradeService {

    private final DataSource dataSource;

    /**
     * 의존성 주입(DI): Spring 설정 파일에서 생성된 dataSource를 주입받습니다.
     */
    @Autowired
    public GradeService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * [신규 학생 성적 입력]
     * 입력된 점수를 바탕으로 총점과 평균을 계산하여 DB에 저장합니다.
     */
    public void insertGrade(GradeDTO grade) throws Exception {
        // 비즈니스 로직: 총점 및 평균 계산
        int total = grade.getKor() + grade.getEng() + grade.getMath();
        double average = Math.round((total / 3.0) * 100) / 100.0; // 소수점 둘째자리 반올림
        
        String sql = "INSERT INTO student_scores (student_name, kor, eng, math, total_score, avg_score, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 파라미터 바인딩 (JDBC)
            pstmt.setString(1, grade.getStudentName());
            pstmt.setInt(2, grade.getKor());
            pstmt.setInt(3, grade.getEng());
            pstmt.setInt(4, grade.getMath());
            pstmt.setInt(5, total);
            pstmt.setDouble(6, average);
            pstmt.executeUpdate(); // 쿼리 실행
            
            // 데이터 입력 후 전체 석차 재계산 실행
            updateAllRanks();
        }
    }

    /**
     * [전체 학생 석차 갱신 로직]
     * 모든 학생의 총점을 비교하여 실시간으로 석차를 다시 계산하여 업데이트합니다.
     */
    public void updateAllRanks() throws Exception {
        // 총점 기준 내림차순 조회
        String selectSql = "SELECT id, total_score FROM student_scores ORDER BY total_score DESC";
        String updateSql = "UPDATE student_scores SET student_rank = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
             ResultSet rs = selectPstmt.executeQuery()) {
            
            int rank = 1;      // 현재 순위
            int prevTotal = -1; // 이전 학생의 총점 (공동 순위 체크용)
            int count = 0;      // 누적 인원수
            
            while (rs.next()) {
                count++;
                int currentTotal = rs.getInt("total_score");
                int id = rs.getInt("id");
                
                // 이전 점수와 다를 경우에만 순위 갱신 (공동 순위 처리)
                if (currentTotal != prevTotal) {
                    rank = count;
                }
                
                // 해당 학생의 순위 정보를 DB에 업데이트
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                    updatePstmt.setInt(1, rank);
                    updatePstmt.setInt(2, id);
                    updatePstmt.executeUpdate();
                }
                prevTotal = currentTotal;
            }
        }
    }
    
    /**
     * [전체 학생 목록 조회]
     * DB에 저장된 모든 학생 정보를 석차순(1위부터)으로 리스트에 담아 반환합니다.
     */
    public List<GradeDTO> getAllGrades() {
        List<GradeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM student_scores ORDER BY student_rank ASC, id DESC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                GradeDTO dto = new GradeDTO();
                dto.setId(rs.getInt("id"));
                dto.setStudentName(rs.getString("student_name"));
                dto.setKor(rs.getInt("kor"));
                dto.setEng(rs.getInt("eng"));
                dto.setMath(rs.getInt("math"));
                dto.setTotalScore(rs.getInt("total_score"));
                dto.setAvgScore(rs.getDouble("avg_score"));
                dto.setStudentRank(rs.getInt("student_rank"));
                dto.setCreatedAt(rs.getString("created_at"));
                list.add(dto); // 조회 결과 객체를 리스트에 추가
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * [학생 정보 수정]
     * 기존 학생의 점수를 수정하고, 그에 따른 총점/평균/석차를 다시 계산합니다.
     */
    public void updateGrade(GradeDTO grade) throws Exception {
        int total = grade.getKor() + grade.getEng() + grade.getMath();
        double average = Math.round((total / 3.0) * 100) / 100.0;

        String sql = "UPDATE student_scores SET student_name = ?, kor = ?, eng = ?, math = ?, total_score = ?, avg_score = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grade.getStudentName());
            pstmt.setInt(2, grade.getKor());
            pstmt.setInt(3, grade.getEng());
            pstmt.setInt(4, grade.getMath());
            pstmt.setInt(5, total);
            pstmt.setDouble(6, average);
            pstmt.setInt(7, grade.getId());
            pstmt.executeUpdate();

            // 수정 후 석차 재조정
            updateAllRanks();
        }
    }

    /**
     * [학생 정보 삭제]
     * 선택된 학생(ID 리스트)의 데이터를 삭제하고 석차를 재계산합니다.
     */
    public void deleteGrades(List<Integer> ids) throws Exception {
        if (ids == null || ids.isEmpty()) return;

        // 선택된 ID들에 대해 IN 연산자를 이용한 일괄 삭제 쿼리 생성
        StringBuilder sql = new StringBuilder("DELETE FROM student_scores WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append("?");
            if (i < ids.size() - 1) sql.append(",");
        }
        sql.append(")");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setInt(i + 1, ids.get(i));
            }
            pstmt.executeUpdate();

            // 삭제 후 석차 재조정
            updateAllRanks();
        }
    }
}
