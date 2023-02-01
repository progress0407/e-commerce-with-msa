# 주문 API 애플리케이션

## 기능 요약

- 상품: 등록, 리스트 조회
- 주문: 등록

## 고려한 점

### `클린한 코드`를 작성하려 노력했습니다.

- 유지보수성 좋은 코드가 개발자들의 업무 행복 지수를 높여준다고 생각합니다.

### **동시성 보장**을 하려고 **노력**했습니다. (**Thread Safety**)
  - 주문을 동시에 하더라도 `낙관적 락`을 통해서 동시 주문에 대한 안정성을 보장합니다.
  - 테스트코드는 `OrderServiceConcurrencyTest`에 있습니다.

### Rest한 API를 작성하려고 노력했습니다.

- 자가 서술적 메시지 등에 모든 것에 대해 지키지는 않았습니다.
- 자원과 행위에 대해 생각하면서 API스펙을 작성했습니다.

### **DDD**를 적용하고자 노력했습니다.

  - 트랜잭션 `스크립트 방식보다는` 도메인에 주도권이 있는 설계를 하고자 하였습니다.
  - Order의 경우 OrderLine에 대해 온전히 관리합니다.
    - 따라서 영속성 전이, 고아 객체 제거 기능을 가지고 있습니다.
  - Aggregate를 적용하려고 노력했습니다.
    - 주문 도메인의 Aggregate Root는 Order입니다.
    - 주문이 생성될 때 
      - 주문항목 또한 같이 생성됩니다.
      - Item의 재고수량이 감소합니다.
- Item과 Order 도메인을 나누었습니다. 각각은 서로에 대해 의존하지 않습니다.
  - Spring Domain Event를 내부 이벤트로 사용했습니다.
  - <img src="https://user-images.githubusercontent.com/66164361/216045147-4c15d80a-ebfa-4030-85ae-f64f440e0dcd.png" alt="drawing" width="600"/>

### 작지만 성능에 대해 생각했습니다.

- `OSIV`를 종료했습니다.
- `1+N` 최적화를 했습니다.
  - by `global batch size`
 
## 생략한 것

아래는 앱 규모상 생략한 내용에 대해 기술했습니다.

### 예외처리 및 핸들링

- 전역 예외처리기(ControllerAdvice)에 대해 생략했습니다.

### 커스텀 예외

- ItemNotFoundException 등에 대해 생략했습니다.
- IllegalArgumentException 등 built-in Exception 으로 대체했습니다.
