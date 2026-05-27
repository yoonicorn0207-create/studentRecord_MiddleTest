# 1단계: Tomcat 이미지를 베이스로 사용
FROM tomcat:9.0-jdk11-openjdk-slim

# 기본적으로 포함된 톰캣 기본 앱들 삭제 (보안 및 용량 절감)
RUN rm -rf /usr/local/tomcat/webapps/*

# 빌드된 WAR 파일을 톰캣의 ROOT.war로 복사
# (Maven 빌드 결과물이 target/cycode.war 인 것을 확인했습니다)
COPY target/cycode.war /usr/local/tomcat/webapps/ROOT.war

# 톰캣 포트 8080 개방
EXPOSE 8080

# 톰캣 실행
CMD ["catalina.sh", "run"]
