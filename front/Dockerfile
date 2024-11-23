# Apache HTTP 서버 이미지 사용
FROM httpd:2.4

# 아파치 HTTP 서버의 기본 DocumentRoot 경로
# 정적 파일을 컨테이너 내부로 복사
COPY . /usr/local/apache2/htdocs/

# 기본적으로 httpd는 80번 포트에서 서비스
EXPOSE 80
