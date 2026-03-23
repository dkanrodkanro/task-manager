# Task Manager

할 일을 등록, 수정, 삭제, 완료 처리할 수 있는 웹 애플리케이션입니다.  
회원가입과 로그인 기능을 포함하며, 로그인한 사용자만 자신의 할 일을 관리할 수 있습니다.

🔗 **배포 URL**: [https://task-manager-production-af9b.up.railway.app](https://task-manager-production-af9b.up.railway.app)

## 기술 스택

| 영역 | 기술 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.5 |
| 템플릿 엔진 | Thymeleaf |
| CSS | Bootstrap 5 |
| ORM | Spring Data JPA |
| 보안 | Spring Security |
| DB | MySQL 8.0 |
| 빌드 | Gradle |
| 컨테이너 | Docker |
| 배포 | Railway |

## 기능

- **회원가입**: 아이디, 비밀번호 입력으로 계정 생성 (중복 아이디 검증, 비밀번호 확인)
- **로그인 / 로그아웃**: Spring Security 기반 세션 인증
- **할 일 목록**: 로그인한 사용자의 할 일만 조회 (최신순 정렬)
- **할 일 등록**: 제목과 설명을 입력하여 새 할 일 추가
- **할 일 수정**: 기존 할 일의 제목, 설명 변경 (본인 소유 검증)
- **할 일 완료 처리**: 체크박스 클릭으로 완료/미완료 상태 토글
- **할 일 삭제**: 할 일 영구 삭제 (본인 소유 검증)

## 페이지 구성

| 페이지 | URL | 설명 |
|--------|-----|------|
| 메인 | `/` | 사이트 제목 + 로그인 버튼 |
| 로그인 | `/login` | 아이디, 비밀번호 입력 폼 |
| 회원가입 | `/register` | 아이디, 비밀번호, 비밀번호 확인 폼 |
| 할 일 목록 | `/tasks` | 로그인 사용자의 할 일 목록 |
| 할 일 등록 | `/tasks/new` | 제목, 설명 입력 폼 |
| 할 일 수정 | `/tasks/{id}/edit` | 기존 데이터 수정 폼 + 삭제 버튼 |

## 프로젝트 구조

```
src/main/java/com/example/taskmanager/
├── entity/        User.java, Task.java
├── repository/    UserRepository.java, TaskRepository.java
├── service/       UserService.java, TaskService.java, CustomUserDetailsService.java
├── controller/    AuthController.java, TaskController.java
├── dto/           UserRegisterDTO.java, TaskCreateDTO.java, TaskUpdateDTO.java
├── config/        SecurityConfig.java
└── TaskmanagerApplication.java

src/main/resources/
├── templates/     index.html, login.html, register.html, tasks.html, new.html, edit.html
└── application.properties
```

## 로컬 실행 방법

### 사전 준비
- Java 17
- MySQL 8.0
- Docker (선택)

### MySQL로 실행
```bash
# MySQL에서 데이터베이스 생성
mysql -u root -p -e "CREATE DATABASE taskmanager;"

# 프로젝트 빌드 및 실행
./gradlew bootRun
```

`http://localhost:8080`으로 접속

### Docker로 실행
```bash
./gradlew bootJar
docker compose up --build
```

`http://localhost:8080`으로 접속
