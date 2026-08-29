기본 레포
dependencies: Lombok, Spring web, Spring data JPA, Validation


# Server
신촌톤 신촌쿵야 백엔드 서버입니다.

---

# 신촌쿵야 Backend

대학생 단체 술자리를 위한 **역경매 기반 예약 매칭 플랫폼** 신촌쿵야의 백엔드 서버입니다.

학생이 인원, 예약 일시, 희망 지역과 예산을 담아 리퀘스트를 등록하면, 조건에 맞는 가게가 가격과 혜택을 제안합니다. 학생은 여러 제안을 비교해 하나를 선택하고 예약을 체결할 수 있습니다.

## 서비스 소개

기존 단체 예약은 학생이 여러 가게에 직접 연락해 수용 가능 인원과 가격, 제공 혜택을 반복해서 확인해야 합니다.

신촌쿵야는 이 과정을 리퀘스트와 제안을 중심으로 단순화합니다.

```text
학생이 단체 예약 리퀘스트 등록
        ↓
가게가 조건에 맞는 리퀘스트 확인
        ↓
사장님이 가격·할인·혜택을 담은 제안 등록
        ↓
학생이 여러 제안 비교
        ↓
제안 수락 및 예약 체결
```

### 주요 기능

- 사장님 회원의 가게 등록 및 내 가게 조회
- 가게별 메뉴 일괄 등록
- 가게 정보와 메뉴 상세 조회
- 학생의 단체 예약 리퀘스트 등록
- 학생별 리퀘스트 이력 조회
- 지역과 수용 인원에 맞는 리퀘스트 조회
- 리퀘스트별 가게 제안 등록
- 제안 목록 및 단건 상세 조회
- 제안 수락과 나머지 제안 자동 거절
- 최종 예약 체결 정보 생성
- 공통 API 응답 및 전역 예외 처리
- Swagger 기반 API 문서 제공

> AI 안주 조합 추천 기능은 별도 기능 브랜치에서 개발 중입니다.

## 팀원 소개

| 팀원 | 역할 | 주요 기여 |
|---|---|---|
| 강상준 | Backend | 가게·메뉴 API, 제안·체결 및 제안 상세 조회, Swagger·Security·배포 설정 |
| 김민주 | Backend | 공통 응답·예외 처리, 제안·체결 기능, AI 안주 조합 추천 기능 |
| 이연우 | Backend | 단체 예약 리퀘스트 등록·목록·상세 조회, OpenAPI 명세 구성 |

## 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1, Spring MVC, Spring Security |
| Data Access | Spring Data JPA, Hibernate |
| Database | MySQL, H2 |
| Validation | Jakarta Bean Validation |
| API Documentation | Springdoc OpenAPI, Swagger UI, OpenAPI 3.0 |
| Build | Gradle |
| Test | JUnit 5, Spring Boot Test, AssertJ |
| Productivity | Lombok |

## 프로젝트 구조

```text
src
├── main
│   ├── java
│   │   └── com.example.shinchonton_backend
│   │       ├── domain
│   │       │   ├── member
│   │       │   │   ├── code
│   │       │   │   ├── entity
│   │       │   │   └── repository
│   │       │   ├── store
│   │       │   │   ├── code
│   │       │   │   ├── controller
│   │       │   │   ├── dto
│   │       │   │   ├── entity
│   │       │   │   ├── repository
│   │       │   │   └── service
│   │       │   ├── partyrequest
│   │       │   │   ├── controller
│   │       │   │   ├── dto
│   │       │   │   │   ├── req
│   │       │   │   │   └── res
│   │       │   │   ├── entity
│   │       │   │   ├── repository
│   │       │   │   └── service
│   │       │   ├── offer
│   │       │   │   ├── controller
│   │       │   │   ├── dto
│   │       │   │   │   ├── req
│   │       │   │   │   └── res
│   │       │   │   ├── entity
│   │       │   │   ├── repository
│   │       │   │   └── service
│   │       │   └── deal
│   │       │       ├── entity
│   │       │       └── repository
│   │       ├── global
│   │       │   ├── apiPayload
│   │       │   │   └── code
│   │       │   │       └── status
│   │       │   ├── common
│   │       │   ├── config
│   │       │   └── exception
│   │       └── ShinchontonBackendApplication.java
│   └── resources
│       ├── application.properties
│       ├── application.yml
│       ├── application-local.properties
│       ├── application-h2.yml
│       ├── application-prod.properties
│       └── static
│           └── openapi.yaml
└── test
    ├── java
    │   └── com.example.shinchonton_backend
    │       ├── domain
    │       └── global
    └── resources
        ├── application.properties
        └── application.yml
```

---

# 📐 Convention

### 🌿 Branch Strategy

| Branch              | Description        |
|---------------------|--------------------|
| `main`              | 배포 브랜치             |
| `develop`           | 개발 브랜치             |
| `feat/#이슈번호-설명`     | 기능 개발              |
| `fix/#이슈번호-설명`      | 버그 수정              |
| `refactor/#이슈번호-설명` | 리팩토링               |
| `chore/#이슈번호-설명`    | 설정 및 기타 작업         |
| `docs/#이슈번호-설명`     | README, 문서 및 주석 수정 |
| `hotfix/#이슈번호-설명`   | 긴급 수정              |

---

### 💬 Commit Convention

| Type       | Description |
|------------|-------------|
| `feat`     | 새로운 기능 추가   |
| `fix`      | 버그 수정       |
| `refactor` | 리팩토링        |
| `docs`     | 문서 수정       |
| `chore`    | 설정 및 기타 작업  |
| `init`     | 프로젝트 초기 설정  |

---

### 🔀 Pull Request

- Base Branch : `develop`
- Reviewer 1명 이상 지정
- AI Code Review 확인 및 반영
- 팀원 1명 이상의 Approve 후 Merge
- PR 제목은 `[Type] 구현 내용` 형식을 사용

---
