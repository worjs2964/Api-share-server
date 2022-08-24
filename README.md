# Api-share-server

본 서버는 [share-service](https://github.com/worjs2964/share-service) 를 REST API 형태로 변경한 서버이며, Swagger를 사용하여 
API 명세서를 작성하였습니다.   
REST API로 변경하면서 기존 코드보다 더 보기좋은 코드를 작성하기 위해 노력하였고, 공유에 State 패턴을 적용하는 등 설계를 개선하였습니다.

- 링크 : https://project-jg.link/swagger-ui.html

### 테스트용 아이디
  - 인증된 사용자 
    - ID: checked@share-service.com 
    - Password: Password96!
  - 인증되지 않은 사용자  
    - ID: unchecked@share-service.com 
    - Password: Password96!

### 배포 환경 및 배포 방법
- AWS (배포 환경)
  - EC2 (아마존 리눅스) 
  - RDS (Mysql)
  - ALB 
  - 그 외 Route53, VPC(subnet, route table), Docker, Git .. 
- 배포 방법
  1. 프로젝트를 Github에 push
  2. EC2에서 Github 프로젝트를 pull
  3. docker-compose를 사용하여 프로젝트 빌드 및 실행

### 사용 기술
- Java 11
- DB
  - Mysql
  - Redis
- Spring
  - Spring Boot
  - Spring Web
  - Spring Security
  - Spring Aop
- ORM
  - Spring Data JPA
  - Querydsl
- Thymeleaf
- Docker
- Swagger
- JWT
- Git

### 구현 기능

- 회원 
  - 회원 가입 및 프로필 확인, 수정 기능을 구현하였습니다.
    - 자기 프로필 및 타인 프로필 확인 가능 (타인의 프로필은 일부 정보만 표시하였습니다.)
  - 관심 키워드 등록 기능
    - Put 요청으로 한 번의 요청으로 관심 키워드를 변경할 수 있습니다.
- 인증 관련
  - JWT를 사용하여 로그인을 구현하였습니다.
  - Redis를 사용하여 로그아웃 및 AccessToken 재발급 기능을 구현하였습니다.
- 공유
  - State 패턴을 적용하여 if문의 사용을 줄이고, 새로운 상태를 추가하기 쉽게 구현하였습니다.
  - 공유에 키워드 등록, 관심 키워드를 가지고 있는 회원에게 알림을 보내는 요청(1회), 공유 공개/비공개 등의 기능을 구현하였습니다.
  - Qureydsl를 사용하여 공유 목록 검색 및 단건 검색 기능을 구현하였습니다.
- 결제
  - 결제에는 카카오 페이 API를 사용하여 구현하였습니다.
- 알림 
  - 새로운 알림과 읽은 알림으로 구분하고 각 알림은 생성, 가입, 변경 중 하나의 타입을 가지도록 구현하였습니다.
  - 읽은 알림을 일괄 삭제하는 기능을 구현하였습니다.
  
## 코드 설명
### 에외 처리
- 예외 처리는 RestApiAdvice 클래스를 통해 진행하였는데. RuntimeException을 상속받은 CustomException 클래스를 만들고 해당 클래스가 ErrorCode(HttpStatus와 메시지를 가지고 있는 Enum 클래스)를 가지고 있게 하여 일괄적으로 처리할 수 있게 구현하였습니다.

 ```
     @ExceptionHandler(CustomException.class)
     public ResponseEntity<ErrorResponse> CustomException(CustomException e, HttpServletRequest request) {
        log.error("URI: ({}){}",request.getMethod(), request.getRequestURI(), e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
     }
```
 

### JWT
- OncePerRequestFilter를 상속받은 JwtAuthenticationFilter의 doFilterInternal 메소드 중 일부 코드입니다.
  - jwt에서 memberUid을 가져오고 검증을 진행 후 정상적인 토큰이라면 SecurityContextHolder에 회원에 정보를 넣어줍니다.  
  만약 토큰이 정상적이지 않다면 request에 ErrorCode를 넣어주는 데 이 값은 jwtEntryPoint(인증되지 않은 사용자를 처리하는 커스텀 AuthenticationEntryPoint)에서 사용됩니다.
```    
    String accessToken = getToken(authorization);

    try {
        String uid = jwtTokenUtil.getMemberUid(accessToken);
        if (uid != null && !isLogout(accessToken)) {
            MemberAccount memberAccount = userDetailsServiceImpl.loadUserByUsername(uid);
            processSecurity(request, memberAccount);
        } else {
            request.setAttribute("exception", ErrorCode.INVALID_AUTH_TOKEN);
        }
    } catch (JwtException e) {
        request.setAttribute("exception", ErrorCode.INVALID_AUTH_TOKEN);
    }
```

```
    private void processSecurity(HttpServletRequest request, MemberAccount memberAccount) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(memberAccount, null, memberAccount.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
```
- 로그아웃의 경우 해당 토큰을 redis에 만료 시간 동안 보관하여, redis에 해당 AccessToken이 있는 경우 로그아웃된 토큰으로 판단하였습니다.
- 토큰 재발급의 경우 memberUid를 Id로 Redis에 보관하여 Redis에서 보관된 RefreshToken과 요청으로 보내온 RefreshToken을 비교한 후 검증을 진행하여 재발급을 진행하였습니다.

### 공유
- 공유에 State 패턴을 적용하여 각 상태에 맞는 동작을 수행하도록 구현하였습니다.
  - DB에서 데이터를 가져온 후 상태를 적용하기 위해 Enum 클래스로 구현 후 Share 객체의  ShareState 필드를 변경하는 식으로 State 패턴을 구현하였습니다.
  
```
    @RequiredArgsConstructor
    public enum ShareState implements ShareStateOperation {
  
    INVISIBLE(new InvisibleShare()),
    VISIBLE(new VisibleShare()),
    FULL(new FullShare()),
    TERMINATED(new TerminatedShare());
  
    private final ShareStateOperation operations;
  
  
    @Override
    public boolean canJoinShare(Share share, Member member) {
        return operations.canJoinShare(share, member);
    }
    @Override
    public Share editShare(Share share) {
        return operations.editShare(share);
    }
    @Override
    public boolean canChangeKeyword(Share share) {
        return operations.canChangeKeyword(share);
    }
    @Override
    public Share changeVisible(Share share) {
        return operations.changeVisible(share);
    }
    @Override
    public boolean canNotify(Share share) {
        return operations.canNotify(share);
    }
}
```
- 회원과 공유의 N:M 관계를 1:N, N:1 관계를 풀어내기 위해 중간에 MemberShare 도메인을 두었고, 추가적으로 결제에 관한 정보를 저장하도록 구현하였습니다.
```  
    @Entity @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class MemberShare {
  
    @Id
    @GeneratedValue
    private Long id;
  
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
  
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;
  
    private String tid;
  
    private String paymentMethodType;
  
    private LocalDateTime approvedAt;
    }
```

### 키워드 등록
- 키워드 등록/삭제의 경우 중복을 제거하기 위해 Set 자료형으로 keyword를 받은 후 KeywordService에서 해당 키워드 엔티티가 있는지 확인한 후 기존 키워드를 반환하고 새 키워드는 저장 후 반환하여 Set을 만든 후 요청한 회원(또는 공유)에 등록하였습니다.
```  
    @Service
    @Transactional
    @RequiredArgsConstructor
    public class KeywordService {
  
        private final KeywordRepository keywordRepository;
      
        public Set<Keyword> saveKeywords(Set<KeywordDto> keywordSet) {
            return keywordSet.stream().map(KeywordDto::getKeyword).map(keyword ->
               
                // 해당 키워드가 있으면 반환하고 없으면 키워드를 저장하고 반환
                keywordRepository.findByKeyword(keyword).orElseGet(() ->
                keywordRepository.save(KeywordReq.toEntity(keyword))
                )
            ).collect(Collectors.toSet());
        }
    }
```
### 결제
- 결제의 경우 카카오페이 API를 사용하여 구현하였으며 결제 승인 과정(approvePay 메소드)에선 결제 준비 과정(readyPay 메소드)에서 발급한 tid 값이 필요한데. 회원은 같은 공유에 참가할 수 없다는 점을 활용하여 회원, 공유에 대한 정보로 주문 번호를 생성하고 해당 주문 번호를 key 값으로 Map에 담아 사용하였습니다.

### 로그 저장
- 운영 시 활용할 수 있도록 Spring AOP를 사용하여 요청에 대한 로그 남기고(LogApiAop 클래스), 해당 로그와 에러 로그(RestApiAdvice 클래스)를 파일로 저장하도록 하였습니다.
  - 로그는 30일 동안 저장하도록 하였고, 서버가 Docker에서 돌아가기 때문에 컨테이너가 종료 시 로그 파일이 날라가지 않도록 볼륨을 지정하여 컨테이너 외부에 보관하였습니다.

