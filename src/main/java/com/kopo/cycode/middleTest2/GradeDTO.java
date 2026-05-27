package com.kopo.cycode.middleTest2;

/**
 * [학생 성적 정보 DTO (Data Transfer Object)]
 * 학생의 인적사항과 성적 데이터를 계층 간 전달하기 위한 객체입니다.
 */
public class GradeDTO {
    private int id;                // 데이터 고유 식별자 (PK)
    private String studentName;    // 학생 성함
    private int kor;               // 국어 점수
    private int eng;               // 영어 점수
    private int math;              // 수학 점수
    private int totalScore;        // 총점 (국+영+수)
    private double avgScore;       // 평균 (총점 / 3)
    private int studentRank;       // 석차 (총점 기준)
    private String createdAt;      // 데이터 생성 일시

    // --- Getter 및 Setter 메서드 (데이터 접근 및 수정) ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getKor() { return kor; }
    public void setKor(int kor) { this.kor = kor; }

    public int getEng() { return eng; }
    public void setEng(int eng) { this.eng = eng; }

    public int getMath() { return math; }
    public void setMath(int math) { this.math = math; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public double getAvgScore() { return avgScore; }
    public void setAvgScore(double avgScore) { this.avgScore = avgScore; }

    public int getStudentRank() { return studentRank; }
    public void setStudentRank(int studentRank) { this.studentRank = studentRank; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
