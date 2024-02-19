# e-commerce with MSA

> MSA에 대한 Best Practice 보다는 탐구 목적에 가깝게 개발하였습니다.

## 프로젝트 설명

| 모듈 명        | 한글 뜻          | 설명                                                                                   |
|-------------|---------------|--------------------------------------------------------------------------------------|
| eureka      | 서비스<br/>디스커버리 | 마이크로 서비스를 이름을 통해 실제 IP, Port를 찾을 수 있게 한다                                             |
| api-gateway | 게이트웨이         | 각 마이크로 서비스를 라우팅하고<br/>인가/로깅 등의 부가 기능을 수행한다 (reverse-proxy)                           |
| user        | 사용자·인증        | 사용자에 대한 정보를 담당하고 인증에 대한 역할을 가지는 서비스                                                  |
| item        | 상품            | 상품을 관리하는 서비스<br/> Command 기능의 경우 상품 관리자가 이용하는 서비스                                    |
| coupon      | 쿠폰            | 상품 할인에 대한 쿠폰 관리하는 서비스 <br/>고정 할인 / 비율 할인이 있다                                         |
| order       | 주문            | 상품들에 대한 주문을 관리하는 서비스                                                                 |
| payment     | 결제            | (`검토 예정`) PG사를 통해 결제를 하는 서비스 <br/>(실제로는 PG 연동을 하지 않고 모의 상황을 가정했다)                    |
| query       | 조회            | (`구현 예정`) BFF, 클라이언트를 위한 조회 서비스 <br/>여러 마이크로 서비스 테이블 정보가 필요한 복잡한 쿼리(통계 등)는 이곳에서 담당한다 |

## 고민한 점

- 분해 전략
    - DDD의 `sub-domain`과 `bounded-context`를 통해 각 마이크로 서비스를 분리하고자 함
- 성능
    - 비동기 호출: 주문(`POST /orders`)의 경우 되도록 동기 통신이 아닌 비동기 통신을 구현하고자 노력
        - 이 부분이 잘 이루어질 경우 DB Connection 선점 시간을 짧게 가져갈 수 있다
    - Table 복제: 타 마이크로 서비스의 테이블의 일부 데이터를 복제하여 통신을 일부 제거하고자 노력
        - 다만, 각 마이크로 서비스 간의 결합도가 생긴다 (`분산 모놀리스가 될 위험성 존재`)
- 유지보수 성
    - spring-cloud-gateway의 route 코드: API가 추가될 수록 관리가 용이하기 힘들기 때문에 kotlin의 문법을 통해 간소화하고자 함
        - kotlin-dsl로 구현한 테스트 코드를 통해 라우트 정보가 올바로 등록했는 지 알 수 있다
    - 공통 모듈
        - common 모듈을 성격에 따라 구분해서, 각 모듈이 필요한 모듈을 import할 수 있다
    - 코드 레벨
        - human-readable한 코드를 작성하려고 노력했다 (네이밍 컨벤션, 약어 제한 등)
        - Restful API 원칙 중 Uniform interface를 지키고자 노력 (자원, 행위)
        - 통합 테스트를 작성하려 노력 (`진행 중`/`전면 재작성 필요 - contract test`)
            - 기본 베이스는 인수테스트로 하되, 이 중 `then`절만 `DB` 조회를 통해서 테스트
- 데이터 정합성
    - 분산 트랜잭션
        - SAGA(코레오그래피)를 이용해서 예외 상황에 대한 rollback
        - Out Box 패턴을 통해 브로커가 장애가 났을 경우 보완하고자 노력
            - 브로커 복구시 Scheduler가 OutBox테이블에서 미발송된 이벤트를 재 적재한다
    - 로컬 트랜잭션
        - 조회시 버전 증가 잠금을 통해 애그리거트의 정합성이 깨질 위험을 방지
            - 애그리거트 루트가 아닌 구성요소가 바뀌는 경우 고려
