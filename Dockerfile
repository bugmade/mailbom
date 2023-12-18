# jdk 이미지 불러오기
FROM openjdk:17-jdk-slim

RUN mkdir -p /home/ubuntu/cso-cms/config
RUN mkdir -p /home/ubuntu/cso-cms/logs
WORKDIR /home/ubuntu/cso-cms

# 애플리케이션 JAR 파일 복사
COPY build/libs/*.war cso-cms.war
COPY build/libs/config ./config

# 애플리케이션 포트 설정
EXPOSE 8090

# COPY된 jar파일 실행하기 (컨테이너 실행할 때 동작)
CMD ["java", "-jar", "cso-cms.war"]
