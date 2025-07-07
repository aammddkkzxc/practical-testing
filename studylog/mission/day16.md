### 레이어 별 특징 및 테스트 방법

#### Presentation Layer (프리젠테이션 계층)
- 특징 
  - 사용자 요청을 받아 비즈니스 계층으로 전달하고, 결과를 클라이언트에게 반환하는 역할. 
  - 입력 데이터에 대한 기본적인 검증을 수행. 
    - Presentation 계층에서의 검증과 Business 계층에서의 검증을 분리해서 생각해야 한다 
    - Presentation 계층에서는 보통 형식적인 검증을 한다(예시: 필수 입력 값 검사, 데이터 타입 검사, null 검사, 빈 문자열 검사, 등...)
    - 컨트롤러에서 사용하는 요청 DTO가 서비스 계층으로 침투하지 못하도록 컨트롤러 계층에서 서비스 전용 DTO로 변환하는 것을 권장(상황에 따라 다를 수 있다. 만약 받는 포맷이 변할 가능성이 거의 없다면, 그냥 컨트롤러의 DTO를 쭉 사용해도 괜찮을 수 있다.)
    - 프론트와의 소통을 위해 RestDocs라이브러리를 사용하기도 한다
- 테스트 방법 
  - MockMvc와 같은 도구를 사용하여 HTTP 요청과 응답을 시뮬레이션 (SliceTest)
  - Mockito 같은 프레임워크를 사용하여 Business Layer와 Persistence Layer를 Mocking 
    - 요청 객체와 응답 객체를 만들고, 관련 동작에 필요한 Service를 모킹해와서 컨트롤러의 동작을 검증 
  - @WebMvcTest 어노테이션을 사용하여 Presentation Layer만 로드하여 테스트 
    - 스프링을 띄우는 비용이 비싸기 때문에 테스트를 위한 스프링 서버를 최소한으로 띄우기도 한다

#### Business Layer (비즈니스 계층)
- 특징 
  - 핵심 비즈니스 로직을 처리하며, Persistence Layer와의 상호작용을 통해 로직을 수행 
  - 비즈니스 로직에 따른 도메인 유효성 검사가 이루어진다 
  - 트랜잭션 관리에 주의해야 한다
- 테스트 방법 
  - Persistence Layer를 Mocking하여 단위 테스트를 수행하거나, Persistence Layer와 함께 통합 테스트를 진행 
  - 테스트와 트랜잭션 관리법 
    - @BeforeEach와 @AfterEach를 사용하여 테스트 간 격리를 위한 setUp 및 tearDown 메서드를 사용 
    - @Transactional을 사용하여 트랜잭션을 관리 
      - 테스트 코드에서 @Transactional을 사용하면 비즈니스 로직이 트랜잭션을 포함하고 있는 것처럼 착각할 수 있으니 주의

#### Persistence Layer (데이터 접근 계층)
- 특징 
  - 데이터 소스와의 상호작용을 담당, CRUD 작업 수행 
  - 비즈니스 로직을 포함하지 않아야 한다 
  - 다양한 데이터베이스 기술들이 사용될 수 있다는 것을 염두 해 두어야 한다(JPA, JDBC, ...)
- 테스트 방법 
  - @DataJpaTest를 사용하여 JPA 관련 빈들만 주입하여 가벼운 테스트를 수행 하거나 
  - @SpringBootTest를 사용하여 전체 애플리케이션을 로드하여 통합 테스트를 수행할 수 있다. 
  - @SpringBootTest는 @Transactional을 포함하지 않으므로 주의