## 주요 기능

### 1. 파일 업로드 및 파싱
대용량 로그 파일(CSV)을 업로드하여 라인 단위로 분석합니다.

- MultipartFile 전체를 메모리에 적재하지 않고 **스트리밍 방식으로 처리**
- 일부 라인에서 파싱 오류가 발생하더라도 전체 분석이 중단되지 않도록 설계

### 2. 외부 API 조회 기능
분석 과정에서 IP 정보 조회를 위해 외부 API를 사용합니다.

- 외부 API 장애가 전체 분석 실패로 전파되지 않도록
  rate limit, timeout, retry, fallback 전략을 고려한 구조로 설계


<br>

## 주요 설계 포인트

### 1. 스트리밍 처리
대용량 파일 업로드 시 메모리 사용량 증가로 인한
OOM 위험을 최소화하는 것을 가장 우선적으로 고려했습니다.

- `MultipartFile`을 한 번에 메모리에 올리지 않고  
  `CSVParser + InputStreamReader` 기반 **라인 단위 스트리밍 처리**
- 비정상적으로 큰 파일로 인한 리소스 고갈을 방지하기 위해  
  **최대 라인 수(MAX_LINES) 제한** 적용


### 2. 책임 분리 중심 구조 설계
분석 로직이 하나의 Service 클래스에 집중되는 구조를 피하고,
역할과 책임이 명확히 드러나는 구조를 목표로 설계했습니다.

#### Executor
- 파일 스트리밍 파싱
- 로그 라인 단위 처리
- 통계 데이터 누적 수행

#### FileValidator
파일 처리 이전 단계에서 입력값 검증 책임을 분리했습니다.
- 파일 empty, 파일 사이즈 검증 책임

#### IpInfoClient
외부 IP 정보 API 호출을 전담하는 Client 계층으로, 외부 API 의존성을 Service로부터 분리


#### Domain
핵심 비즈니스 로직을 담당하며, 외부 구현에 의존하지 않도록 설계했습니다.

- `Analysis`  
  : 분석 단위의 루트 엔티티
- `AnalysisStatistics`  
  : 상태 코드 / IP / Path 단위 집계 책임
- `ParseReport`  
  : 파싱 성공 / 실패 정보 관리

#### Service
- 분석 생성 및 조회 흐름 조립
- 외부 API의 재시도, fallback 등의 세부 구현은 Client 계층에 위임


#### Mapper
- 도메인 → 응답 DTO 변환
- 조회 응답 조립 책임만 담당하여 비즈니스 로직과 분리

#### AOP
- 분석 시작 / 종료 / 실패 로깅
- 실행 시간 측정
- 로깅 관심사를 비즈니스 로직과 분리


### 3. 외부 API 실패 대응 전략
외부 API 장애가 분석 전체의 실패로 이어지지 않도록
복구 가능한 흐름을 우선적으로 고려했습니다.

- Retry를 통해 **지수 백오프 기반 재시도**
- 최종 실패 시 `Recover`를 통해 `UNKNOWN` 값으로 처리하여
  분석 흐름 유지


### 4. 프로젝트 구조
프로젝트는 기능 흐름에 따라 책임을 명확히 분리한 구조를 사용했습니다.

- `Controller / Service / Domain / Infrastructure` 구조
- 파일 파싱(parser), 외부 API 연동(client), 저장소(persistence)는  
  `infrastructure` 하위로 분리
- 분석 상태 및 집계 로직은 `domain`에 위치시켜  
  **핵심 비즈니스 로직이 외부 구현에 의존하지 않도록 설계**

<img width="235" height="627" alt="image" src="https://github.com/user-attachments/assets/6732e599-4c06-4095-b352-afd2025e8e06" />


<br>
<br>

## 실서비스 관점에서의 보완점

### 1. 분석 처리 비동기화
현재는 동기 방식으로 분석을 수행하지만,
실 서비스 환경에서는 비동기 처리로의 전환이 필요하다고 판단했습니다.

- 초기 단계  
  → Spring Event 기반 비동기 처리로 요청과 실행 로직 간 결합도 감소
- 트래픽 증가 또는 분석 시간 증가 시  
  → Kafka / RabbitMQ 기반 Worker 구조로 확장
- 분석 상태를 `PENDING / RUNNING / COMPLETED / FAILED`로 관리하여  
  처리 흐름 및 장애 상황을 추적 가능하도록 설계


### 2. IP 정보 조회 안정성 및 정확성 강화
- ipinfo API 실패율이 일정 기준 이상 증가할 경우를 대비해  
  **다른 Public IP API로 우회 조회 가능한 구조**로 확장 가능
- 연속 실패 횟수 또는 시간 단위 실패 비율을 기준으로  
  외부 API 상태를 판단하고, 주 API 장애 시 보조 API 사용


### 3. 캐시 구조 확장
- 현재는 **단일 서버 환경을 가정**하여  
  `ConcurrentHashMap` 기반 캐시 사용
- 다중 서버 환경 확장 시
  - Redis 기반 분산 캐시 적용
  - TTL 정책을 통한 캐시 갱신 전략 도입


<br>

## 실행 방법

1. Application 실행
2. `http://localhost:8080/swagger-ui/index.html#/` 접속
3. 관련 API 실행