- 실제 사용자 use-case를 고려하여 API를 설계
    - 무신사, 쿠팡 등의 e-commerce을 참고하여, 실제 사용자의 API 호출을 가정하여 작성

## 이 프로젝트의 한계

> 실제로 MSA의 극한까지 Best Practice를 지키면서 만들지는 못했다

- 각 마이크로 서비스가 충분히 크지 않다
    - 현업 개발·기획자들이 공감할 수 있을 정도로 충분히 크지 않다
    - 실제 이용자 수가 많은 서비스와 비교하면 디테일함이 많이 부족하다
- 각 JVM 인스턴스가 차지하는 성능
    - 로컬에서 6개 이상의 서버가 구동될 때, 성능 부담이 된다 (많게는 8.9Gi~10.gGi 메모리 사용)
    - 로컬 환경 스펙 (노트북)
        - CPU: 3.2Ghz 8Core / Memory: 28GB / SSD

## 하지 못한 것

- 실제 결제 연동
    - 토스 PG 등 실제 결제 연동까지 진행하지는 못했다
    - 또 편의상의 이유로 실제 결제의 흐름을 축약/왜곡하여 작성하였다
- 브로커 클러스터에 문제가 생긴 경우에 대한 장애 처리
    - 이 부분까지는 진행하지 못했다
- 물리적으로 분리되지 않은 DB
    - 하나의 물리 DB를 논리적으로 나누어 사용하고 있음
    - 로컬 성능/비용의 부담으로 실제와 가깝게 구현하지 못함

## 현재 상황

> 여러 일정으로 잠정적으로 보류되어왔던 프로젝트입니다...  
> 여유가 될 때마다 구현하고 있습니다 :)

## 작업 체크 리스트

- [x] (마이그레이션) [기존 DDD 프로젝트](https://github.com/progress0407/code-review-simple-orders) (import by `Git Subtree`)
- [x] Java -> `Kotlin` 으로 언어 변경
- [x] Library 버전 최신화 (to Spring Boot 3.x)
- [x] 멀티 모듈 프로젝트로 전환 (`itme`, `order`)
- [x] `Eureka` Module 개발
- [x] `API Gateway` Module 개발
- [x] `공통 Module` 추출 (`common`)
- [x] RabbitMQ 연동 후 주문 상품 이벤트 pub-sub 개발
- [x] H2 -> MySQL로 DB 변경
- [ ] RabbitMQ -> Kafka로 전환
- [x] (마이그레이션) [User Module](https://github.com/progress0407/intergrated-study/tree/main/0.%20study/1.%20alone/%5BMSA%5D%20Spring%20Cloud%20MicroService/leedowon-msa-project/user-service)가져오기
- [x] 쿠폰 Module 개발
- [x] `Neflix Passport` 구현
    - 보류 (동기 호출시 Blocking 예외 발생)
    - [x] 대안으로 토큰 검증 필터 구현
- [x] Order -> Item, Coupon: 주문 생성 이벤트 검증부 구현
- [x] 마이크로 서비스 `2-depth 멀티 모듈`로 그룹화
- [x] `p6spy 로그 포맷터` 적용
- [x] `Coupon 가격 계산 API` 구현
- [x] `repository.saveAll()`에서 `data.sql` 로 초기화하는 구조로 변경
- [x] Item -> Coupon, 상품 Semi 데이터 이벤트 발송 기능 구현
- [ ] 마이크로 서비스 내 애그리거트 트랜잭션 충돌 방지
      - 애그리거트 내 구성요소가 바뀔 경우 고려
      - 애그리거트 수정시 조회 메서드에 LockModeType.OPTIMISTIC_FORCE_INCREMENT 적용
- [x] Order <-> Coupon, Item 보상 트랜잭션 발신, 송신 구현
- [x] Canceled 상태 추가 및 로직 구현
- [ ] Order 바운디드 컨텍스트에 Orderer 추가
- [ ] k8s 배포 스크립트 작성