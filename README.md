# 바로인턴 13기 Java 백엔드 개발 직무 과제

<br>

## 프로젝트 개요
- Spring Boot 기반 In-Memory 인증·인가 API 프로젝트
- 3계층 아키텍처(Controller → Service → Repository)
- JWT 기반 보안, 역할(Role) 기반 접근 제어를 적용하여 **회원가입**, **로그인**, **관리자 권한 부여** 기능 제공
- 외부 데이터베이스나 파일 시스템 없이 모든 데이터를 인메모리에 저장하며, 빠르고 가볍게 동작하도록 설계


<br><br>


## 프로젝트 목표

- **사용자 인증 시스템**을 구축합니다. (회원가입, 로그인)
- **JWT(Json Web Token) 기반 인증 메커니즘**을 구현하여 보안성을 강화합니다.
- **역할(Role) 기반 접근 제어**를 적용하여 관리자(Admin) 권한이 필요한 API를 보호합니다.


<br><br>

## 프로젝트 기간

25.06.09 ~ 25.06.11


<br><br>

## 기술 스택

| 구분               | 기술                                                                                                                                                         |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 언어 및 프레임워크 | ![Java 17](https://img.shields.io/badge/Java-17-blue) ![Spring Boot 3.5.0](https://img.shields.io/badge/Spring_Boot-3.5.0-brightgreen)                         |
| 인증 및 보안      | ![Spring Security 6.5.0](https://img.shields.io/badge/Spring_Security-6.5.0-red) ![JWT 0.11.5](https://img.shields.io/badge/JWT-0.11.5-orange)                     |
| 문서화            | ![Swagger UI 2.7.0](https://img.shields.io/badge/Swagger_UI-2.7.0-blueviolet)                                                                                    |
| 테스트            | ![JUnit 5](https://img.shields.io/badge/JUnit-5-red)                                      |

<br><br>


## 프로젝트 구조

```
com.sparta.barointern
├─ BaroInternApplication.java 
│
├─ config                          // 공통 설정
│  ├─ DataInitializer              // 기본 관리자 계정 생성
│  ├─ SecurityConfig.java          // Spring Security & JWT 필터 등록
│  └─ SwaggerConfig.java           // Swagger 설정
│
├─ controller                      // Presentation 계층
│  ├─ UserController.java          // POST /signup, POST /login
│  └─ AdminController.java         // PATCH /admin/users/{userId}/roles
│
├─ service                         // Business 로직
│  ├─ UserService.java             // 회원가입·로그인 처리
│  └─ AdminService.java            // 관리자 권한 부여 처리
│
├─ repository                      // Data 접근 계층
│  ├─ UserRepository.java          // 인터페이스 정의
│  └─ InMemoryUserRepository.java  // 메모리 기반 구현체
│
├─ security                        // JWT 및 인증 필터
│  ├─ JwtProvider.java             // 토큰 생성·검증
│  └─ JwtAuthenticationFilter.java // Authorization 헤더 파싱·검증
│
├─ model                           // 도메인 모델
│  ├─ User.java                    // 엔티티
│  └─ Role.java                    // enum { USER, ADMIN }
│
├─ handler                         // handler
│  └─ GlobalExceptionHandler.java  // @RestControllerAdvice 전역 처리
│
├─ dto                             // API 요청/응답 객체
│  ├─ SignupRequest.java           // username, password, nickname
│  ├─ SignupResponse.java          // username, nickname, roles
│  ├─ LoginRequest.java            // username, password
│  └─ LoginResponse.java           // token
│
└─ exception                       // 예외 처리
   ├─ CustomException.java         // 비즈니스 예외 정의
   └─ ExceptionCode.java           // 예외 코드, 메시지

```

<br><br>

## 실행 방법

1. 소스 코드 클론 & 빌드
    
    ```bash
    git clone <GitHub Public Repository 링크>
    cd barointern
    ./gradlew clean bootJar
    ```
    

1. EC2 서버에 JAR 파일 업로드
    
    ```bash
    scp -i /c/Project/pem/barointern.pem \
      build/libs/barointern-0.0.1-SNAPSHOT.jar \
      ubuntu@3.34.134.208:/home/ubuntu/
    ```
    
2. SSH 접속 & 실행
    
    ```bash
    ssh -i /c/Project/pem/barointern.pem ubuntu@3.34.134.208
    
    # (최초 1회) JDK 설치
    sudo apt-get update
    sudo apt-get install -y openjdk-17-jdk
    
    cd /home/ubuntu
    nohup java -jar barointern-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
    ```
    
3. 확인
    
    ```bash
    tail -n 20 app.log   # "Started BarointernApplication" 메시지 확인
    ```
    

<br><br>

## API 명세서
![image](https://github.com/user-attachments/assets/e39ee4eb-5016-4464-80b7-6067941ebaaa)


- Swagger UI: http://3.34.134.208:8080/swagger-ui/index.html
- AWS EC2 URL: [http://3.34.134.208:8080](http://3.34.134.208:8080/login)
    
<br><br>

### 1. 회원가입 — `POST /signup`

- **Request**
    
    ```json
    {
      "username": "JIN HO",
      "password": "12341234",
      "nickname": "Mentos"
    }
    
    ```
    
- **Response (200 OK)**
    
    ```json
    {
      "username": "JIN HO",
      "nickname": "Mentos",
      "roles": [
        { "role": "USER" }
      ]
    }
    
    ```
    
- **Response (400 Bad Request)**
    
    ```json
    {
      "error": {
        "code": "USER_ALREADY_EXISTS",
        "message": "이미 가입된 사용자입니다."
      }
    }
    
    ```
    
<br>

### 2. 로그인 — `POST /login`

- **Request**
    
    ```json
    {
      "username": "JIN HO",
      "password": "12341234"
    }
    
    ```
    
- **Response (200 OK)**
    
    ```json
    {
      "token": "Bearer eyJhbGciOiJIUzI1NiJ9…"
    }
    
    ```
    
- **Response (401 Unauthorized)**
    
    ```json
    {
      "error": {
        "code": "INVALID_CREDENTIALS",
        "message": "아이디 또는 비밀번호가 올바르지 않습니다."
      }
    }
    
    ```
    
<br>

### 3. 관리자 권한 부여 — `PATCH /admin/users/{userId}/roles`

- **Headers**:
    
    `Authorization: Bearer <JWT 토큰>`
    
- **Response (200 OK)**
    
    ```json
    {
      "username": "JIN HO",
      "nickname": "Mentos",
      "roles": [
        { "role": "ADMIN" }
      ]
    }
    
    ```
    
- **Response (403 Forbidden)**
    
    ```json
    {
      "error": {
        "code": "ACCESS_DENIED",
        "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
      }
    }
    
    ```
    
- **Response (404 Not Found)**
    
    ```json
    {
      "error": {
        "code": "USER_NOT_FOUND",
        "message": "해당 사용자를 찾을 수 없습니다."
      }
    }
    
    ```
    
<br><br>

## 테스트 방법

```bash
./gradlew test
```

- UserServiceTest: 회원가입 성공/중복 예외, 로그인 성공/자격 오류
- AdminServiceTest: 권한 부여 서비스 로직 성공, 존재하지 않는 사용자 권한 부여
- AdminControllerTest: 권한 없는 사용자 요청
- 모든 테스트가 Green인지 확인
