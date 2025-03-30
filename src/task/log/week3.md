### 학습 내용 정리

#### 이번 주차에서는
- Layer에 대한 테스트 코드 학습
- 테스트 코드 적용 실습

### Layer에 대한 테스트 코드

#### 통합 테스트
- 통합 테스트의 목적 : 여러 모듈이 협력하는 기능을 통합적으로 검증. 서로 다른 두 객체가 협력해서 동작할 때 단위테스트로만으로 예측하기 매우 어렵다
- **풍부한 단위 테스트 & 통합 테스트** 두 가지 모두 필요. 단위 테스트는 각 모듈의 정상 작동을 보장, 통합 테스트는 전체 시스템의 흐름을 검증.

#### 레이어를 구분하는 이유
- 관심사의 분리! 각 레이어는 특정한 역할을 수행하도록 한다. 이를 통해 유지보수성 극대화

- Presentation Layer 
  - 외부 요청을 받고, 최소한의 검증을 수행. 
    - 도메인과 관련된 검증은 비지니스 레이어에서 수행
    - 문자열이 문자열 다운지 정도만 검증 
  - Business Layer, Persistence Layer를 Mocking해서 테스트
- Business Layer 
  - 비즈니스 로직을 구현하며, Persistence Layer와 함께 통합 테스트 진행 
- Persistence Layer 
  - 데이터 접근 역할을 수행하며, 비즈니스 로직은 포함X 
  - 거의 단위 테스트와 비슷한 성격을 가진다 
  - 간단한 경우라도 작성한 메서드 쿼리 보장, 추후에 jpa가 아닌 다른 환경으로 변할 수 도 있다. 이를 보장하기 위해 작성

#### 테스트시 사용 어노테이션
- @SpringBootTest 
  - 전체 애플리케이션을 로드하여 통합 테스트를 수행 
- @DataJpaTest 
  - JPA 관련 빈들만 주입하여 가벼운 테스트를 수행. 
  - 내부에 @Transactional이 존재하여 롤백이 된다.
- @WebMvcTest 
  - Presentation Layer에 대한 단독 테스트를 위해 사용 
  - 다른 레이어들은 mocking을 통해 제어 
  - JPA관련 빈들을 로드하지 않기 때문에 주의해야 할 점 
    - JPA 관련 빈을 로드하지 않는다. 따라서, @EnableJpaAuditing과 같은 JPA 관련 설정이 어플리케이션 클래스에 선언되어 있으면 테스트 시 JPA 관련 빈을 찾지 못해 오류가 발생한다 
      - @EnableJpaAuditing을 별도의 @Configuration 클래스로 분리하여 어플리케이션 클래스에서 제거 
      - 테스트 클래스에 JpaMetamodelMappingContext를 @MockBean으로 추가하는 방법도 있다
- @Transactional 
  - 서비스 테스트 시 @SpringBootTest + @Transactional 주의점 
    - 테스트 코드에서 @Transactional을 사용하면 비즈니스 로직이 트랜잭션을 포함하고 있는 것처럼 착각할 수 있다. 
  - @Transactional(readOnly = true)
    - CQRS : command(CUD)와 query(R)를 분리 
    - 쿼리용 서비스와 커맨드용 서비스를 분리할 수 도 있다. -> DB에 대한 엔드포인트 구분 
    - Master/Slave DB를 나눔으로써 부하를 줄임

#### 동시성 이슈
- 낙관적 락 (Optimistic Lock)
  - 데이터를 읽을 때는 락을 걸지 않고, 데이터를 업데이트할 때 충돌이 발생하지 않았는지 확인하는 방식 
  - 주로 읽기 작업이 많은 환경에서 사용, 데이터의 버전을 관리하여 충돌을 감지 
  - 장점: 데드락 가능성이 적고 성능이 비교적 좋다 
  - 단점: 충돌이 발생하면 롤백이나 재시도가 필요할 수 있다.
- 비관적 락 (Pessimistic Lock)
  - 데이터를 읽을 때부터 락을 걸고, 업데이트가 완료될 때까지 락을 유지하는 방식 
  - 주로 수정 작업이 많은 환경에서 사용, 데이터의 일관성을 유지하기 위해 미리 락을 걸어 다른 트랜잭션이 접근하지 못하게 한다. 
  - 장점: 데이터 무결성을 잘 유지할 수 있다. 
  - 단점: 락 경합으로 인해 성능 저하가 발생할 수 있다.

#### 테스트 코드 적용 실습
- 실습 진행 시 가장 중요하게 생각했던 부분 
  - 왜 이 부분에 테스트 코드가 필요할까? 
  - 주로 비지니스 로직이 있는 부분 
  - 오류가 나타나기 가장 쉬운 부분이라고 생각되어서 
- 뭘 어떻게 검증해야 할까? 
  - 예시 데이터가 로직(테스트 하고 싶은 메스드)을 거친 후 예상되는 결과가 정확하게 일치할 경우 테스트 통과하도록 작성 
- 어떤 테스트를 진행하는지 쉽게 알아보려면 (네이밍, bdd스타일 신경쓰기)
  - 도메인 용어를 사용 
- 실습 진행 프로젝트 위치
https://github.com/aammddkkzxc/readable-code/tree/main/src/test/java/cleancode/studycafe/tobe
- 어려웠던 부분 / 고민사항
https://github.com/aammddkkzxc/readable-code/blob/main/src/test/java/cleancode/studycafe/tobe/needfeedback.md