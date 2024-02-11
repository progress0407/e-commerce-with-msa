# E-Commerce-with-MSA-and-DDD

## 현재 상황

> 다른 일정으로 잠정적으로 보류된 프로젝트에요.  
> 서둘러 작성할게요...

## 작업 체크 리스트

[기존 DDD 프로젝트](https://github.com/progress0407/code-review-simple-orders)를 MSA 프로젝트로 전환

- [x] 기존 프로젝트 Git Subtree 임포트
- [x] Java -> Kotlin 변경
- [x] Library 버전 최신화
- [x] 멀티 모듈로 전환
- [x] Eureka Module 개발
- [x] API Gateway Module 개발
- [x] 공통 Module 추출
- [x] RabbitMQ 연동 후 주문 상품 이벤트 pub-sub 개발
- [x] H2 -> MySQL로 DB 변경
- [ ] RabbitMQ -> Kafka로 전환
- [x] [User Module](https://github.com/progress0407/intergrated-study/tree/main/0.%20study/1.%20alone/%5BMSA%5D%20Spring%20Cloud%20MicroService/leedowon-msa-project/user-service) 옮겨오기
- [x] 쿠폰 Module 개발
- [x] Neflix Passport 구현
  - 보류 (동기 호출 Blocking 예외)
  - [x] 토큰 검증 필터 구현
- [x] Order <-> Item 주문 생성 검증 구현
- [x] 마이크로 서비스 2-depth 멀티 모듈로 그룹화
- [ ] Coupon 가격 계산 API 구현
  - [ ] Item -> Coupon, 상품 Semi 데이터 이벤트 발송 기능 구현

## 이 프로젝트의 한계

> 실제로 MSA의 극한까지 Best Practice를 지키면서 만들지는 못했다

- 각 마이크로 서비스가 충분히 크지 않다
  - 개발자들이 공감할 수 있을 정도로 충분히 크지 않다
- 실제로, 물리적으로 분리되지 않은 DB
  - 하나의 DB에 스키마를 나누어 사용하고 있음

## 하지 못한 것

- 실제 결제 연동
  - 토스 PG 등 실제 결제 연동까지 진행하지는 못했다
- 브로커 클러스터에 문제가 생긴 경우에 대한 장애 처리
  - 이 부분까지는 진행하지 못했다

## 고려한 점

### `클린한 코드`를 위한 노력

- 유지보수성 좋은 코드가 개발자들의 불행 지수를 낮춘다고 생각한다


### Rest한 API를 위한 노력

- 자가 서술적 메시지 등에 모든 것에 대해 지키지는 않았다
- 자원과 행위에 대해 생각하면서 API스펙을 작성했다

### **DDD**를 적용 노력

  - 트랜잭션 `스크립트 방식보다는` 도메인에 주도권이 있는 설계를 하고자 함
  - Order의 경우 OrderLine에 대해 온전히 관리
    - 따라서 영속성 전이, 고아 객체 제거 기능을 가지고 있음
  - Aggregate를 적용하려고 노력
    - 주문 도메인의 Aggregate Root는 Order이다
    - 주문이 생성될 때 
      - 주문항목 또한 같이 생성
      - Item의 재고수량이 감소
- Item과 Order 도메인을 나눔. 각각은 서로에 대해 의존하지 않는다
  - Spring Domain Event를 내부 이벤트 -> Kafka 외부 이벤트로 전환
  - <img src="https://user-images.githubusercontent.com/66164361/216045147-4c15d80a-ebfa-4030-85ae-f64f440e0dcd.png" alt="drawing" width="600"/>

### 작은 성능 고려

- `OSIV`를 종료
  - 요청 쓰레드 반환 시점을 이르게
  - 더티체킹 발생시 Service Layer 위쪽은 볼 필요 없음
- `1+N` 최적화
  - by `global batch size`
 
## 생략한 것

아래는 앱 규모상 생략한 내용에 대해 기술

### 예외처리 및 핸들링

- 전역 예외처리기(ControllerAdvice)에 대해 생략

### 커스텀 예외

- ItemNotFoundException 등에 대해 생략
- IllegalArgumentException 등 built-in Exception 으로 대체
