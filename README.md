# 🐰 BAZZI 🐰 
조회수 및 뱃지 관리 API 프로젝트
## 📌 프로젝트 개요
사용자 계정을 기반으로 GitHub 리포지토리의 조회수를 관리하고, 조회수를 시각적으로 표현하는 SVG 뱃지를 생성하는 REST API입니다. Spring Boot와 Redis를 기반으로 구현되었으며, 클린 아키텍처와 마이크로서비스 아키텍처(MSA) 원칙을 준수하여 도메인 간 권한 침범을 최소화하고 유지보수성을 높였습니다.

### 주요 기능

#### 1. 조회수 관리  
- 특정 사용자 계정과 리포지토리 URL에 대한 오늘의 조회수 및 전체 조회수를 Redis에 저장하고 관리.  
- 사용자가 본인의 리포지토리에 접근할 때, 해당 URL에 대해 오늘/전체 조회수가 자동으로 +1 증가됨.  
- 조회수를 확인하거나 초기화할 수 있는 API 기능을 제공함.

#### 2. 뱃지 관리  
- 사용자 계정 및 리포지토리 정보를 기반으로, 지정된 스타일 정보(색상, 라벨, 글꼴 크기 등)를 적용하여 SVG 형식의 뱃지를 생성.  
- 생성된 뱃지는 Markdown 및 HTML에서 사용 가능하며, 해당 리포지토리의 실시간 조회수를 포함함.  
- 뱃지는 미리보기와 실사용 버전이 있으며, Query String을 통해 개별 사용자 기준으로 스타일 커스터마이징 가능.

## 🏛️ 아키텍처 설계
### 설계 원칙
- 클린 아키텍처: 계층 간 의존성을 명확히 분리하여 비즈니스 로직과 외부 시스템(Redis, HTTP)을 격리.
- MSA 원칙: 도메인 간 권한 침범을 최소화하기 위해 BadgeService가 ViewCountService에 직접 의존하지 않고 인터페이스를 통해 상호작용.
- 모듈화: SVG 생성 로직은 별도의 SvgGenerator 클래스로 분리하여 재사용성과 테스트 용이성 확보.

### 디렉토리 구조
```
project-root/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bazzi/
│   │   │           ├── application/
│   │   │           │   ├── dto/          // DTO 클래스
│   │   │           │   └── service/      // 서비스 인터페이스 및 구현
│   │   │           ├── infrastructure/   // 인프라 계층 (Redis 리포지토리 등)
│   │   │           │   └── persistence/  // Redis 리포지토리 구현
│   │   │           ├── interfaces/       // 컨트롤러 및 API 관련
│   │   │           │   ├── controller/   // 컨트롤러 클래스
│   │   │           │   └── config/       // Swagger, Redis 설정 등
│   │   │           └── util/             // 유틸리티 클래스 (SVG 생성기 등)
│   │   │
│   │   └── resources/
│   │       ├── application.yml           // 애플리케이션 설정
│   │       └── static/                   // 정적 리소스 (필요 시)
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── bazzi/
│       │           ├── service/          // 서비스 테스트
│       │           └── controller/       // 컨트롤러 테스트
│       └── resources/
│           └── application-test.yml      // 테스트용 설정
│
└── pom.xml / build.gradle                // 빌드 파일
```

### 기술 스택

- 언어: Java 17
- 프레임워크: Spring Boot 3.5.0
- 데이터베이스: Redis
- API 문서: Swagger (OpenAPI 3.0)
- 테스트: JUnit 5, Mockito
- 빌드 도구: Gradle

## 📋 API 명세
### 1. 조회수 관리 API

| 메서드    | 엔드포인트              | 설명                     | 요청 파라미터                        | 응답값                                |
| ------ | ------------------ | ---------------------- | ------------------------------ | ---------------------------------- |
| `GET`  | `/api/viewcount/{username}` | 조회수 조회(오늘/전체)   | `username`(Path)  | `{ today: number, total: number }` |
| `POST` | `/api/viewcount/{username}/increment` | 조회수 증가 (오늘/전체)       | `username`(Path)  | `{ today: number, total: number }` |
| `POST`  | `/api/viewcount/{username}/reset` | 조회수 초기화 (오늘/전체 0으로 설정) | `username`(Path) | `{ success: boolean }`             |

### 2. 뱃지 관리 API

| 메서드   | 엔드포인트                | 설명                          | 요청 파라미터 (쿼리 스트링)                              | 응답값                       |
| ----- | -------------------- | --------------------------- | --------------------------------------------- | ------------------------- |
| `GET` | `/api/badge`         | 조회수 포함 실시간 뱃지 생성 및 조회수 1 증가 | `url`, `label`, `color`, `icon`, `fontSize` 등 | `SVG 문자열 (image/svg+xml)` |
| `GET` | `/api/badge/preview` | 조회수 증가 없이 뱃지 미리보기           | `url`, `label`, `color`, `icon`, `fontSize` 등 | `SVG 문자열 (image/svg+xml)` |

## 🚀 설치 및 실행
### 사전 요구사항

- Java 17 이상
- Redis 서버 (로컬 또는 원격)
- Gradle

### 설치 방법

#### 1. 리포지토리 클론
```bash
git clone https://github.com/your-repo/viewcount-badge-api.git
cd viewcount-badge-api
```

#### 2. Redis 설정
- Redis 서버를 실행합니다.
- src/main/resources/application.yml에 Redis 연결 정보를 설정합니다.
```yaml
spring:
    redis:
        host: localhost
        port: 6379
```

#### 3. 빌드 및 실행
```bash
mvn clean install
mvn spring-boot:run
```

#### 4. Swagger UI 접속
- 애플리케이션 실행 후 http://localhost:8080/swagger-ui.html 에서 API 문서를 확인하세요.


## 🧪 테스트
### 단위 테스트

- JUnit 5와 Mockito를 사용해 서비스 및 컨트롤러 단위 테스트를 구현했습니다.
- 테스트 실행 명령
    ```bash
    mvn test
    ```

## 🛠️ 추가 개선 가능성

- 캐싱 최적화: Redis에 TTL(Time-To-Live)을 추가하여 오래된 데이터 자동 삭제.
- 뱃지 스타일 커스터마이징 확장: 사용자 요청에 따라 SVG 뱃지 스타일을 더 세분화하여 커스터마이징 가능.
- 모니터링: Prometheus와 Grafana를 연동하여 API 성능 모니터링.

## 🤝🏻 기여 방법
1. 이슈 생성 후 기능/버그 논의.
2. 브랜치 생성 (feature/기능명 또는 bugfix/버그명).
3. 코드 작성 후 단위 테스트 추가.
4. Pull Request 생성 및 코드 리뷰 요청.

## 📄 라이선스
MIT License

이 README.md는 프로젝트의 설계와 구현 세부사항을 명확히 전달하며, 뱃지 미리보기 생성 기능을 포함한 요구사항을 모두 반영했습니다. 추가 수정이 필요하면 알려주세요!

