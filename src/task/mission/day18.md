### Q1. @Mock, @MockBean, @Spy, @SpyBean, @InjectMocks 의 차이를 한번 정리해 봅시다.
#### Mock 관련
- @Mock 
  - 패키지: org.mockito 
  - 대상: 일반 객체 
  - 동작: 실제 객체 대신 가짜(Mock) 객체를 생성하여 테스트에 사용. 
  - 사용 예: 단위 테스트 환경에서 의존성 주입 없이 독립적으로 사용.
- @MockBean 
  - 패키지: org.springframework.boot 
  - 대상: Spring 컨텍스트에 등록된 Bean 
  - 동작: 스프링 컨텍스트에 등록된 Bean을 Mock 객체로 대체하여 테스트. 
  - 사용 예: 스프링 환경에서 테스트를 수행할 때 사용. 

#### Spy 관련
- @Spy 
  - 패키지: org.mockito 
  - 대상: 일반 객체 
  - 동작: 실제 객체를 생성하되, 특정 메서드에 대해 행위와 결과를 지정할 수 있음 (부분 모킹)
  - 사용 예: 단위 테스트 환경에서 실제 객체의 일부 메서드만 모킹할 때 사용.
- @SpyBean
  - 패키지: org.springframework.boot
  - 대상: Spring 컨텍스트에 등록된 Bean
  - 동작: 스프링 컨텍스트에 등록된 Bean을 Spy 객체로 대체하여 부분 모킹
  - 사용 예: 스프링 환경에서 실제 빈의 일부 메서드를 모킹할 때 사용.

#### InjectMocks
- 패키지: org.mockito 
- 대상: @Mock 또는 @Spy로 생성된 객체 
- 동작:
  - 테스트 대상 클래스의 인스턴스를 생성. 
  - 해당 클래스가 의존하는 객체들을 자동으로 주입 (@Mock 또는 @Spy로 선언된 객체가 주입)
- 사용 예:
```
@Mock
UserRepository userRepository;
@InjectMocks
UserService userService; // userRepository가 자동으로 주입
```

#### @InjectMocks vs @Autowired
- @InjectMocks:
  - Mock 또는 Spy를 통해 의존성을 주입.
  - 단위 테스트 환경에서 의존성을 자동으로 설정. 
- @Autowired:
  - 스프링 컨테이너에서 관리되는 빈을 가져와 의존성을 설정.
- 예시 비교:

```
// 단위 테스트
@Mock
UserRepository userRepository;
@InjectMocks
UserService userService; // userRepository가 주입

// 스프링 컨테이너
@MockBean
UserRepository userRepository;
@Autowired
UserService userService; // userRepository가 주입
```

### Q2. 각 항목 배치하기

- 집중한 부분 : 현재 테스트가 어떤 것(도메인)을 테스트 하고 있는가?
  - 어떤 것을 setUp에 위치시킬까

```
public class CommentServiceTest {

    @BeforeEach
    void setUp() {
        1-1, 2-1, 3-1 사용자1 생성에 필요한 내용 준비
        1-2, 2-2, 3-2 사용자1 생성
        1-3, 2-3, 3-5 사용자1의 게시물1 생성에 필요한 내용 준비
        1-4, 2-4, 3-6 게시물1 생성
    }

    @DisplayName("사용자가 댓글을 작성할 수 있다.")
    @Test
    void writeComment() {
        // given
        1-5. 댓글 생성에 필요한 내용 준비

        // when
        1-6. 댓글 생성

        // then
        검증
    }

    @DisplayName("사용자가 댓글을 수정할 수 있다.")
    @Test
    void updateComment() {
        // given
        2-5. 댓글 생성에 필요한 내용 준비
        2-6. 댓글 생성

        // when
        2-7. 댓글 수정

        // then
        검증
    }

    @DisplayName("자신이 작성한 댓글이 아니면 수정할 수 없다.")
    @Test
    void cannotUpdateCommentWhenUserIsNotWriter() {
        // given
        3-3. 사용자2 생성에 필요한 내용 준비
        3-4. 사용자2 생성
        3-7. 사용자1의 댓글 생성에 필요한 내용 준비
        3-8. 사용자1의 댓글 생성

        // when
        3-9. 사용자2가 사용자1의 댓글 수정 시도

        // then
        검증
    }
}
```
