package com.kopo.cycode.middleTest2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

	private final DataSource dataSource;

	@Autowired
	public GradeService(DataSource dataSource) {
		this.dataSource = dataSource; // Spring이 자동으로 DB연결 도구 넣음 (DI)
	}

	/* 학생 목록 조회 */
	public List<GradeDTO> getAllGrades() {
		List<GradeDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM student_scores ORDER BY student_rank ASC, id DESC";

		try (Connection conn = dataSource.getConnection(); // 1. 연결확보
				PreparedStatement pstmt = conn.prepareStatement(sql); // 2. SQL준비
				ResultSet rs = pstmt.executeQuery()) { //3. 쿼리 실행 및 결과받기

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
				list.add(dto);
			} // 4. 결과를 DTO에 담기
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list; //5. 결과를 리스트로 반환
	}// 학생 목록 조회

	/* 학생 성적 입력 */
	public void insertGrade(GradeDTO grade) throws Exception {
		int total = grade.getKor() + grade.getEng() + grade.getMath();
		double average = Math.round((total / 3.0) * 100) / 100.0;

		String sql = "INSERT INTO student_scores (student_name, kor, eng, math, total_score, avg_score, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";

		try (Connection conn = dataSource.getConnection(); // 1. 연결확보
				PreparedStatement pstmt = conn.prepareStatement(sql)) { // 2. SQL준비
			pstmt.setString(1, grade.getStudentName());
			pstmt.setInt(2, grade.getKor());
			pstmt.setInt(3, grade.getEng());
			pstmt.setInt(4, grade.getMath());
			pstmt.setInt(5, total);
			pstmt.setDouble(6, average);
			pstmt.executeUpdate(); // 3. 쿼리 실행 및 결과받기

			updateAllRanks();
		}
	}// 학생 성적 입력

	/* 석차 재계산 로직 */
	public void updateAllRanks() throws Exception {
		String selectSql = "SELECT id, total_score FROM student_scores ORDER BY total_score DESC";
		String updateSql = "UPDATE student_scores SET student_rank = ? WHERE id = ?";

		try (Connection conn = dataSource.getConnection();
				PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
				ResultSet rs = selectPstmt.executeQuery()) {

			int rank = 1;
			int prevTotal = -1;
			int count = 0;

			while (rs.next()) {
				count++;
				int currentTotal = rs.getInt("total_score");
				int id = rs.getInt("id");

				if (currentTotal != prevTotal) {
					rank = count;
				}

				try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
					updatePstmt.setInt(1, rank);
					updatePstmt.setInt(2, id);
					updatePstmt.executeUpdate();
				}
				prevTotal = currentTotal;
			}
		}
	}// 석차 재계산 로직

	/* 학생 성적 수정 */
	public void updateGrade(GradeDTO grade) throws Exception {
		int total = grade.getKor() + grade.getEng() + grade.getMath();
		double average = Math.round((total / 3.0) * 100) / 100.0;

		String sql = "UPDATE student_scores SET student_name = ?, kor = ?, eng = ?, math = ?, total_score = ?, avg_score = ? WHERE id = ?";

		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, grade.getStudentName());
			pstmt.setInt(2, grade.getKor());
			pstmt.setInt(3, grade.getEng());
			pstmt.setInt(4, grade.getMath());
			pstmt.setInt(5, total);
			pstmt.setDouble(6, average);
			pstmt.setInt(7, grade.getId());
			pstmt.executeUpdate();

			updateAllRanks();
		}
	}// 학생 성적 수정

	/* 학생 성적 삭제 */
	public void deleteGrades(List<Integer> ids) throws Exception {
		if (ids == null || ids.isEmpty())
			return;

		StringBuilder sql = new StringBuilder("DELETE FROM student_scores WHERE id IN (");
		for (int i = 0; i < ids.size(); i++) {
			sql.append("?");
			if (i < ids.size() - 1)
				sql.append(",");
		}
		sql.append(")");

		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < ids.size(); i++) {
				pstmt.setInt(i + 1, ids.get(i));
			}
			pstmt.executeUpdate();

			updateAllRanks();
		}
	}// 학생 성적 삭제

}// GradeService
