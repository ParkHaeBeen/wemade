## 주요 기능

### 1. 파일 업로드 및 파싱
- 스트링밍 처리
- 파싱오류 허용하면서도 분석 지속

### 2. 외부 API 조회 기능
- 외부 API 의존성을 낮추기 위해 rate limit 고려, timeout 설정, 실패 시 fallback 전략 을 적용할 수 있도록 구조 설계

<Br>

## 주요 신경 부분

### 1. 스트리밍 처리
- MultipartFile을 한 번에 메모리에 올리지 않고 CSVParser + InputStreamReader를 사용해 라인 단위 스트리밍 처리
- 최대 라인 수(MAX_LINES) 제한을 두어 비정상적으로 큰 파일로 인한 메모리 폭주 방지

### 2. 책임분리 중심 구조 설계
하나의 Service 클래스에 로직이 집중되지 않도록 **역할 기반으로 명확히 분리**했습니다.

- **Executor**
  - 파일 스트리밍 파싱
  - 로그 라인 단위 처리
  - 통계 데이터 누적

- **Domain**
  - `Analysis` : 분석 단위의 루트 엔티티
  - `AnalysisStatistics` : 상태 코드 / IP / Path 집계 책임
  - `ParseReport` : 파싱 성공/실패 정보 관리

- **Service**
  - 분석 생성 및 조회 흐름 조립
  - 외부 API(IP 정보) 연동

- **Mapper**
  - 도메인 → 응답 DTO 변환
  - 조회 응답 조립 책임만 담당

- **AOP**
  - 분석 시작 / 종료 / 실패 로깅
  - 실행 시간 측정
  - 비즈니스 로직과 로깅 관심사 분리

### 3. 외부 API 실패 전략
- Retry를 통해 지수백오프기반을 활용하여 조회 재시도
- 최종실패시 Recover를 통해 UNKNOWN으로 반환
 
### 4. 프로젝트 구조
- 프로젝트는 기능 흐름에 따라 **Controller / Service / Domain / Infrastructure**로 책임을 분리한 구조를 사용
- 파일 파싱(parser), 외부 API 연동(client), 저장소(persistence)는 infrastructure 하위로 분리하고, 분석 상태와 집계 로직은 domain에 위치시켜 핵심 비즈니스 로직이 외부 구현에 의존하지 않도록 설계

<img width="235" height="627" alt="image" src="https://github.com/user-attachments/assets/6732e599-4c06-4095-b352-afd2025e8e06" />

<br>
<br>

## 실서비스 보완점

### 1. 분석 처리 비동기화
현재는 동기 방식으로 분석을 수행하지만, 실 서비스에서는 비동기 처리로의 전환이 필요하다고 판단합니다.

- 초기 단계에서는 Spring Event 기반 비동기 처리를 통해 분석 요청과 실행 로직 간 결합도를 낮추고 구조적 확장을 준비
- 분석 요청 증가 또는 처리 시간이 길어질 경우, Kafka / RabbitMQ 등의 메시지 큐를 도입하여 Worker 기반 비동기 분석 처리로 확장
- 분석 상태(PENDING / RUNNING / COMPLETED / FAILED)를 명시적으로 관리하여 처리 흐름과 장애 상황을 추적 가능하도록 설계


### 2. IP 정보 조회 안정성 및 정확성 강화
- ipinfo API 호출 실패율이 일정 기준 이상으로 증가할 경우를 대비해, 다른 Public IP 정보 API로 **우회 조회(fallback)** 할 수 있는 구조로 확장 가능하도록 설계
- 연속 실패 횟수 또는 일정 시간 내 실패 비율을 기준으로 외부 API 상태를 판단하고, 주 API 장애 시 보조 API를 사용하여 분석 결과 품질 저하를 최소화



### 3. 캐시 구조 확장
- 현재는 **단일 서버 환경을 가정**하여 `ConcurrentHashMap` 기반 캐시 사용
- 다중 서버 환경으로 확장 시
  - Redis 기반 분산 캐시 적용
  - TTL 정책을 통한 캐시 갱신 전략 도입


<br>

## 실행 방법

1. Application 실행
2. "http://localhost:8080/swagger-ui/index.html#/" 접속
3. 관련 API 실행
