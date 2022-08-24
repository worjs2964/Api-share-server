FROM openjdk:11 AS builder

# 프로젝트 필요한 파일들 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# log 저장용 파일, 컨테이너가 삭제되도 유지되게 도커 컴포즈에서 볼륨 마운트 처리.
RUN mkdir "application_log"

# Jar 파일 생성
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# 빌드된 jar 파일을 api-server.jar로 복사
FROM openjdk:11
COPY --from=builder build/libs/*.jar api-server.jar

# 컨테이너 포트 노출
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "/api-server.jar"]