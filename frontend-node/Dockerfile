# 1단계: Node.js 이미지를 베이스로 사용
FROM node:18-slim

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 파일 복사 및 설치
COPY package*.json ./
RUN npm install --production

# 소스 코드 복사
COPY . .

# 노드 서버 포트 3000 개방
EXPOSE 3000

# 서버 실행
CMD ["node", "server.js"]
