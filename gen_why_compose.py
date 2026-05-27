from PIL import Image, ImageDraw, ImageFont
import os

# 바탕화면 경로 설정
desktop = os.path.join(os.environ['USERPROFILE'], 'Desktop')
output_path = os.path.join(desktop, 'Why_Docker_Compose.png')

# 이미지 크기 및 배경 설정 (가로로 긴 형태)
width, height = 1000, 600
img = Image.new('RGB', (width, height), color='#f0f4f8')
draw = ImageDraw.Draw(img)

# 기본 폰트 설정
try:
    font = ImageFont.truetype("arial.ttf", 18)
    title_font = ImageFont.truetype("arial.ttf", 35)
    header_font = ImageFont.truetype("arial.ttf", 24)
except:
    font = ImageFont.load_default()
    title_font = ImageFont.load_default()
    header_font = ImageFont.load_default()

# 1. 메인 타이틀
draw.text((300, 30), "Why use Docker Compose?", fill='#2c3e50', font=title_font)

# 섹션 정의 (4개 영역)
sections = [
    {
        "title": "1. Multi-Container Orchestration",
        "desc": "Manage DB, Backend, and Frontend\nwith just one command: 'up'.",
        "color": "#e74c3c",
        "pos": (50, 120)
    },
    {
        "title": "2. Simplified Networking",
        "desc": "Containers communicate via service names\n(e.g., 'db') instead of complex IPs.",
        "color": "#3498db",
        "pos": (520, 120)
    },
    {
        "title": "3. Environment Consistency",
        "desc": "Ensures 'It works on my machine'\nworks everywhere else too.",
        "color": "#27ae60",
        "pos": (50, 350)
    },
    {
        "title": "4. Centralized Configuration",
        "desc": "All ports, volumes, and environments\ndefined in a single YAML file.",
        "color": "#f39c12",
        "pos": (520, 350)
    }
]

for s in sections:
    x, y = s['pos']
    # 박스 그리기
    draw.rectangle([x, y, x+430, y+180], fill='white', outline=s['color'], width=3)
    # 제목
    draw.text((x+20, y+20), s['title'], fill=s['color'], font=header_font)
    # 설명
    draw.text((x+20, y+70), s['desc'], fill='#34495e', font=font)

# 중앙 연결 아이콘 (데코레이션)
draw.ellipse([460, 260, 540, 340], fill='#2c3e50')
draw.text((475, 285), "DOCKER", fill='white')

# 이미지 저장
img.save(output_path)
print(f"Success! Image saved to: {output_path}")
