from PIL import Image, ImageDraw, ImageFont
import os

# 바탕화면 경로 설정
desktop = os.path.join(os.environ['USERPROFILE'], 'Desktop')
output_path = os.path.join(desktop, 'Docker_Architecture.png')

# 이미지 크기 및 배경 설정
width, height = 800, 600
img = Image.new('RGB', (width, height), color='white')
draw = ImageDraw.Draw(img)

# 폰트 설정 (기본 폰트 사용)
font = ImageFont.load_default()

# 제목
draw.text((250, 20), "Docker Compose Architecture Diagram", fill='black')

# 1. User (Circle/Ellipse)
draw.ellipse([50, 200, 150, 300], outline='blue', width=2)
draw.text((75, 240), "User", fill='blue')

# User -> Frontend Arrow
draw.line([150, 250, 250, 250], fill='black', width=2)
draw.text((160, 230), "Port 3000", fill='black')

# 2. Frontend (Box)
draw.rectangle([250, 150, 450, 350], outline='magenta', width=2)
draw.text((280, 240), "Frontend (Node.js)", fill='magenta')

# Frontend -> Backend Arrow
draw.line([450, 250, 550, 250], fill='black', width=2)
draw.text((460, 230), "API: 8080", fill='black')

# 3. Backend (Box)
draw.rectangle([550, 150, 750, 350], outline='orange', width=2)
draw.text((580, 240), "Backend (Spring)", fill='orange')

# Backend -> DB Arrow
draw.line([650, 350, 650, 450], fill='black', width=2)
draw.text((660, 390), "JDBC: 3306", fill='black')

# 4. DB (Box)
draw.rectangle([550, 450, 750, 550], outline='green', width=2)
draw.text((590, 490), "DB (MySQL 8.0)", fill='green')

# Volume (Small Box)
draw.rectangle([400, 470, 500, 530], outline='purple', width=2)
draw.text((415, 495), "db_data Vol", fill='purple')
draw.line([500, 500, 550, 500], fill='purple', width=1)

# 이미지 저장
img.save(output_path)
print(f"Success! Image saved to: {output_path}")
