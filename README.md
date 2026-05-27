# 🎓 학생 성적 관리 시스템 (Student Grade Management System)

Spring MVC 아키텍처를 기반으로 설계된 **학생 성적 관리 프로그램**입니다. Docker 가상화 환경을 통해 데이터베이스부터 프론트엔드까지 명령어 한 줄로 즉시 구동할 수 있도록 구축되었습니다.

---

## 🚀 주요 기능 (Key Features)
- **성적 관리(CRUD)**: 학생의 이름과 국어, 영어, 수학 점수를 입력, 조회, 수정, 삭제할 수 있습니다.
- **자동 계산 로직**: 총점, 평균을 실시간으로 계산하며, 총점을 기준으로 **전체 석차(Rank)**를 자동으로 산출합니다.
- **3-Tier 아키텍처**: Node.js(UI), Spring MVC(API), MySQL(DB)로 분리된 현대적인 웹 구조를 가집니다.
- **인프라 자동화**: Docker Compose를 통해 복잡한 서버 설정을 코드화(IaC)하여 배포 편의성을 극대화했습니다.

---

## 🛠 기술 스택 (Tech Stack)
### Backend
- **Language**: Java 11 (OpenJDK)
- **Framework**: Spring MVC 4.3
- **Data Access**: JDBC (Plain JDBC)
- **Build Tool**: Maven

### Frontend
- **Environment**: Node.js
- **UI**: HTML5, CSS3, JavaScript (AJAX)

### Infrastructure
- **Database**: MySQL 8.0
- **Virtualization**: Docker, Docker Compose

---

## 🏗 시스템 아키텍처 (Architecture)
1. **Frontend**: 사용자의 입력을 받아 AJAX를 통해 Backend API와 통신합니다. (Port 3000)
2. **Backend**: 비즈니스 로직(성적 계산, 석차 산출)을 처리하고 DB와 연동합니다. (Port 8080)
3. **Database**: 학생 성적 데이터를 영구 저장하며, 볼륨 설정을 통해 컨테이너 재생성 시에도 데이터를 유지합니다. (Port 3306)

---

## 🚦 시작하기 (Getting Started)

### 1. 전제 조건
- Docker & Docker Compose 설치
- Java 11 & Maven 설치

### 2. 빌드 및 실행
```bash
# 1. 백엔드 프로젝트 빌드 (WAR 파일 생성)
mvn clean package -DskipTests

# 2. Docker 컨테이너 실행
docker-compose up -d
```

### 3. 접속 정보
- **Web UI**: [http://localhost:3000](http://localhost:3000)
- **Backend API**: [http://localhost:8080](http://localhost:8080)
- **DB (MySQL)**: Localhost:3307 (User: kopouser / Pass: kopouser)

---

## 📂 프로젝트 구조 (Selective)
- `src/main/java/.../middleTest2/`: 핵심 비즈니스 로직 및 컨트롤러
- `src/main/webapp/record_card.html`: 프론트엔드 진입점
- `docker/mysql/init.sql`: 데이터베이스 초기화 스크립트
- `docker-compose.yml`: 전체 인프라 정의서
