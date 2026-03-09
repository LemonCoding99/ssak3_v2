<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/986c581f-0913-4ace-ae19-37ed07ce93e9" />



<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/a24bab17-4e27-4eda-90fb-591422aa2503" />



# “굿즈 커머스 플랫폼 - 싹쓰리”

<br><br>

## 목차
<!-- 하이퍼링크 걸어서 넣기 -->
- [📄 프로젝트 소개](#-프로젝트-소개)
- [👊 핵심 기능](#-핵심-기능)
- [🔧 기술 스택](#-기술-스택)
- [📊 서비스 흐름도](#-서비스-흐름도)
- [⚙️ 시스템 아키텍쳐](#️-시스템-아키텍쳐)
- [💻 와이어프레임](#-와이어프레임)
- [📑 ERD](#-erd)
- [📝 API 명세서](#-api-명세서)
- [🛠 기술적 의사 결정](#-기술적-의사-결정)
- [🚩 트러블 슈팅](#-트러블-슈팅)
- [📈 성능 개선](#-성능-개선)
- [👨‍👩‍👧‍👦 팀원 소개](#team)

<br><br>

---
## **📄** 프로젝트 소개
<br>

**“트렌디한 한정판 굿즈와 실시간 소통을 결합한 이커머스 플랫폼, 싹쓰리”**

*갖고 싶은 모든 굿즈를 쉽고 빠르게 '싹' 쓸어 담아보세요😆*

**굿즈 시장의 폭발적 성장과 팬덤 경제**

최근 MZ세대를 중심으로 좋아하는 연예인, 캐릭터, 브랜드를 소비하는 '팬덤 경제'가 가파르게 성장하고 있습니다. 굿즈는 단순히 물건을 사는 것을 넘어 개인의 취향을 드러내는 수단이 되었으며, 이에 최적화된 전문 플랫폼의 필요성을 느꼈습니다.

**한정판 및 타임 세일에 특화된 구매 경험 제공**

인기 굿즈는 오픈과 동시에 품절되는 경우가 많습니다. 이를 위해 **타임딜** 기능과 **주간 인기 TOP 10** 기능을 도입하여 사용자에게 긴장감 넘치는 구매 경험을 제공하고, 트렌디한 상품을 놓치지 않게 돕고자 했습니다.

**소통과 신뢰 기반의 커머스**

일반적인 쇼핑몰과 달리 굿즈는 커뮤니티 성격이 강합니다. 한정된 물량인 경우가 많아 문의 요청도 활발한 편입니다. 이에 따라 **1:1 문의 게시판**과 **실시간 문의 채팅**을 통해 고객의 궁금증을 즉각 해소하고, **카카오 로그인**과 **쿠폰/결제 시스템**을 통해 실제 상용 서비스 수준의 편리한 구매 여정을 구현하는 데 집중했습니다.

<br><br>

---

## 👊 핵심 기능

<br>

<details open>
<summary><b>🔎 수많은 데이터 속에서, 원하는 상품을 즉각 찾아내는 정교한 필터링</b></summary>
<br>
  
> *통합 검색은 **키워드와 상세 가격 범위를 조합한 동적 필터링** 기능으로,
쏟아지는 상품 속에서 사용자가 원하는 조건에 딱 맞는 상품을
정교한 필터링으로 찾아내어 보여줍니다.*
>

- 검색 시점에 진행 중인 특가 혜택을 실시간으로 제공하여 일반가와 할인가를 한눈에 비교
- 상품명과 상세 정보 내 키워드로 탐색하여 원하는 상품만 검색
- 최소 가격부터 최대 가격까지 사용자의 예산에 최적화된 상품만 검색
- 판매 중단이나 삭제된 상품을 제외하고 현재 구매 가능한 최적의 상품만 노출

<br>
</details>

<details open>
<summary><b>🏆 찰나의 승부, 실시간으로 요동치는 가장 핫한 상품 리스트</b></summary>
<br>
  
> *인기 조회수 랭킹은 **Redis ZSet 기반의 실시간 집계 시스템**으로,
수많은 사용자의 클릭을 즉각적으로 계산하여
지금 이 순간 가장 인기 있는 상품을 신뢰도 있게 보여줍니다.*
> 
- 데이터 추가와 동시에 순위가 정렬되어 끊김 없는 최신 정보 제공
- 여러 대의 서버에서도 랭킹 데이터를 공유하여 사용자에게 동일한 인기 순위 보장
- 과거의 데이터에 머물지 않고, 현재 시점의 실제 인기를 반영한 합리적인 구매 지표 제공
- DB 부하를 최소화하고 응답 속도를 극대화하여 메인 페이지 내 지연 없는 리스트 노출

<br>
</details>

<details open>
<summary><b>🛍️ 정해진 시간에만 열리는 한정 특가, 단 한 번의 기회 타임딜</b></summary>
<br>
  
> *타임딜은 일정 시간 동안만 오픈되는 한정 특가 상품으로,
사용자는 **오픈 시간에 맞춰 실시간으로 특가 정보를 확인**하고
남은 시간을 보며 구매 타이밍을 직접 선택할 수 있습니다.*
> 
- 서버 시간 기준의 스케줄링을 통해 클라이언트 기기 시간과 무관한 공정한 오픈 시간 보장
- 스케줄러 기반 상태 자동 전환을 통해 타임딜 종료 즉시 판매를 차단하고 공정한 판매 환경 유지
<br>
</details>

<details open>
<summary><b>🛒 담는 순간 완성되는 최적의 할인, 고민 없이 누리는 스마트 쇼핑</b></summary>
<br>
  
> *장바구니에 상품을 담는 것부터 
주문, 쿠폰 적용까지 하나의 흐름으로 연결하여
**데이터 정합성**을 중심으로 설계했습니다.*
> 
- 재고 · 쿠폰 · 상태 전이를 통합한 구매 흐름 설계
- 상품을 원하는 수량만큼 장바구니에 담되, 재고 초과 시 즉시 차단
- 장바구니에서 일부 상품만 선택하여 주문 가능
- 주문 시 쿠폰 적용 가능, 결제 실패 시 쿠폰 상태 자동 복구

<br>
</details>

<details open>
<summary><b>💳 단 1초의 망설임 없는 승인, 가장 안전한 결제의 마침표</b></summary>
<br>
  
> *결제는 외부 PG(Toss)와 연동되며,
**금액 위변조 방지**와 **보상 트랜잭션 처리**에 초점을 맞춰 설계했습니다.*
> 
- 주문 검증 → PG 승인 → 상태 전이까지 통합 설계
- 서버에서 주문 금액 재검증 후 PG 승인 요청
- 승인 성공 시 주문 상태 완료 처리 및 장바구니 정리
- 승인 실패 시 재고 · 쿠폰 롤백
- 주문 취소 시 PG 결제 취소 처리

<br>
</details>
<details open>
<summary><b>🔗 기다림 없는 소통, 판매자와 구매자를 잇는 즉각적인 연결 고리</b></summary>
<br>
  
> *문의 채팅은 **웹 소켓 기반의 실시간 1:1 상담** 기능을 제공하여
서비스 이용 중 발생한 궁금증에 즉시 답을 얻을 수 있도록 설계했습니다.*
> 
- 복잡한 상황도 대화형 소통으로 빠르게 해결
- 결제 직전 고민이나 문제 상황에서 즉각 응대
- 상담 페이지 종료 후에도 재접속 가능하여 대화 맥락 유지
- 한 번에 하나의 채팅방만 생성 가능하도록 하여 과도한 문의 방지
- 관리자와 1:1 매칭으로 책임 있는 상담 제공
- ‘접수 완료 → 검토 중 → 답변 완료’ 단계별 상태 확인
- 필요 시 상담 종료 및 관리자 강제 종료 기능 지원
<br>
</details>


<br><br>

---

## 🔧 기술 스택

#### 💻 Language
[![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)

#### 🧩 Framework & Library
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-59666C?style=for-the-badge)](https://spring.io/projects/spring-data-jpa)
[![Spring WebClient](https://img.shields.io/badge/Spring_WebClient-6DB33F?style=for-the-badge)](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
[![QueryDSL](https://img.shields.io/badge/QueryDSL-0769AD?style=for-the-badge)](https://querydsl.com/)
[![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge)](https://projectlombok.org/)

#### 🗄 Database
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

#### ⚡ Middleware
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)

#### 🧪 Test
[![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge)](https://site.mockito.org/)

#### 🧰 Test Tools
[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)
[![k6](https://img.shields.io/badge/k6-7D64FF?style=for-the-badge&logo=k6&logoColor=white)](https://k6.io/)
[![Apache JMeter](https://img.shields.io/badge/Apache_JMeter-D22128?style=for-the-badge)](https://jmeter.apache.org/)

#### 🧑‍💻 IDE
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)](https://www.jetbrains.com/idea/)

#### 📊 Monitoring
[![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)](https://prometheus.io/)
[![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)](https://grafana.com/)

#### 📈 Log Visualization & Analysis
[![Filebeat](https://img.shields.io/badge/Filebeat-005571?style=for-the-badge)](https://www.elastic.co/beats/filebeat)
[![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)](https://www.elastic.co/elasticsearch/)
[![Logstash](https://img.shields.io/badge/Logstash-005571?style=for-the-badge)](https://www.elastic.co/logstash/)
[![Kibana](https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=kibana&logoColor=white)](https://www.elastic.co/kibana)

#### 👥 Community
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/)
[![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)](https://www.notion.so/)
[![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)](https://slack.com/)
[![Zep](https://img.shields.io/badge/Zep-5A29E4?style=for-the-badge)](https://zep.us/)

#### 🎨 Design
[![ERD Cloud](https://img.shields.io/badge/ERD_Cloud-7A5AF8?style=for-the-badge)](https://erdcloud.com/)
[![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)](https://www.figma.com/)
[![draw.io](https://img.shields.io/badge/draw.io-F08705?style=for-the-badge)](https://app.diagrams.net/)
[![Excalidraw](https://img.shields.io/badge/Excalidraw-6965DB?style=for-the-badge)](https://excalidraw.com/)

#### 🔐 Security
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)

#### 🔄 CI/CD
[![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)](https://docs.github.com/en/actions)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

#### 📚 OpenAPI
[![Toss Payments](https://img.shields.io/badge/Toss_Payments-0064FF?style=for-the-badge)](https://docs.tosspayments.com/)
[![Kakao OAuth 2.0](https://img.shields.io/badge/Kakao_OAuth_2.0-FFCD00?style=for-the-badge)](https://developers.kakao.com/)

#### ⚡ Real-Time Communication
[![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge)](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API)

#### ☁ AWS
[![AWS ECS](https://img.shields.io/badge/AWS_ECS-FF9900?style=for-the-badge)](https://aws.amazon.com/ecs/)
[![Amazon ECR](https://img.shields.io/badge/Amazon_ECR-FF9900?style=for-the-badge)](https://aws.amazon.com/ecr/)
[![AWS RDS](https://img.shields.io/badge/AWS_RDS-527FFF?style=for-the-badge)](https://aws.amazon.com/rds/)
[![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge)](https://aws.amazon.com/s3/)
[![AWS Route 53](https://img.shields.io/badge/AWS_Route_53-8C4FFF?style=for-the-badge)](https://aws.amazon.com/route53/)
[![AWS ALB](https://img.shields.io/badge/AWS_ALB-8C4FFF?style=for-the-badge)](https://aws.amazon.com/elasticloadbalancing/)
[![Internet Gateway](https://img.shields.io/badge/Internet_Gateway-FF9900?style=for-the-badge)](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_Internet_Gateway.html)
[![NAT Gateway](https://img.shields.io/badge/NAT_Gateway-FF9900?style=for-the-badge)](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-nat-gateway.html)
[![AWS IAM](https://img.shields.io/badge/AWS_IAM-FF9900?style=for-the-badge)](https://aws.amazon.com/iam/)

<br><br>

---

## 📊 서비스 흐름도

<details>
  <summary><b>[ 회원 서비스 흐름도 ]</b></summary>

  <img width="512" height="471" alt="image" src="https://github.com/user-attachments/assets/f7aa0ab8-31a7-4600-b182-2be275d38b06" />

</details>
<details>
  <summary><b>[ 관리자 서비스 흐름도 ]</b></summary>

  <img width="512" height="516" alt="image" src="https://github.com/user-attachments/assets/ec4bcbc5-e8d6-4bce-83b7-f750285d7f5a" />

</details>
<br><br>

---
## ⚙️ 시스템 아키텍쳐
<details>
<summary><b>v1</b></summary>

<img width="350" height="400" alt="image" src="https://github.com/user-attachments/assets/a6a17ad2-15dc-4fbc-adc0-759ddf606477" />

</details>
<details>
<summary><b>v2</b></summary>

<img width="400" height="450" alt="image" src="https://github.com/user-attachments/assets/bfc7ac4f-c387-4fb5-9590-28f8b8cd8d0e" />


</details>
<details>
<summary><b>v3</b></summary>

<img width="500" height="941" alt="image" src="https://github.com/user-attachments/assets/5547f697-c0ab-4910-b1a5-697136b7f922" />


</details>

<details open>
<summary><b>v4</b></summary>

<img width="731" height="930" alt="image" src="https://github.com/user-attachments/assets/2212d4c9-3e7e-44f1-87c7-0b1ecdf9a943" />

</details>

<br><br>

---
## 💻 와이어프레임

<!-- 수정 후 최종본 넣기 -->
<img width="1475" height="972" alt="image" src="https://github.com/user-attachments/assets/ff4b54e0-f2fb-43bc-a641-cf5c04db3583" />

[와이어프레임 링크](https://www.figma.com/design/y462Rj5dJu5KCmmVD1cFgx/%EC%8B%B9%EC%93%B0%EB%A6%AC?node-id=0-1&p=f&t=JjvgP7QshvAVdOns-0)
<br><br>

---
## 📑 ERD
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/3aa914dd-52d3-4a34-a982-dd75397e6ce5" />

[ERD 링크](https://www.erdcloud.com/d/733xaDF6icfwc8eJY) 


<br><br>

---
## 📝 API 명세서
<!-- 여기에 넣기 -->

[API 명세서 보러가기](https://www.notion.so/teamsparta/API-3102dc3ef5148000b60ec1913961ee5d)

<br><br>



---
## 🛠 기술적 의사 결정

<br>

<details>
<summary><b>✨ Session vs JWT 인증 방식 의사 결정</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

인증 시스템을 구현할 때 서비스 초기 단계에서 빠르고 안정적인 구현에 집중할 것인지, 아니면 서비스의 성장 이후를 대비한 수평성 확장성을 미리 확보할 것 인지에 대한 의사 결정이 필요했습니다.

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

- **Session 방식**: 단일 서버 환경에서 구현이 단순하고 보안 제어가 직관적이지만, 서버 메모리에 세션 정보를 저장하는 방식은 향후 사용자가 늘어남에 따라 메모리 부족의 위험성을 키우고, 수평 확장(Scale-Out) 상황에서 세션 불일치 문제를 발생 시킨다는 점을 경계했습니다.
- **JWT**: 서버의 메모리 자원을 소모하지 않는 Stateless한 특성을 가진 JWT는 수평 확장(Scale-out) 시 별도의 세션 클러스터링이나 공유 저장소 구축 없이도 대응 가능하다는 점을 고려했습니다.

✅ 최종적으로 당장의 편의성보다는 미래의 확장성과 플랫폼 간의 유연한 연동을 보장하는 JWT 기반 인증 방식을 도입하기로 결정했습니다.

<br>

<b>💡 고려한 대안</b>

- **세션 기반 인증**
- 구현이 쉽고 보안 상 즉시 만료가 가능하다는 장점이 있으나, 서버 메모리의 부담과 향후 분산 환경에서의 인프라 복잡도 증가 우려로 제외했습니다.

<br>

<b>✨ 해결 과정</b>

1. `OncePerRequestFilter` 를 상속 받은 `JwtFilter` 를 구현하여 매 요청마다 토큰의 유효성을 검증하도록 설계했습니다.
2. `SecurityConfig` 에서 인증이 필요한 경로와 필요하지 않은 경로를 분리하고, Stateless 세션 정책을 명시하여 JWT의 취지에 맞게 설정했습니다.

<br>

<b>📝 회고</b>

**느낀점**

- Session과 JWT 인증 방식 사이에서 고민하면서, 기술적 의사 결정에는 정답이 없고 언제나 현재 서비스의 상황을 파악하여 트레이드 오프를 고려해야 한다는 것을 다시 한 번 깨달았습니다.

**향후 고도화**

- 서버가 사용자를 기억하지 않는 Stateless한 JWT를 도입하면서 서버 부담을 줄이고 확장성이라는 이점을 챙겼지만, 토큰 탈취 시 제어권이 없다는 또 다른 문제가 있었습니다. 이를 해결하기 위해 추후에 **Refresh Token**과 **Black List** 전략 도입의 필요성을 느꼈습니다.

<br>
</details>

<details>
<summary><b>✨ OAuth 2.0 도입 배경</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

사용자가 느끼는 회원가입에 대한 심리적 장벽을 낮추고, 검증된 플랫폼의 보안 인프라를 활용하여 개인정보 관리 리스크를 줄이고자 했습니다. 

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

  - **외부 API 의존성**: 카카오/구글 등 외부 플랫폼의 장애 시 인증 시스템이 마비될 수 있으나, 자체 회원가입 시스템과 병행으로 운영함으로써 리스크를 최소화
  - **사용자 편의성**: 복잡한 가입 절차를 생략하여 유입률 증가

<br>

<b>💡 고려한 대안</b>

- **카카오 vs 구글**
  - 국내 사용자 타겟팅이 우선이였기 때문에 접근성이 가장 높은 카카오를 1순위로 채택했습니다.

<br>

<b>✨ 해결 과정</b>

글로벌 인증 표준인 OAuth 2.0 프레임워크를 준수하여 카카오 소셜 로그인을 구현했습니다.

클라이언트로부터 전달 받은 Authorization Code를 서버에서 직접 카카오 인증 서버의 Access Token으로 교환하는 Authorization Code Grant 방식을 채택하여 보안성을 강화했습니다.

카카오로부터 안전하게 획득한 사용자 정보를 바탕으로 신원을 확인하고, 우리 서비스만의 자체 JWT를 발급하여 체계적인 인증 관리 시스템을 구축했습니다.

<br>

<b>📝 회고</b>

**느낀점**

- Redirect URI를 통해 발급된 인가 코드를 서버에서 처리하도록 구현하며, 사용자의 민감한 정보를 노출하지 않고 서버 간 안전하게 권한을 획득하는 OAuth 2.0의 보안 매커니즘을 깊이 있게 이해할 수 있어서 좋았습니다.

**향후 고도화**

- 카카오 외에도 구글, 네이버 등 다양한 소셜 로그인을 인터페이스화 하여 코드 수정 없이 확장 가능한 구조로 발전시키고 싶습니다.
 
<br>
</details>

<details>
<summary><b>📆 타임딜 스케줄러 도입 배경 및 결정</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

타임딜은 정해진 시간에 정확히 열리고 닫혀야 하는 기능으로, 사용자에게 실시간으로 최신 정보를 제공해야 했습니다.

기존 방식인 요청 시점 조회 방식은 동시 접속자가 많을 경우 DB 부하가 급증하고 응답 지연 가능성이 있습니다.

따라서 트래픽 집중 구간에서도 안정적으로 동작할 수 있는 방법이 필요했습니다.

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

- **요청 시점 조회 방식**
    - 사용자가 요청할 때마다 데이터를 조회
    - 트래픽이 몰리면 처리 속도 저하 가능
- **스케줄러 기반 준비 방식**
    - 타임딜 시작 전에 데이터를 미리 준비
    - 안정적이고 빠른 응답 가능

✅ 비교 결과, **안정성과 속도 모두 확보 가능한 스케줄러 기반 방식을 선택**했습니다.

<br>

<b>💡 고려한 대안</b>

- 요청 시점 조회 → 구현은 간단하지만 트래픽 집중 시 성능 저하 위험
- 스케줄러 기반 준비 → 사전 준비를 통해 트래픽 집중에도 안정적 운영 가능

<br>

<b>✨ 실행 및 결과</b>

- 타임딜 오픈 시간에 맞춰 데이터를 미리 준비
- 준비된 데이터를 시스템에 저장 → 사용자 요청 시 즉시 제공
- 덕분에 사용자 트래픽이 집중되어도 안정적인 서비스 제공 가능

<details>
  <summary>스케줄러 코드 예시</summary>
  <img width="565" height="480" alt="image" src="https://github.com/user-attachments/assets/4074c5f3-1825-4bac-b277-258bd3423faf" />

- 스케줄러가 주기적으로 실행되어 **타임딜 상태를 자동으로 전이**
- 조회 API는 단순히 상태 읽기만 수행 → 트래픽 집중에도 안정적

</details>

<br>

<b>📝 회고</b>

타임딜에 스케줄러를 적용하면서 요청 중심 설계와 시간 기반 상태 전이 설계의 차이를 이해하게 되었습니다.

특히 트래픽이 몰리는 기능에서는 상태 전이를 분리하는 것이 안정성에 중요하다는 것을 새롭게 배웠습니다.

<br>
</details>

<details>
<summary><b>📈 WebSocket STOMP 도입 배경</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

- ssak3 프로젝트는 사용자와 관리자 간의 1:1 문의 채팅 기능을 제공하고 있습니다. 
- 초기에는 순수 WebSocket으로 채팅 기능을 구현했으나 직접 모든 기능을 구현하려다 보니 구현난이도 상승 및 여러 가지의 문제 상황이 발생하게 되었습니다.
- 특히 Websocket만으로 구현했을 때에는 단순 채팅 기능만 구현하여 큰 문제가 없었지만 고도화를 진행하면서 개선의 필요성을 느꼈습니다.
<details>
    <summary>기존 코드</summary>
    <img width="857" height="520" alt="image" src="https://github.com/user-attachments/assets/bbd0361a-3542-4912-8627-4f1b15d56762" />
</details>

- **복잡한 메시지 라우팅**
- Handler 내에서 메시지 타입(ENTER, TALK, QUIT)별로 수동 분기 처리
- **세션 관리의 어려움**
    - 사용자 연결 정보를 직접 관리하고 추적해야 하는 복잡도
- **인증/인가 처리의 부재**
    - JWT 토큰 검증을 위한 별도 로직 구현 필요
- **연결 안정성**
    - 연결 끊김 감지 및 재연결 로직을 직접 구현해야 함

✅ 따라서 프로젝트가 확장되면서 더 체계적이고 유지보수 가능한 구조가 필요하다고 판단했습니다.

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

- **관심사의 분리**
    - 네트워크 연결 관리와 채팅 비즈니스 로직이 한 곳에 섞여있는 기존 구조를 개선하여 로직 자체에만 집중할 수 있는 환경을 만들고자 했습니다.
- **유지보수 효율성**
    - 엔드포인트가 늘어날 때마다 `switch-case` 문이 무한정 길어지는 구조는 코드 작성 중 오류가 발생할 가능성이 높다고 판단되어 선언적 방식의 도입을 고려했습니다.
- **표준 규격의 도입**
    - 클라이언트와 서버가 서로 다른 방식으로 파싱 로직을 구현하는 대신 표준 프로토콜을 사용해 통신 규격을 통일하고자 했습니다.

<br>

<b>💡 고려한 대안</b>

**순수 WebSocket  vs STOMP**

- **순수 WebSocket**
    
    **장점**
    
    - 완전한 프로토콜 설계 자유도
    - 최소한의 오버헤드로 빠른 응답 속도
    - 추가 라이브러리 불필요
    
    **단점**
    
    - 모든 메시지 파싱 및 라우팅 로직을 직접 구현
    - 세션 관리, 인증, 재연결 등 인프라 코드의 복잡도 증가
    - Redis 등 외부 시스템과 연동 시 모든 통신 프로토콜을 직접 구현
    - 코드 가독성 및 유지보수성 저하

- **STOMP**
    
    **장점**
    
    - **구조화된 메시지 형식**: COMMAND, headers, body로 명확한 메시지 구조
    - **자동 라우팅**: `@MessageMapping`을 통한 선언적 메시지 처리
    - **간편한 인증 처리**: `ChannelInterceptor`를 활용한 메시지 레벨 JWT 검증
    - **외부 브로커 연동 용이**: Spring 설정만으로 Redis 등과 즉시 연결 가능
    - **내장 HeartBeat**: 연결 상태 자동 모니터링 및 끊김 감지
    
    **단점**
    
    - 프레임 헤더로 인한 미세한 오버헤드 (채팅 서비스에서는 무시 가능)
    - STOMP 프로토콜 규격 준수 필요
    - 프론트엔드에서 stomp.js/sock.js 라이브러리 학습 필요
 
  
    **STOMP 사용 결정 이유**
    
    - **개발 생산성**
        - 정형화된 구조로 개발 시간 단축
    - **확장성 대비**
        - 향후 분산 서버 환경에서 외부 브로커(Redis) 연동이 필수인데, STOMP는 설정 변경만으로 간편하게 연동 가능
    - **보안 강화**
        - `ChannelInterceptor`를 통한 메시지 단위 JWT 검증으로 접근 차단
    - **운영 안정성**
        - HeartBeat를 통한 자동 연결 관리로 장애 대응 용이
    - **적합한 성능**
        - 채팅은 게임처럼 ms 단위 응답이 필요 없으므로 STOMP의 약간의 오버헤드는 허용 가능

<br>

<b>✨ 해결 과정</b>

**개선 과정**
<details>
  <summary><b>기존의 길고 복잡했던 Handler 코드</b></summary>


```java
      
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class InquiryChatHandler extends TextWebSocketHandler {
    
        private final ObjectMapper objectMapper = new ObjectMapper();
        private final InquiryChatService inquiryChatService;
        private static Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();  // 방 번호, 세선 정보
    
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    
            String payload = message.getPayload(); // 전송된 메시지 문자열로 가져옴
    
            ChatMessageRequest request = objectMapper.readValue(payload, ChatMessageRequest.class);  // JSON 변환
    
            Long roomId = request.getRoomId();  // 채팅방 입장
    
            // 타입별 분리
            switch (request.getType()) {
                case ENTER:
                    // 방 세션 관리 및 입장
                    handleEnterRoom(roomId, session);
                    break;
    
                case SEND:
                    // DB 저장 및 브로드캐스팅
                    handleSendMessage(roomId, request);
                    break;
    
                case QUIT:
                    // 퇴장 알림 및 세션 정리
                    handleQuitRoom(roomId, request, session);
                    break;
            }
        }
    
        // ENTER: 세션 리스트에 추가
        private void handleEnterRoom(Long roomId, WebSocketSession session) {
    
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
    
            // 방이 없는 경우 생성
            if (sessions == null) {
                sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
                roomSessions.put(roomId, sessions);
            }
    
            sessions.add(session);
    
             System.out.println("1:1 상담이 시작되었습니다.");
        }
    
        // SEND: DB 저장 후 방에 있는 모든 세션에게 메시지 전송
        private void handleSendMessage(Long roomId, ChatMessageRequest request) throws Exception {
    
            inquiryChatService.saveMessage(request); // DB 저장
    
            // 메시지 전달
            String output = request.getSenderRole() + ": " + request.getContent();
    
            broadcastToRoom(roomId, output);
        }
    
        // QUIT: 나간 세션 제거 및 채팅방 삭제
        private void handleQuitRoom(Long roomId, ChatMessageRequest request, WebSocketSession session) throws Exception {
    
            // 해당 세션 제거
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
    
                // 관리자 / 회원의 상담 종료 알림
                broadcastToRoom(roomId, request.getSenderRole() + "님이 상담을 종료하셨습니다.");
    
                // 방 제거
                if (sessions.isEmpty()) {
                    roomSessions.remove(roomId);
                }
            }
    
            // 웹소켓 연결 끊기
            if (session.isOpen()) {
                session.close();
            }
    
            System.out.println(roomId + "번 방 상담 종료 및 세션 정리 완료");
        }
    
        // 방에 있는 모든 세션에게 메시지 발송하는 메서드
        private void broadcastToRoom(Long roomId, String message) throws Exception {
    
            Set<WebSocketSession> sessions = roomSessions.get(roomId);
    
            if (sessions != null) {
                TextMessage textMessage = new TextMessage(message);
                for (WebSocketSession s : sessions) {
                    if (s.isOpen()) {
                        s.sendMessage(textMessage);
                    }
                }
            }
        }
    
        // 세션의 연결이 끊겼을 때 리스트에서 삭제 + 방 없애기
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    
            // 나간 세션들 지워주기
            for (Set<WebSocketSession> sessions : roomSessions.values()) {
                sessions.remove(session);
            }
    
            // 방이 빌 경우(모든 세션이 나간 경우) 방 자체를 삭제하기
            roomSessions.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    
            System.out.println("연결 종료: " + session.getId());
        }
    }
```
    
</details>
    
<details>
  <summary><b>STOMP를 적용한 코드</b></summary>

- WebSocketConfig
    ```java

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // 1. 처음 소켓 연결할 엔드포인트 설정
            registry.addEndpoint("/ssak3/stomp/chat")  // STOMP 전용 주소 사용
                    .setAllowedOrigins("*");  // 모든 도메인 허용(테스트용, 추후 수정 예정)
        }
    
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            // 2. 메시지를 보낼 때 Publish 설정
            // 클라이언트가 메시지를 보낼 때 /pub으로 시작하면 @MessageMapping이 가로챔
            registry.setApplicationDestinationPrefixes("/pub");
    
            // 3. 메시지를 받을 때 Subscribe 설정
            // 클라이언트가 /sub 주소를 구독하고 있으면 서버가 메시지를 해당 주소로 보내줌
            registry.enableSimpleBroker("/sub");  // 추후에 Redis로 확장
        }
    ```

- InquiryChatStompController

  
    ```java

        /**
         * 채팅 메시지 전송 API InquiryChatStompController
         */
        @MessageMapping("/chat/message")
        public void sendMessage(@Payload ChatMessageRequest request, StompHeaderAccessor accessor) {
            // StompHeaderAccessor: 웹소켓 메시지 안에 숨겨진 세션 정보, 인증 정보, 헤더 값들을 읽거나 수정
    
            // 핸들러의 세션에서 인증된 유저 정보 꺼내기
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            Long userId = (Long) sessionAttributes.get("userId");
            String role = (String) sessionAttributes.get("role");
    
            log.info("메시지 받음 - userId: {}, roomId: {}, content: {}", userId, request.getRoomId(), request.getContent());
    
            // Redis 먼저 발행
            ChatMessageResponse tempResponse = ChatMessageResponse.fromRequest(request, userId, role);
    
            log.info("Redis 발행 - roomId: {}, content: {}", tempResponse.getRoomId(), tempResponse.getContent());
    
            redisTemplate.convertAndSend(chatTopic.getTopic(), tempResponse);
    
            // DB 저장은 비동기로 처리
            inquiryChatService.saveMessageAsync(request, userId, role);
        }
    
        /**
         * 문의 채팅방 입장, 퇴장 알림 API
         */
        @MessageMapping("/chat/notice")
        public void sendNotice(@Payload ChatMessageRequest request) {
    
            // 채팅방 입장, 퇴장 시 알림으로 보내 줄 메시지
            String message = (request.getType() == ChatMessageType.ENTER)
                    ? request.getSenderRole() + "님이 입장했습니다."
                    : request.getSenderRole() + "님이 문의를 종료했습니다.";
    
            ChatMessageResponse response = ChatMessageResponse.from(request, message);
    
            redisTemplate.convertAndSend(chatTopic.getTopic(), response);  //
        }
    ```
</details>
   
    
- **STOMP 도입을 통한 채팅의 REST화:** 기존 `TextWebSocketHandler`에서 수동으로 `switch-case`문을 통해 메시지 타입을 구분하던 로직을 없앴습니다.


<img width="971" height="152" alt="image" src="https://github.com/user-attachments/assets/c05d59b4-91ad-4bc5-92be-5df118938778" />

- 대신 @MessageMapping을 도입하여, 마치 REST API의 Controller처럼 경로(Path)를 기반으로 비즈니스 로직을 분리하여 코드 가독성과 유지보수성을 확보했습니다.

<br>

<b>📝 회고</b>

**느낀점**

- 웹소켓 핸들러에서 직접 세션을 관리할 때 발생하던 동기화 문제나 복잡한 분기문을 STOMP와 `@MessageMapping`으로 해결하며, 잘 설계된 프로토콜이 코드 가독성과 생산성에 얼마나 큰 영향을 주는지 체감했습니다.
- 서비스의 목적에 따라 **약간의 오버헤드를 감수하더라도 유지보수성과 확장성(Redis 연동 등)을 챙기는 것이 더 전략적인 선택**이 될 수 있음을 배웠습니다. 결과적으로 REST API처럼 직관적인 구조로 코드를 만들 수 있었습니다.

**향후 고도화**

- `@MessageExceptionHandler`를 도입하여 STOMP 통신 중 발생하는 에러를 클라이언트에게 규격화된 형태로 전달하는 기능을 추가하고 싶습니다.
- 채팅에서 읽음 처리, 알림 기능을 추가하고 싶습니다.

<br>
</details>

<details>
<summary><b>📩 Redis pub/sub 도입 배경</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

<details>
  <summary>1차 배포 아키텍쳐 설계도</summary>

  <img width="492" height="517" alt="image" src="https://github.com/user-attachments/assets/549f13fb-03ac-4c2f-892e-411dda91d54c" />
</details>

*위 설계도는 ssak3 프로젝트의 1차 배포 아키텍처 설계도입니다.* 

해당 아키텍처를 보면 애플리케이션 서버 안에 Redis를 함께 구동하고 있음을 알 수 있습니다.

프로젝트 고도화 과정에서 User 서버와 Admin 서버로 분할하기로 결정했습니다.

- 분할 이유
    - 사용자 트래픽 폭증 시 관리자 서비스는 정상 운영되어야 함
    - 각 서버의 독립적인 스케일링 및 장애 격리

**발생한 문제**

기존의 단일 서버 환경에서는 Spring의 **SimpleBroker**만으로 충분했습니다. 

하지만 서버가 분할될 경우 User 서버에 연결된 사용자 A와 Admin 서버에 연결된 관리자 B는 서로 다른 메모리 공간에 세션이 존재합니다. SimpleBroker는 자신의 서버 내부에만 메시지를 전달하므로 **서버 간 메시지 공유가 불가합니다.**

따라서 채팅방에서 한쪽이 보낸 메시지를 상대방이 받지 못하는 문제가 발생하게 되었고 이를 해결할 새로운 방안이 필요하게 되었습니다.

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

- **메시지 브로커 도입**
    - 서버가 여러 대일 경우, A 서버에 접속한 사용자가 보낸 메시지를 B 서버에 접속한 사용자가 받지 못하는 '**데이터 파편화**' 문제를 해결하기 위해 서버 간 메시지를 공유할 매개체가 필요했습니다.

<br>

<b>💡 고려한 대안</b>

**전문 메시지 브로커 vs  Redis Pub/Sub**

- **전문 메시지 브로커 (RabbitMQ, Kafka)**
    
    **장점**
    
    - 메시지 영속성 보장 (디스크 저장)
    - 복잡한 메시지 라우팅 및 큐 관리 기능
    - 대규모 메시지 처리 및 순서 보장
    
    **단점**
    
    - 별도 서버 구축 및 운영 비용
    - 과도한 기능으로 인한 복잡도 증가
    - 문의 채팅과 같은 간단한 Pub/Sub에 사용되기에는 오버스펙으로 볼 수 있음
    - 프로젝트 규모 대비 러닝 커브 높음
    
- **Redis Pub/Sub**
    
    **장점**
    
    - 이미 캐시 용도로 Redis 사용 중 (추가 인프라 불필요)
    - 극도로 빠른 메시지 전달 속도 (in-memory)
    - 간단한 Pub/Sub 구조로 학습 비용 최소화
    - Spring STOMP와 즉시 연동 가능 (설정만으로 완료)
    
    **단점**
    
    - **메시지 비영속성**: 전달 후 즉시 소멸 (브로커가 아닌 메시지 버스)
    - 구독자가 없으면 메시지 유실
    - 복잡한 라우팅이나 재처리 불가

**Redis pub/sub으로 결정한 이유**

- **실시간성 우선**
    - 채팅은 즉시성이 가장 중요하며, Redis의 in-memory 처리는 밀리초 단위 응답 보장
- **인프라 효율**
    - 기존 Redis 활용으로 추가 서버 불필요
- **프로젝트 적합성**
    - 1:1 채팅은 복잡한 메시지 큐 기능이 필요 없음
- **간단한 연동**
    - Spring 설정에서 `SimpleBroker` → `RedisMessageBroker`로 변경만으로 완료

<br>

<b>✨ 해결 과정</b>

- **Redis Pub/Sub을 활용한 메시지 브로커 확장**
    - 내장 브로커(SimpleBroker)의 단점인 서버 간 메시지 미공유 문제를 해결하기 위해 **Redis를 메시지 중간 매개체**로 도입했습니다.
    - `RedisMessageListenerContainer`를 통해 분산된 서버 환경(User/Admin)에서도 실시간 메시지 동기화를 구현했습니다.
    
- **성능 최적화**
  <img width="977" height="188" alt="image" src="https://github.com/user-attachments/assets/d84ae974-f369-4006-8d01-e1f4b1a58211" />
    - 메시지 수신 시 Redis 발행을 우선 처리하여 실시간성을 확보하고, 상대적으로 무거운 DB 저장은 비동기(saveMessageAsync)로 처리하여 사용자 응답 지연을 최소화했습니다.
 
<details> 
<summary>Redis 적용한 코드</summary>

  - RedisConfig
    ```java
        // Redis로부터 오는 메시지를 구독하는 컨테이너
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic("chat"));   // "chat"을 구독하도록 설정
        return container;
    }

    // 실제 메시지를 처리할 구독 설정
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");  // Redis에서 응답이 오면 onMessage 실행
    }
    ```

- InquiryChatStompController
```java
      
    /**
     * 채팅 메시지 전송 API 
     */
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequest request, StompHeaderAccessor accessor) {
        // StompHeaderAccessor: 웹소켓 메시지 안에 숨겨진 세션 정보, 인증 정보, 헤더 값들을 읽거나 수정

        // 핸들러의 세션에서 인증된 유저 정보 꺼내기
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long userId = (Long) sessionAttributes.get("userId");
        String role = (String) sessionAttributes.get("role");

        // Redis 먼저 발행
        ChatMessageResponse tempResponse = ChatMessageResponse.fromRequest(request, userId, role);

        redisTemplate.convertAndSend(chatTopic.getTopic(), tempResponse);

        // DB 저장은 비동기로 처리
        inquiryChatService.saveMessageAsync(request, userId, role);
    }
    
```

</details>

<br>

<b>📝 회고</b>

**느낀점**

- 단일 서버에서 다중 서버(User/Admin 서버 분리)로 인프라 구조를 확장하면서 발생하는 **'세션 불일치 및 메시지 파편화'** 문제를 기술적으로 해결해볼 수 있었습니다.
- 가장 큰 소득은 단순히 기능 구현에 그치지 않고, **확장성**있는 시스템 설계의 중요성을 체감했다는 점입니다. 초기 설계 당시에는 SimpleBroker로 충분하였으나, 서버 분리 후 메시지가 사라지는 현상을 목격하며 분산 환경에서의 상태 관리에 대해 고민하게 되었습니다.
- 새로운 기술을 무조건 도입하기보다 **제작하고자 하는 기능을 파악하여 적절한 방안을 찾고** **현재 우리 프로젝트가 보유한 자원(이미 사용 중인 Redis)을 최대한 활용**하여 인프라 복잡도를 낮추면서도 목표를 달성하는 의사결정 능력을 기를 수 있었습니다.

**향후 고도화**

- **분산 세션 관리 통합:** 현재 각 서버가 개별적으로 관리하는 웹소켓 세션 정보를 Redis로 통합 관리하여, 어떤 서버에 접속하더라도 사용자의 실시간 접속 상태를 정확히 파악할 수 있는 관리 서버를 추가하고 싶습니다.

<br>
</details>

<details>
<summary><b>👷 BastionHost 도입 배경</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

<details>
  <summary>1차 배포 아키텍쳐 설계도</summary>

  <img width="492" height="517" alt="image" src="https://github.com/user-attachments/assets/2e05a706-6bc2-4b9c-a2d9-b08dd6d6f397" />
</details>

*위 설계도는 ssak3 프로젝트의 1차 배포 아키텍처 설계도입니다.* 

- 해당 아키텍처를 보면 데이터베이스와 애플리케이션 서버가 **public subnet에 위치하고 있어 보안 위협에 그대로 노출**되고 있다는 문제가 있었습니다. 

- 서비스의 안전성을 위해 **내부 자원들을 격리하면서도 개발자가 안전하게 서버를 관리할 수 있는 전용 통로가 필요**했습니다.

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

- **공격 표면의 최소화**
    - 모든 인스턴스에 공인 IP를 할당하면 관리 포인트가 늘어나고 공격 노출 범위가 넓어지므로, 외부 노출 창구를 하나로 제한한다는 원칙을 세웠습니다.
- **네트워크 계층화**
    - 서비스 로직이 돌아가는 애플리케이션 서버와 중요한 데이터가 담긴 DB 서버는 Private Subnet에 위치시키는 것으로 설계 방향성을 정했습니다.
- **접근 통제**
    - 서버를 직접적으로 제어하는 관리자만 특정 경로로 접근하도록 설계하고자 하였습니다.

<br>

<b>💡 고려한 대안</b>

**외부 포트 개방 vs Bastion Host**

- **외부 포트 개방(v1)**
    - 보안 그룹 설정만으로 소프트웨어적 접근 제어는 가능하지만 보안 위협에 노출됨.
- **Bastion Host(v2 이후)**
    - 모든 priavate 서버 접근을 Bastion을 통과해야만 접속 가능하도록 통제하여 보안성을 높일 수 있음.

**보안그룹 설정만으로 부족한 이유?**

Public Subnet에 있는 서버는 Public IP를 가지는데 이는 인터넷 어디서든 그 서버의 주소로 패킷을 보낼 수 있다는 것을 의미합니다. 보안그룹 설정을 잘못 할 경우 즉시 해킹 위험에 노출됩니다.

반면에 Private Subnet에 있는 서버는 Public IP 자체가 없으므로 이러한 위험에서 벗어날 수 있습니다. 설정에 문제가 생기더라도 물리적으로 2중 보안이 가능합니다.

<br>

<b>✨ 해결 과정</b>
<details>
  <summary>2차 배포 아키텍처 설계도</summary>
  
  <img width="632" height="711" alt="image" src="https://github.com/user-attachments/assets/1a9bded5-32ac-41d3-944a-a786a95b8289" />

</details>

*위 설계도는 ssak3 프로젝트의 2차 배포 아키텍처 설계도입니다.*  

- v1에서 public subnet만 있었던 것과 달리 public subnet과 private subnet으로 분리되었습니다.

- 서버 관리자의 경우 Ec2나 RDS에 직접적으로 접속해야하므로 Bastion Host를 이용해 접근 제어를 하도록 하였습니다.

**개선 내용**

- **Bastion Host 구축**
    - AWS EC2를 활용해 퍼블릭 서브넷에 Bastion Host를 배치했습니다.
- **SSH 터널링 적용**
    - 실제 서비스 서버는 프라이빗 서브넷에 위치시키고, 오직 Bastion Host의 특정 IP로부터 오는 SSH 요청만 수용하도록 보안 그룹을 강화했습니다.

<br>

<b>📝 회고</b>

**느낀점**

- 이번 아키텍처 개선을 통해 인프라 설계에서 **'편의성'과 '보안' 사이의 적절한 균형**을 잡는 법을 배웠습니다. 초기 수동 배포와 퍼블릭 설정은 구현 속도는 빨랐으나, 서비스가 커질수록 보안 사고의 위험이 늘어난다는 점을 배웠습니다.
- 단순히 보안 그룹 설정에만 의존하지 않고, 네트워크 레벨에서 외부 노출을 차단하는 계층적 구조를 설계함으로써, **설정 오류가 발생하더라도 시스템 전체가 무너지는 것을 방지하는 안정성을 확보**할 수 있었습니다. 이를 통해 코드 작성만큼이나 안전한 실행 환경을 구축하는 것이 얼마나 중요한지 깨달았습니다.

**향후 고도화**

- **Audit Log 관리**
    - 현재는 Bastion Host를 통해 접속이 가능하지만, 향후 관리자가 늘어날 것에 대비하여 접속 후 실행한 명령어를 기록하는 로깅 시스템을 구축해보고 싶습니다.
- **AWS Systems Manager(SSM) 도입**
    - 현재는 SSH 키를 사용한 방식을 사용하고 있지만 이 방식도 키가 유출 및 분실 될 경우의 위험성을 인지하고 있습니다. 이러한 위험을 없애기 위해, 포트 개방 없이 브라우저 기반으로 안전하게 인스턴스에 접속할 수 있는 SSM Session Manager로 전환하는 고도화 방안을 고려하고 있습니다.

<br>
</details>

<details>
<summary><b>📈 ELK 로그 모니터링 도입 배경</b></summary>

<br>
  
<b>⁉️ 의사 결정 발생 배경</b>

단일 서버에서 분산 서버 아키텍처로 수평 확장(Scale-out) 하게 되면서 각 서버에 흩어진 로그를 확인하기 위해 매번 개별 서버로 접속해야 하는 번거로움이 발생했고, 전체 시스템의 흐름을 한 눈에 파악하기 어려워졌습니다.

장애 발생 시 원인 파악이 지연되는 문제를 해결하고자 **로그 중앙 집중화**가 필요한 시점이라고 생각했습니다.

<br>

<b>🙋‍♀️ 의사 결정 과정</b>

단순히 로그를 수집하는 것 뿐만 아니라, 실시간으로 쏟아지는 로그를 검색하고 시각화 할 수 있는 인프라가 필요했습니다.

인프라를 설계하며 중점을 둔 가치는 본연의 서비스 성능을 해치치 않으면서 신뢰할 수 있는 데이터 파이프라인을 구축하는 것이었습니다.

<br>

<b>💡 고려한 대안</b>

**TCP 전송 방식 vs Filebeat 방식**

<aside>

✅ **TCP 전송 방식 (직접 전송)**

**장점**

별도의 에이전트 설치 없이 Logback 설정 만으로 구현 가능하여 초기 구축이 빠름

**단점**

Logstash나 네트워크에 문제가 생길 경우, 애플리케이션 서버에서 로그를 보내지 못해 대기가 발생하거나 로그가 유실될 위험이 있음

</aside>

<aside>

✅ **Filebeat 방식**

**장점**

로그 생성과 전송을 분리하여 애플리케이션 서버 장애와 상관 없이 로그 파일 보존

**단점**

서버마다 Filebeat라는 별도 프로세스를 관리해야 하는 운영 부담이 늘어남

</aside>

✅ Filebeat를 선택하여 애플리케이션은 로그 작성에만 집중하고, 별도의 경량 에이전트가 로그를 전송하는 구조를 지향하여 데이터 유실 방지와 서비스 안정성을 챙기기로 결정

<br>

<b>✨ 해결 과정</b>
분산된 서버의 로그를 안정적으로 수집하기 위해 분산 서버 환경에서 컨테이너 기반의 인프라를 다음과 같이 구축했습니다.

- 각 User 서버, Admin 서버 내부에 Docker Compose를 활용하여 Filebeat 컨테이너를 구동
- 로그 처리에 따른 부하가 메인 서비스 서버에 영향을 주지 않도록 별도의 EC2 인스턴스를 할당하고 Docker Compose를 통해 ELK 스택 구축, 데이터 보존을 위해 도커 볼륨 활용
- `logback-spring.xml` 설정을 통해 로그를 JSON 포맷으로 표준화

<br>

<b>📝 회고</b>

**느낀점**

- 분산 서버 환경에서 발생하는 로그 파편화 문제를 해결하기 위해 ELK 스택과 Filebeat를 도입했습니다. 이 과정을 통해 로그 생성과 수집, 가공 단계를 분리하여 관심사를 독립적으로 운영하는 구조를 익힐 수 있었습니다.

**향후 고도화**

- 향후에는 특정 임계치를 넘어서는 에러 발생 시 Slack이나 이메일로 즉시 알림을 보내는 실시간 대응 체계를 구축하고 싶습니다. 이를 통해 장애 인지 시간을 줄이는 방향으로 고도화 할 예정입니다.

<br>
</details>

<br>

---
## 🚩 트러블 슈팅

<br>

<details>
<summary><b>✨ 인기 조회수 TOP 10 조회 시 발생한 N+1 문제</b></summary>

<br>

<b>⚠️ 문제 상황</b>
<img width="1024" height="141" alt="image" src="https://github.com/user-attachments/assets/8d1ea9ab-b4b0-41a5-bc4a-81bbf8c690b2" />


인기 조회수 TOP 10 상품을 조회할 때, 상품 정보를 조회하는 쿼리는 한 번만 실행되는데 타임딜 정보를 조회하는 쿼리 10개가 추가로 실행되는 문제가 발생했습니다.

<br>

<b>🙋‍♀️ 문제 발생 원인</b>

<img width="758" height="246" alt="image" src="https://github.com/user-attachments/assets/90f86a22-291b-4e72-9ef8-ae1129b43e5d" />


`for` 문에서 상품 id 개수만큼 반복문을 돌면서 `TimeDealRepository`의 `findByProductId(productId)`를 호출하는 게 원인이었습니다.

Redis에서 인기 조회수 상품 ID를 10개를 획득한 후, 반복문을 돌며 각 상품에 대한 타임딜 정보를 DB에서 조회하고 있었습니다.

결과적으로 로직 상의 문제로 ID 목록 1번 + 타임딜 정보 N번의 문제가 발생하게 되었습니다.

<br>

<b>✨ 해결 과정</b>

- **해결 방법**
  <img width="1024" height="517" alt="image" src="https://github.com/user-attachments/assets/dbc05113-c8c1-4f8b-ae5a-84eec59db8db" />

  1. Redis가 정렬해준 데이터를 이용해 조회수 TOP 10 상품 id 목록을 가져옵니다.
  2. N+1 문제를 방지하기 위해, 10개의 상품 id를 한 번에 넘겨 상품 정보와 타임딜 정보를 통째로 가져옵니다.
  3. 가져온 데이터들을 상품 id를 Key로 하는 Map 구조로 변환하여, `for` 문 안에서 반복적인 `TimeDealRepository` 접근 없이 타임딜 정보를 즉시 가져왔습니다.

- **해결 후**
<img width="1024" height="29" alt="image" src="https://github.com/user-attachments/assets/c7164006-2065-470d-b140-41e8ba258ce5" />

<br><br>

<b>📝 회고</b>

**느낀점**

- 아무리 빠른 인 메모리 저장소를 사용하더라도, 뒤따르는 비즈니스 로직이 비효율적이라면 전체 성능이 저하된다는 것을 몸소 느꼈습니다.
- 코드를 작성할 때 단순히 기능의 동작 여부만 확인할 것이 아니라, 해당 코드가 실제 SQL로 어떻게 변환되어 실행될 것 인지에 대해 계속해서 고민하고 신경 써야 한다는 것을 깨달았습니다.

</details>

<details>
<summary><b>✨ 사용자 식별 정보가 누락된 채 기록되는 문제</b></summary>

<br>

<b>⚠️ 문제 상황</b>
<img width="1024" height="45" alt="image" src="https://github.com/user-attachments/assets/7b93482a-5d03-42ee-830c-02c4d9a41bf4" />


- LoggingFilter에서 인증된 사용자의 요청임에도 불구하고 USER_ID가 계속 null로 찍히는 문제가 발생했습니다.

<br>

<b>🙋‍♀️ 문제 발생 원인</b>
<details>
<summary>LoggingFilter.java</summary>

```java

package com.example.ssak3.common.filter;

import com.example.ssak3.common.model.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
* HTTP 요청에 대한 로그 담당
  */
  @Slf4j
  @Component
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public class LoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       // 실행 시작 시간
       long startTime = System.currentTimeMillis();

       // API 요청 정보
       String url = request.getRequestURI();
       String method = request.getMethod();
       String queryParams = request.getQueryString();

       // 인증 정보 꺼내기
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       Long userId = null;

       if (auth != null && auth.isAuthenticated()) {
           AuthUser user = (AuthUser) auth.getPrincipal();
           userId = user.getId();
       }

       // TraceId 생성
       String traceId = UUID.randomUUID().toString().substring(0, 8);
       
       // MDC에 저장: 이 스레드에서 찍히는 모든 로그에 traceId가 붙음
       MDC.put("traceId", traceId);

       try {
           filterChain.doFilter(request, response);
       } finally {
           int statusCode = response.getStatus();

           long endTime = System.currentTimeMillis();

           if (statusCode >= 500) {
               log.error("USER_ID: {} | STATUS_CODE: {} | URL: {} | METHOD: {} | QUERY_PARAMS: {} | EXECUTION_TIME: {} ms",
                       userId, statusCode, url, method, queryParams, endTime - startTime);
           } else if (statusCode >= 400) {
               log.warn("USER_ID: {} | STATUS_CODE: {} | URL: {} | METHOD: {} | QUERY_PARAMS: {} | EXECUTION_TIME: {} ms",
                       userId, statusCode, url, method, queryParams, endTime - startTime);
           }

           // 스레드 풀을 재사용하기 때문에 작업이 끝나면 MDC를 반드시 비워야 함
           MDC.clear();
       }
  }
  }

```

</details>

<img width="467" height="85" alt="image" src="https://github.com/user-attachments/assets/933d4c42-2b8e-43f4-bcab-7acb29d35d69" />


원인은 Filter Chain의 우선 순위 문제였습니다.

`@Order(Ordered.HIGHEST_PRECEDENCE)` 어노테이션으로 인해 해당 로깅 필터가 최앞단에 위치하게 되었고, 사용자의 인증 정보를 확인하여 `SecurityContextHolder`에 넣어주는 `JwtFilter`보다 `LoggingFilter` 가 먼저 실행되었습니다.

그 결과, 인증 객체가 생성되기 전에 로그를 찍으려고 시도하다 보니 `SecurityContext` 에서 USER 정보를 가져오지 못해 `USER_ID`가 항상 null이 되었습니다.

<br>

<b>✨ 해결 과정</b>

<img width="1024" height="440" alt="image" src="https://github.com/user-attachments/assets/bd1bbdb4-159d-48b3-a175-4e4d5e0ec8e1" />

- LoggingFilter를 JwtFilter와 UsernamePasswordAuthenticationFilter 사이에 두어 인증 정보가 담긴 후에 처리 되게 하면서 인증 정보가 지워지기 전에 로그를 찍을 수 있도록 설정해서 해결했습니다.

<b>📝 회고</b>

**느낀점**

- 이번 경험을 통해 Spring Security Filter Chain의 상세 동작 매커니즘을 알게 되는 계기가 되었습니다. 단순한 기능 구현을 넘어서, 각 컴포넌트의 생명주기와 실행 순서를 설계 단계부터 철저히 검토해야 사이드 이펙트를 막을 수 있다는 것을 다시 한 번 인지하게 되었습니다.

<br>
</details>

<details>
<summary><b>🐛 분산 서버 환경에서 스케줄러 중복 실행 문제</b></summary>

<br>

<b>⚠️ 문제 상황</b>

타임딜 스케줄러 실행 시 일부 상품이 OPEN되지 않는 문제가 발생했습니다.

<br>

<b>🙋‍♀️ 문제 발생 원인</b>

로그 분석 결과, 분산 서버 환경에서 스케줄러가 중복 실행되며 상태 변경 충돌이 발생하고 있음을 확인했습니다.

서버를 수평 확장하면서 애플리케이션이 **각 서버에 독립적으로 실행**되고 있었습니다.
이로 인해 스케줄러 또한 서버마다 각각 등록되어 동작했습니다.

스케줄러 실행 시 **다른 서버의 실행 여부를 제어하거나 확인하는 로직이 부재**한 상태였습니다.분산 환경에 대한 고려 없이 단일 서버 기준으로 스케줄러를 설계한 것이 원인이었습니다.

아래 로그를 통해 동일한 시각에 여러 서버에서 스케줄러가 동시에 실행되고 있음을 확인할 수 있었습니다.
<img width="1024" height="184" alt="image" src="https://github.com/user-attachments/assets/06741ea8-08b4-4df6-a519-2733607b92d8" />

동일한 실행 시각에 각 서버 인스턴스에서 TimeDeal Scheduler START 로그가 출력되며, 스케줄러가 서버 수만큼 중복 실행되고 있음을 확인할 수 있습니다.

<br>

<b>💡 고려한 대안</b>

문제 해결을 위해 다음과 같은 방안을 검토했습니다.

1. **특정 서버에서만 스케줄러 실행**
    - 설정을 통해 한 서버에만 스케줄러를 활성화
    - 서버 장애 시 스케줄러가 동작하지 않을 위험이 있습니다.

2. **DB Lock 활용**
    - 스케줄러 실행 시 DB Row Lock 또는 플래그를 사용
    - 트랜잭션 관리 부담 증가 및 DB 부하 우려가 있습니다.

3. **분산 락(Distributed Lock) 적용 — ShedLock 라이브러리 활용**
    - `@SchedulerLock` 어노테이션을 사용해 분산 락을 손쉽게 구현
    - 스케줄러 실행 시 공용 저장소에 락을 획득하도록 처리
    - `lockAtMostFor`, `lockAtLeastFor` 옵션으로 락 지속 시간 제어 가능
    - 서버 장애 시에도 락 해제 가능하며, 분산 환경에 적합하고 확장성 있는 방식입니다.

여러 대안 중 **ShedLock 기반 분산 락 방식을 선택했습니다.**

<br>

<b>✨ 해결 과정</b>

- 스케줄러 메서드에 @SchedulerLock 어노테이션을 적용하여 분산 락을 획득하는 구조로 구현했습니다.

<img width="550" height="457" alt="image" src="https://github.com/user-attachments/assets/29b598fd-05d8-4895-a814-56d1322a4c40" />

- `name` 속성으로 락 이름을 지정하여 여러 스케줄러 간 락 충돌을 방지했습니다.
- `lockAtMostFor`는 락을 최대 유지하는 시간으로, 락이 장시간 해제되지 않는 상황을 방지합니다.
- `lockAtLeastFor`는 락을 최소 유지하는 시간으로, 너무 잦은 락 획득과 해제를 방지합니다.
- 락 획득에 성공한 인스턴스에서만 스케줄러 로직을 실행하며, 락 획득에 실패한 다른 인스턴스는 실행하지 않습니다.

이로써 **분산 서버 환경에서도 스케줄러가 한 번만 실행됨을 보장**할 수 있었습니다.

아래 로그는 여러 인스턴스가 동시에 기동된 환경에서, **락을 획득한 1개의 인스턴스만 스케줄러를 실행한 예시**입니다
<img width="1024" height="190" alt="image" src="https://github.com/user-attachments/assets/031fcbae-e5bd-4c03-8010-e9d5daf0a011" />

<br>

<b>📝 회고</b>

`@SchedulerLock`을 활용해 분산 환경에서 스케줄러 중복 실행 문제를 해결할 수 있었습니다.

락 유지 시간은 다음과 같이 설정했습니다.

- `lockAtMostFor = "PT1M"` : 최대 1분 동안 락을 유지하여, 서버 장애 등으로 락이 해제되지 않는 상황을 방지
- `lockAtLeastFor = "PT10S"` : 최소 10초 동안 락을 유지하여, 락 획득 후 바로 해제되는 것을 방지하고 안정적인 실행 보장

**추가 개선 방안**

- 락 획득 실패시 재시도 로직을 추가하고 싶습니다.
- 트랜잭션 실패 또는 서버 장애 시 **스케줄러 작업 복구 전략**을 마련하고 싶습니다.

<br>
</details>

<details>
<summary><b>✨ 동시 요청 환경에서 데드락 및 정합성 문제</b></summary>

<br>

<b>⚠️ 문제 상황</b>
- **기능** : 쿠폰 발급 기능 / 상품 주문 기능
- **발생 시나리오** : JMeter를 이용해 각각의 기능에 대해 100건의 동시성 테스트를 수행했습니다.
- **현상** : 테스트 중 `Deadlock found when trying to get lock` 에러가 대량 발생하며 트랜잭션이 중단되는 현상이 생겼습니다.
- **비즈니스 리스크** : 실제 운영 환경에서 시스템 마비가 발생할 수 있으며, 선착순 수량이 정확히 관리되지 않아 비즈니스 신뢰도가 하락할 수 있습니다.
<img width="1280" height="227" alt="image" src="https://github.com/user-attachments/assets/db542c03-136a-4dc0-914f-b502d584e1e1" />

<br>

<b>🙋‍♀️ 문제 발생 원인</b>

- 원인 분석
    - 작성한 코드와 실제 실행되는 쿼리의 순서가 다르다는 것을 발견
        - 작성한 코드 : select → update → insert
        - 실제 실행 순서 : select → insert → update
        <img width="1024" height="106" alt="image" src="https://github.com/user-attachments/assets/e54751ac-4ecf-4455-b352-4a816e8a5632" />

    - 영속성 컨텍스트의 쓰기 지연 때문에 순서의 변화가 생긴다
        - JPA에서는 쓰기 작업을 DB에 효율적으로 반영하기 위해 트랜잭션에서 발생한 모든 쓰기 작업을 기록해두고 한꺼번에 실행

      -> 이 과정에서 삽입 쿼리가 업데이트 쿼리보다 먼저 실행

- 핵심 원인
    - MySQL의 기본 격리 수준인 Repeatable Read에서는 단순 조회는 s-lock을 사용하지 않고 MVCC를 사용 → 초기 단계에서 여러 트랜잭션이 동시 진입
    - OrderProduct 삽입 시 정합성 보장을 위해 DB는 외래키 관계에 있는 부모테이블(Product)에 s-lock을 건다
    - 이후 상품의 재고를 차감하기 위한 update 쿼리가 실행되면서 s-lock에서 x-lock으로 승격 시도
        - Transaction(1)에서 s-lock을 잡고 x-lock으로 승격 시도 -> Transaction(2)의 s-lock이 풀리기를 대기
        - Transaction(2)에서도 s-lock을 잡고 x-lock으로 승격 시도 -> Transaction(1)의 s-lock이 풀리기를 대기
      <img width="1024" height="464" alt="image" src="https://github.com/user-attachments/assets/92ec2ea2-d73b-4be8-b4d0-6fc2069631c5" />

      <img width="1024" height="478" alt="image" src="https://github.com/user-attachments/assets/3f28ef25-5491-44ad-a14d-a29d518a90c8" />

      ===> Deadlock 발생

<br>

<b>💡 고려한 대안</b>

**방법 1) 쓰기 순서 강제 조정 (flush 활용)**

- update 후 바로 flush()를 실행하여 X-Lock을 선점하도록 했습니다.
  <img width="579" height="94" alt="image" src="https://github.com/user-attachments/assets/7619387d-2741-4fae-9c17-764b8216b35a" />

- 결과
    - 기존(insert 후 update) → 현재(update 후 insert)
  <img width="1024" height="107" alt="image" src="https://github.com/user-attachments/assets/d58f7c9f-8b87-4918-99ba-449b0721c092" />



- 데드락은 해결되었으나 **데이터 정합성 문제 발생**
    - **쿠폰 발급 기능**
        
        : 100건의 쿠폰 발급 동시성 테스트 시 동시에 데이터를 읽어가는 현상을 막지 못해 수량 업데이트가 누락되는 정합성 문제 발생했습니다.
        
        - 발급된 쿠폰 수 : 100
        - 쿠폰 테이블에서 틍정한 발급된 쿠폰 수 : 93
      <img width="708" height="303" alt="image" src="https://github.com/user-attachments/assets/b60b4bf4-e3a2-43ca-9134-e9a666666138" />

  - **상품 주문 기능**
    
      : 재고가 100인 상품에 대해 100건의 구매 동시성 테스트 시 주문 수와 재고 차감 수가 맞지 않는 문제 발생했습니다.
    
      - 주문 생성 수 : 100
      - 재고 차감 수 : 39  (100 → 61)

      
    <img width="1024" height="62" alt="image" src="https://github.com/user-attachments/assets/63621e29-eabb-44ae-aef1-d458c441086c" />

    <img width="1024" height="202" alt="image" src="https://github.com/user-attachments/assets/0e2ec6bf-dcad-4274-b3b9-b2371e49eb7f" />

**방법 2) 비관적 락(Pessimistic Lock)**

- 구현 방법 : `Repository`에 `@Lock(LockModeType.PESSIMISTIC_WRITE)`이 적용된 `findByIdWithLock`을 정의하고 서비스 레이어에서 적용하였습니다.
        
  <img width="463" height="106" alt="image" src="https://github.com/user-attachments/assets/88194280-2bb7-4afe-922f-ed166fea78ff" />

    - 결과 : **데드락과 데이터 정합성 문제가 모두 해결**되었습니다.
        - 쿠폰 발급 기능
            - 실제 발급된 쿠폰 수(100) = 쿠폰 테이블에서 측정된 발급 쿠폰 수(100)
                
              <img width="734" height="295" alt="image" src="https://github.com/user-attachments/assets/2eb2c411-b80b-4ee6-9f3e-434b93dbacd3" />
                
        - 상품 주문 기능
            - 생성된 주문 수(100) = 재고 차감 수(100)

            <img width="1280" height="83" alt="image" src="https://github.com/user-attachments/assets/9d82b995-b6c4-46f8-8802-93e43bbbd795" />
            <img width="1280" height="271" alt="image" src="https://github.com/user-attachments/assets/0be34ca2-77e5-4ffd-a42c-e331bf34cbea" />


    

<br>

<b>✨ 해결 과정</b>

- **비관적 락 선택**
    - coupon / product 조회 시점에 비관락 적용했습니다.
    - **장점**: **데이터 조회 시점부터 x-lock을 획득**하여 다른 트랜잭션의 접근을 차단하여 **데이터 정합성 보장**합니다.
    - **단점**: 순차적 처리를 하게 되어 **병목 현상 발생 가능성**이 있습니다.
    - 비관적 락은 성능은 떨어지지만, 이커머스 서비스 특성 상 쿠폰 발급 / 주문 도메인에서 **정합성이 최우선이므로 성능 손실을 감수하더라도 무결성을 보장하는 비관적 락을 선정**하였습니다.

- **해결 결과**
    - **문제 해결 여부** : 데드락 현상과 정합성 문제 모두 해결했습니다.
    - **이전과 이후 변화** :
        - **데드락**: 대량 발생 → 0건
        - **수량 정합성**: 100건 모두 정확히 반영
    - **검증 결과**: JMeter 100건 동시 요청 테스트 결과 에러율 **0.00%** 및 데이터 일치를 확인했습니다.
<img width="1969" height="275" alt="image" src="https://github.com/user-attachments/assets/aa8eff74-8026-4b90-bcc8-62ceeec9c799" />


<br><br>

<b>📝 회고</b>

**느낀 점**

- 이번 트러블 슈팅을 통해 제가 작성한 코드의 로직 흐름대로 적용되지 않을 수 있다는 것을 알게 되었고, 이 문제를 식별하는 과정에서 로그를 적극적으로 활용해볼 수 있었습니다.
- 두 가지 해결 방안을 모두 시도해 본 점과, InnoDB 모니터를 통해 데드락의 근본적인 원인을 로그 분석을 통해 검증한 과정이 효과적이었습니다.

**아쉬운 점**

- 비관적 락 적용 시 발생할 수 있는 성능 문제가 남아있습니다.

**추가 개선 방안**

- 향후 트래픽 증가 시 성능에 미치는 영향을 지속적으로 관찰하고 필요하다면 **분산락** 등 다른 동시성 제어 방식도 고려해 볼 필요가 있습니다.

<br>
</details>

<br>

---
## 📈 성능 개선

<br>

<details>
  <summary><b>✨ 인기 조회수 TOP 10 성능 개선</b></summary>

<br>

  <b>⁉️  성능 개선 포인트</b>

  현재 서비스 구조 상 메인 페이지에 인기 조회수 TOP 10 상품이 보여지기 때문에, 상품이 늘어날수록 메인 페이지 로딩 속도가 느려지는 문제가 있었습니다.

해당 문제를 방치할 경우, 타임딜 이벤트나 마케팅 효과로 인한 트래픽 급증 시 DB 점유율이 100%에 도달하여 전체 서비스 장애로 이어질 위험이 있었습니다.

상품 조회 시점마다 모든 데이터를 `ORDER BY DESC` 로 정렬하는 것과 `UPDATE` 쿼리를 DB에 직접 날리는 것이 좋지 못한 성능의 원인이었습니다.

<br>

<b>🙋‍♀️ 해결 방법</b>

Redis의 ZSet 자료구조를 활용하여 조회 UPDATE 뿐만 아니라 ORDER BY 정렬 연산 자체를 Redis에 위임함으로써 DB 부하를 근본적으로 제거했습니다.

<br>

<b>✨ 구현 내용</b>

<details>
  <summary>ProductRankingService.java</summary>

  ```java
@Service
@RequiredArgsConstructor
public class ProductRankingService {

    private final StringRedisTemplate redisTemplate;
    private static final String PRODUCT_DAILY_RANKING_PREFIX = "product:ranking:";
    private static final String PRODUCT_WEEKLY_RANKING_KEY = "product:ranking:weekly";
    private static final String PRODUCT_VIEW_CHECK_PREFIX = "product:view:check:ip:";
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;

    /**
     * 조회수 증가 메소드
     */
    public void increaseViewCount(Long productId, String ip) {

        LocalDateTime now = LocalDateTime.now();

        String key = PRODUCT_VIEW_CHECK_PREFIX + ip + ":productId:" + productId; // 오늘 조회 했는지 체크할 Key
        LocalDateTime midnight = LocalDate.now().plusDays(1).atStartOfDay(); // 다음 날 자정 만료
        Boolean isFirstView = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.between(now, midnight).getSeconds(), TimeUnit.SECONDS);

        // 오늘 첫 조회가 아니면 조회수 증가 x
        if (Boolean.FALSE.equals(isFirstView)) {
            return;
        }

        LocalDate nowDay =  LocalDate.now();

        Double score = redisTemplate.opsForZSet().incrementScore(PRODUCT_DAILY_RANKING_PREFIX + nowDay, productId.toString(), 1);

        // 최초 한 번만 TTL 설정
        if (score != null && score == 1.0) {

            // 오늘로부터 10일 뒤 자정 시점 구하기
            LocalDateTime dayViewCountExp = nowDay.plusDays(10).atStartOfDay();

            // TTL 설정: Redis에서 오늘로부터 10일 뒤 데이터 만료
            redisTemplate.expireAt(PRODUCT_DAILY_RANKING_PREFIX + nowDay, dayViewCountExp.atZone(ZoneId.systemDefault()).toInstant());
        }

    }

    /**
     * 조회수 TOP 10 상품 조회
     */
    public List<ProductGetPopularResponse> getPopularProductTop10() {

        // 랭킹으로 조회
        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().reverseRangeWithScores(PRODUCT_WEEKLY_RANKING_KEY, 0, 9);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        // DB에서 TOP 10 상품 id 리스트 가져오기: Redis가 정렬해준 순서 보장함
        List<Long> productIdList = result.stream().map(id -> Long.parseLong(id.getValue())).toList();

        // DB에서 Product 가져오기: in 연산은 Redis가 정렬해준 순서를 보장해주지 않음
        List<Product> productList = productRepository.findAllByIdInAndStatusAndIsDeletedFalse(productIdList, ProductStatus.FOR_SALE);

        // DB에서 TimeDeal 중인 상품 있으면 가져오기
        List<TimeDeal> timeDealList = timeDealRepository.findAllByProductIdInAndStatusAndIsDeletedFalse(productIdList, TimeDealStatus.OPEN);

        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Map<Long, TimeDeal> timeDealMap = timeDealList.stream()
                .collect(Collectors.toMap(timeDeal -> timeDeal.getProduct().getId(), timeDeal -> timeDeal));

        // 랭킹에 따라 조립
        return productIdList.stream()
                .map(id -> {
                    Product product = productMap.get(id);
                    return product != null ? ProductGetPopularResponse.from(product, timeDealMap.get(id)) : null;
                })
                .filter(Objects::nonNull) // null인 애들은 걸러냄
                .toList();
    }

    /**
     * 주간 인기 집계
     */
    public void updateWeeklyRanking() {
        LocalDate now = LocalDate.now();

        List<String> otherKeys = new ArrayList<>();

        // 현재 시점으로부터 일주일치 키 리스트 생성 (오늘은 제외)
        for (int i = 0; i < 6; i++) {
            otherKeys.add(PRODUCT_DAILY_RANKING_PREFIX + now.minusDays(i + 1));
        }

        // 일주일치 결과 집계
        redisTemplate.opsForZSet().unionAndStore(
                PRODUCT_DAILY_RANKING_PREFIX + now,
                otherKeys,
                PRODUCT_WEEKLY_RANKING_KEY);
    }

}
```

</details>
1. ZSET: 조회수를 스코어로 활용하여 실시간 랭킹을 유지했습니다.
2. Sliding Window: “주간 인기 상품”을 구현하기 위해 날짜별 키를 생성하고, 여유기간을 두고 10일 후 만료 되도록 TTL을 설정했습니다.

<br>

<b>💡 결과 및 효과</b>
<details>
  <summary>EC2 사양(t3.small)에 비례하는 로컬 테스트 환경 구축</summary>

  - JVM 메모리 제한 : `-Xms1024m -Xmx1024m`
- t3.small에 맞게 도커 메모리 제한해서 실행
  ```
  docker build -t zset-v1 .
  ```

  ```
  docker run -d --name zset-v1  --cpus="2.0"  --memory="2048m"  -e JAVA_OPTS="-Xms1024m -Xmx1024m" -e DB_URL="jdbc:mysql://host.docker.internal:3306/ssak3?rewriteBatchedStatements=true" -e DB_USERNAME="root"  -e DB_PASSWORD="" -e DDL="update" -e SECRET_KEY="" -e CLIENT_ID="" -e REDIRECT_URI="http://host.docker.internal:8080/ssak3/auth/login/kakao/callback" -e CLIENT_SECRET="" -e REDIS_HOST="host.docker.internal" -e REDIS_PORT="6379" -p 8080:8080 zset-v1
  ```

  ```
  server:
    tomcat:
      threads:
        max: 50
  ```  
</details>

<details>
  <summary>ZSet 도입 전</summary>
 vus 50
 <img width="833" height="340" alt="image" src="https://github.com/user-attachments/assets/0dcced1b-3323-448f-b0f6-df9e5f15af5a" />

  
</details>

<details>
  <summary>ZSet 도입 </summary>
 vus 50
 <img width="993" height="336" alt="image" src="https://github.com/user-attachments/assets/c52b6a23-9213-48f7-be2d-5ed785f591a6" />

</details>


| **지표** | **기존 방식** | **현재 방식** | **개선 수치** |
| --- | --- | --- | --- |
| **처리량 (TPS)** | 약 32.7 req/s | 약 329.6 req/s | **약 10배 향상** |
| **평균 응답 시간** | 1,500ms (1.5s) | 103.6ms | **약 14.5배 단축** |
| **p95 응답 시간** | 2,510ms (2.5s) | 304.5ms | **약 8.2배 단축** |
| **총 처리 건수** | 1,954건 | 28,107건 | **약 14.4배 증가** |

기존에는 DB에서 조회수 순으로 정렬한 결과를 캐싱하여 사용했습니다. 하지만 부하 테스트 결과, 사용자가 몰릴 때 p(95) 응답 시간이 844ms까지 치솟는 현상을 확인했습니다. 이는 DB가 대량의 데이터에 대해 정렬 연산을 처리하는 데 한계가 있기 때문이라고 판단했습니다.

이러한 DB의 병목을 해결하기 위해 정렬 연산의 주체를 DB에서 Redis로 위임하여, p(95) 응답 속도와 TPS를 향상 시켰습니다. 또 기존 방식의 최신 트렌드를 반영하지 못하던 문제를 해결하고 최근 7일 간의 인기 조회수 상품을 제공할 수 있게 되었습니다.

본 테스트는 인프라 제약으로 인해 로컬 환경에서 수행되었습니다. 실제 운영 서버(vCPU 2 Core/RAM 2GB/t3.small)은 로컬 PC 대비 자원이 제한적이므로, 실운영 환경에서의 절대적인 응답 시간은 테스트 수치보다 높게 측정될 수 있습니다.

하지만 로컬 환경에서 Redis Zset 적용 전후의 성능을 대조하였으므로, p(95) 응답 속도 개선, TPS 향상 등의 결과는 유의미한 결과라고 판단했습니다.

<br>

<b>📝 회고</b>

**개선한 점**

- 정렬이 핵심인 랭킹 시스템에서 RDBMS의 비싼 정렬 비용을 대신해, 메모리 상에서 정렬 상태를 유지하는 Redis ZSet의 특성을 파악하고 적절히 적용하여 성능을 개선했습니다.

**아쉬운 점**

- 더 다양한 조건에서 테스트 해보지 못한 것이 아쉽습니다.

**향후 고도화**

- Redis가 장애 시 DB로 모든 트래픽이 쏠리게 되면 DB도 같이 다운될 수 있는데, 이를 해결하기 위해 Circuit Breaker 도입을 고려하고 있습니다. 장애를 감지하면 즉시 DB 요청을 차단하고 미리 설정된 Fallback 데이터를 반환함으로써, 극단적인 상황에서도 최소한의 서비스 가용성을 보장하는 견고한 서비스를 구축하고 싶습니다.
  
<br>
</details>

<details>
  <summary><b>⚡  타임딜 open 목록 조회 성능 최적화</b></summary>

<br>

  <b>⁉️  성능개선 포인트</b>

  - 타임딜 OPEN 목록 조회 시, 사용자 트래픽이 많을 경우 동일한 조회 요청이 반복되며 **DB 조회 부담이 증가했습니다.**
- 특히 첫 페이지는 새로 OPEN 되는 타임딜이 노출되는 구간으로, 접속자가 집중되면서 **응답 속도 지연이 발생할 가능성이 높았습니다.**
- 이러한 특성상 전체 페이지 중 일부 구간(첫 페이지)에 트래픽이 편중되는 구조임을, **성능 테스트 결과를 통해, 첫 페이지에 요청이 집중될수록 응답 시간이 크게 증가하는 현상을 확인했습니다.**

<br>

<b>🙋‍♀️ 해결 방법</b>

- **Spring Cache + Redis** 기반 캐싱 적용
- 캐시 전략:
    - 트래픽 집중도가 가장 높은 첫 페이지 중심 캐싱
    - 조건 : #status == 'OPEN' &&#pageable.pageNumber <= 1
- READY/ CLOSED 상태는 조회 빈도가 낮고 상태 변경이 잦아 캐싱 효율이 낮다고 판단하여  제외했습니다.
- 나머지 페이지는 실시간 DB 조회로 처리하여 메모리 부담을 최소화했습니다.

<br>

<b>✨ 구현 내용 </b>

- Redis 기반 CacheManager를 구성하여 타임딜 OPEN 목록 조회 결과를 캐싱했습니다.
- 캐시 TTL은 10분으로 설정해, 캐시가 비정상적으로 오래 유지되는 상황을 방지했습니다.
- @Cacheable을 적용하여 OPEN 상태이면서 첫 페이지(pageNumber ≤ 1) 요청만 캐싱되도록 조건을 명시했습니다.
- 페이지 번호와 페이지 사이즈를 캐시 키로 사용해 페이지별 응답 데이터의 정합성을 유지했습니다.
- 조회 메서드는 readOnly 트랜잭션으로 구성해 캐시 미스 시에도 DB 부하를 최소화했습니다.

  <img width="593" height="737" alt="image" src="https://github.com/user-attachments/assets/b3e6be5b-73e6-4036-b3ee-7cea35270c97" />
  <img width="877" height="284" alt="image" src="https://github.com/user-attachments/assets/8d528556-5adb-45b2-b419-467e5438376a" />

<br>

<b>💡 결과 및 효과</b>

1. p95 기준 응답 시간 대폭 개선
- 캐싱 전: **약 5.05s**
- 캐싱 후: **약 25.45ms (0.025s)**
- 동일한 테스트 환경에서 비교했을 때, **p95 기준 약 99.5% 이상 응답 시간 감소**를 확인했습니다.

- 캐싱 전

  
  <img width="826" height="606" alt="image" src="https://github.com/user-attachments/assets/a1f1c488-46e2-4d87-b8d4-5d067b15eca1" />

- 캐싱 후

  
  <img width="872" height="593" alt="image" src="https://github.com/user-attachments/assets/27f1c821-05df-4917-a098-19eea8fc4a46" />

2. 평균 응답 시간 및 지연 구간 개선
- 평균 응답 시간: **1.49s →9.72ms (0.009s)**
- p90 구간: **2.8s → 16.59ms**
- p95 구간: 5**.06s → 25.45ms**

p90 / p95 구간 모두 초 단위에서 밀리초 단위로 감소하여, 트래픽이 집중되는 상황에서도 **사용자 체감 성능이 획기적으로 개선**되었습니다.

특히 기존에는 일부 요청이 최대 8.93**s**까지 지연되었으나, 캐싱 이후에는 최대 지연도 **372.49ms** 수준으로 안정화되었습니다.

3. 처리량 증가
- 동일한 부하 조건에서
    - 캐싱 전: **4,886 requests**
    - 캐싱 후: 11,198 **requests**

동일한 테스트 조건에서 총 처리 요청  수 약 2.3배 증가, 초당 처리량 약 2.5배 증가 했습니다.

캐싱 적용 이후 **더 많은 요청을 안정적으로 처리**할 수 있음을 확인했습니다.

4. 테스트 환경에 대한 해석
- 본 성능 테스트는 서버 환경 제약으로 인해 **로컬 개발 PC에서 수행**되었습니다.
- 실제 운영 서버는 vCPU 2 Core / Memory 2GB(t3.small) 환경으로, 테스트 환경 대비 자원이 제한적이기 때문에 절대적인 응답 시간 수치는 더 불리할 수 있습니다.
- 다만 캐싱 전·후를 **동일한 환경에서 비교**했기 때문에, **상대적인 성능 개선 효과(p95 감소, 처리량 증가)는 유의미하다고 판단했습니다.**

<br>

<b>📝 회고</b>

타임딜 상태 변경은 스케줄러를 통해 관리되므로 캐시 TTL에 의존하지 않고 상태 변경 시점에 캐시를 명시적으로 무효화했습니다.

또 모든 페이지를 캐싱하기보다는 트래픽이 집중되는 첫 페이지에만 캐싱을 적용하여, 성능 개선과 메모리 사용량 사이의 균형을 맞출 수 있었습니다.

이번 개선을 통해 캐시는 단순한 성능 최적화 수단이 아니라, 도메인 특성과 트래픽 패턴을 함께 고려해 설계해야 한다는 점을 체감했습니다.

**향후 고도화**

- 데이터가 증가함에 따라 OFFSET 기반 페이징은 페이지 번호가 커질수록 불필요한 스캔 비용이 증가하는 한계가 있습니다. 향후에는 커서 기반 페이징과 적절한 인덱싱을 적용하여, 대용량 데이터 환경에서도 조회 성능 저하를 방지할 수 있을 것으로 판단했습니다.

</details>

<details>
  <summary><b>👍 문의 채팅 메시지 처리 방식 최적화</b></summary>

<br>
  
<img width="972" height="390" alt="image" src="https://github.com/user-attachments/assets/2a9a8b0e-3894-4002-ad26-711ccf63a9ed" />


*위 이미지는 동기 메시지 수신 방법을 도식화한 것입니다.*

이 방식은 채팅 메시지가 들어오면 먼저 DB에 저장된 후 해당 내용이 Redis를 거쳐서 채팅방에 뿌려지는 형식입니다. 초기 STOMP 적용 시, 순수 WebSocket에서 사용하던 **동기 방식의 DB 저장 로직**을 그대로 가져오면서 다음과 같은 처리 흐름이 만들어졌습니다:

```
    메시지 수신 → DB 저장 (대기) → Redis 발행 → 클라이언트 수신
```

하지만 이 방식은 DB 저장이 완료될 때까지 메시지 전달이 지연되는 구조로, **실시간성이 생명인 채팅 기능**에는 적합하지 않다고 판단되었습니다.

따라서 채팅 메시지의 우선순위를 재정의했습니다.

1. **1순위**: 실시간 메시지 전달 (사용자 경험)
2. **2순위**: 영구 저장 (이력 조회)

우선순위 재정의 과정을 통해 DB 저장은 백그라운드에서 처리하고, **실시간 전달에만 집중**하는 비동기 구조가 필요하다고 판단했습니다.

<br>

<b>🙋‍♀️ 해결 방법</b>

<details>
  <summary>@Async를 활용한 비동기 저장</summary>

  메시지 수신 후 Redis를 즉시 발행하며 DB 저장 요청은 별도의 스레드에서 처리

**장점**

- 즉각적인 메시지 전달 (지연 시간 최소화)
- 구현 복잡도 낮음 (`@Async` 어노테이션만으로 처리)
- 기존 코드 구조 유지 가능

**단점**

- 메시지 단위 처리로 DB 부하는 동일
- 대량 메시지 발생 시 스레드 풀 고갈 가능성

**적용 이유**

- 1:1 채팅 특성상 메시지 발생 빈도가 높지 않음
- 빠른 개선 효과를 얻을 수 있음
- 추후 Batch 전환 시에도 호환 가능한 구조
  
</details>

<details>
  <summary>Spring Batch를 활용한 일괄 저장</summary>
  메시지 수신 → Redis 발행 → 메모리 큐 적재 → Batch Insert(일정 주기)

**장점**

- DB 부하 대폭 감소
- 트랜잭션 횟수 최소화

**단점**

- 구현 복잡도 증가
- 메모리 관리 필요
- 서버 재시작 시 큐 데이터 유실 위험

**보류 사유**

- 현재 트래픽 규모에서는 과도한 최적화로 판단
- 메시지 유실 방지를 위한 추가 안전장치 필요
</details>

<br>

<b>✨ 구현 내용 </b>

- 비동기 저장 방식 구현하기
  <details>
    <summary>InquiryChatStompController</summary>

  ```java

    /**
     * 채팅 메시지 전송 API
     */
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequest request, StompHeaderAccessor accessor) {
        
        // 핸들러의 세션에서 인증된 유저 정보 꺼내기
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long userId = (Long) sessionAttributes.get("userId");
        String role = (String) sessionAttributes.get("role");

        // Redis 먼저 발행
        ChatMessageResponse tempResponse = ChatMessageResponse.fromRequest(request, userId, role);
        redisTemplate.convertAndSend(chatTopic.getTopic(), tempResponse);

        // DB 저장은 비동기로 처리
        inquiryChatService.saveMessageAsync(request, userId, role);
    }
  ```
  </details>
  <details>
  <summary>InquiryChatService</summary>

  ```java
  @Async("chatExecutor")  // 별도 스레드에서 실행
  @Transactional
  public void saveMessageAsync(ChatMessageRequest request, Long userId, String role) {
      try {
          InquiryChatRoom foundRoom = roomRepository.findById(request.getRoomId())
                  .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
  
          if (foundRoom.getStatus() == ChatRoomStatus.COMPLETED) {
              throw new CustomException(ErrorCode.INQUIRY_CHAT_ALREADY_COMPLETED);
          }
  
          User foundUser = userRepository.findById(userId)
                  .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  
          InquiryChatMessage message = new InquiryChatMessage(
                  foundRoom,
                  foundUser,
                  UserRole.valueOf(role),
                  request.getType(),
                  request.getContent()
          );
  
          messageRepository.save(message);            
      } catch (Exception e) {
          log.error("메시지 저장 실패 - roomId: {}, userId: {}, error: {}", 
              request.getRoomId(), userId, e.getMessage(), e);
      }
  }
  ```
  **변경점**
  
  - `saveMessageAsync()` 메서드 추가 (비동기)
  - `@Async("chatExecutor")` 사용하여 별도 스레드에서
  
  </details>
  <details>
  <summary>ChatMessageResponse</summary>

  ```java
  @Getter
  @RequiredArgsConstructor
  @NoArgsConstructor(force = true)  // 역직렬화 가능
  public class ChatMessageResponse {
      private final Long roomId;
      private final Long senderId;
      private final UserRole senderRole;
      private final ChatMessageType type;
      private final String content;
      private final LocalDateTime createdAt;
      private final LocalDateTime updatedAt;
  
      // 채팅 메시지 Response(DB 조회 메시지)
      public static ChatMessageResponse from(InquiryChatMessage inquiryChatMessage) {
          return new ChatMessageResponse(
                  inquiryChatMessage.getRoom().getId(),
                  inquiryChatMessage.getSender().getId(),
                  inquiryChatMessage.getSenderRole(),
                  inquiryChatMessage.getType(),
                  inquiryChatMessage.getContent(),
                  inquiryChatMessage.getCreatedAt(),
                  inquiryChatMessage.getUpdatedAt()
          );
      }
  
      // 공지 메시지 Response
      public static ChatMessageResponse from(ChatMessageRequest request, String noticeMessage) {
          return new ChatMessageResponse(
                  request.getRoomId(),
                  0L,
                  request.getSenderRole(),
                  request.getType(),
                  noticeMessage,
                  LocalDateTime.now(),
                  LocalDateTime.now()
          );
      }
  
      // 메시지 요청으로부터 임시 Response 생성(DB 저장 전 Redis로 먼저 보냄)
      public static ChatMessageResponse fromRequest(ChatMessageRequest request, Long userId, String role) {
          return new ChatMessageResponse(
                  request.getRoomId(),
                  userId,
                  UserRole.valueOf(role),
                  request.getType(),
                  request.getContent(),
                  LocalDateTime.now(),  // DB 저장 전이므로 현재 시간 기준
                  LocalDateTime.now()
          );
      }
  }
  ```
  **변경점**
  
  - `saveMessageAsync()` 메서드 추가 (비동기)
  - `@Async("chatExecutor")` 사용하여 별도 스레드에서
  
  </details>
  <details>
  <summary>AsyncConfig</summary>

  - 새로 추가하는 파일

  ```java
  @Configuration
  @EnableAsync
  public class AsyncConfig {
  
      @Bean(name = "chatExecutor")
      public Executor chatExecutor() {
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  
          // 기본 스레드 수 (항상 유지)
          executor.setCorePoolSize(5);
  
          // 최대 스레드 수 (큐가 꽉 찼을 때 증가)
          executor.setMaxPoolSize(10);
  
          // 대기 큐
          executor.setQueueCapacity(100);
  
          // 스레드 이름 설정 (로그에서 확인 가능)
          executor.setThreadNamePrefix("chat-async-");
  
          // 큐가 꽉 찼을 때 호출한 스레드에서 직접 실행 (메시지 손실 방지)
          executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
  
          executor.initialize();
          return executor;
      }
  }
  ```
  
  **스레드 풀 설계 근거**

  | 항목 | 값 | 설명 | 이유 |
  | --- | --- | --- | --- |
  | **corePoolSize** | 5 | 항상 유지되는 기본 스레드 수 | 평균 동시 채팅 수 고려 |
  | **maxPoolSize** | 10 | 부하가 높을 때 최대 10개까지 증가 | 피크 시간대 대응 |
  | **queueCapacity** | 100 | 대기 중인 작업을 100개까지 큐에 보관 | 급격한 트래픽 스파이크 버퍼 |
  | **CallerRunsPolicy** | - | 큐가 꽉 차면 메시지를 버리지 않고 호출 스레드에서 실행 | 큐 초과 시 메인 스레드에서 처리 |
 
  </details>

  <details>
  <summary>변경된 동작 흐름</summary>

  ```java
  1. 클라이언트가 /pub/chat/message로 메시지 전송
     ↓
  2. InquiryChatStompController.sendMessage() 실행
     ↓
  3. ChatMessageResponse.fromRequest()로 임시 응답 생성
     ↓
  4. Redis로 즉시 발행
     ↓
  5. RedisSubscriber → 구독자들에게 전송
     ↓
  6. (동시에) saveMessageAsync()가 별도 스레드에서 DB 저장
  ```
  </details>

<br>

<b>💡 결과 및 효과</b>

동기 저장 방식
<img width="1024" height="342" alt="image" src="https://github.com/user-attachments/assets/6ebb4416-6513-4cd7-a425-c7b9c343fee4" />
DB를 거친 후 메시지를 뿌려주므로 소요 시간이 25ms 정도 걸리는 것을 확인할 수 있습니다.

비동기 저장 방식
<img width="1024" height="73" alt="image" src="https://github.com/user-attachments/assets/97e1be76-8b6f-4bd0-81f2-8d3f5cd13352" />

DB 저장 전에 메시지를 먼저 뿌려주므로 소요 시간이 **3~7ms** 정도 걸리는 것을 확인할 수 있습니다.

**비동기 저장 방식이 동기 저장 방식에 비해 소요 시간이 훨씬 짧은 것을 확인해 볼 수 있었습니다.**

<br>

<b>📝 회고</b>

이번 성능 개선 작업을 통해 실시간 응답과 데이터 영속성 사이에서 우선순위를 정하는 것 과정을 배울 수 있었습니다.

처음엔 데이터가 유실되면 안된다고 생각해, DB 저장 후 메시지를 보내는 방식으로 제작했습니다. 

하지만 정작 중요한 **채팅 속도**가 느려지는 문제가 생겼습니다. 사용자는 상대방에게 **바로 전달되는 것**을 더 중요하게 생각한다는 점을 놓쳤던 것입니다.

그래서 **빠른 소통**을 1순위로 두고, DB 작업은 뒤에서 따로 처리하도록 구조를 바꾸니 서버는 기다리는 시간 없이 메시지를 더 빠르게 처리할 수 있게 되었습니다. 

단순히 새로운 기술을 배우는 것보다, 서비스의 성격에 맞춰 **기술의 우선순위를 배치하는 법**을 배울 수 있었습니다.

**향후 고도화**

- 현재는 트래픽이 크게 몰리지 않을 것으로 잡고 제작하였지만 추후에 서비스의 규모가 커질 경우 이를 처리할 수 있는 장치가 필요하다고 판단했습니다. 현재의 메시지 단위 비동기 처리를 넘어, Redis를 버퍼로 활용하는 **Write-Back** 전략을 도입하여 메시지를 Redis 리스트에 임시 저장한 뒤, 일정 수량 이상 모이면 `Bulk Insert`를 수행하는 **Spring Batch** 연동하는 작업을 진행해보고 싶습니다.

<br>
</details>

<details>
  <summary><b>✨ 사용자 전용 쿠폰 목록 조회 성능 최적화</b></summary>

  <br>

  <b>⁉️  성능개선 포인트</b>
  
  - **DB 부하 집중:** 사용자 전용 쿠폰 목록 조회는 모든 사용자가 접속 시 가장 먼저 확인하는 페이지로, 트래픽이 몰릴 경우 반복적인 DB Query가 발생하여 데이터베이스 병목 현상을 유발할 수 있습니다.
- **낮은 데이터 변동성:** 발급 가능한 쿠폰 목록은 관리자가 수정하기 전까지는 데이터가 자주 바뀌지 않는 특성을 가졌기 때문에 매번 DB에서 데이터를 읽어오는 것은 리소스 낭비라고 판단했습니다.

<br>

<b>🙋‍♀️ 해결 방법</b>

- **캐시 전략 도입:** 사용자가 쿠폰 목록을 조회할 때 먼저 Redis 캐시를 확인하고, 캐시에 데이터가 없을 때만 DB에 접근하도록 했습니다.
- **캐시 일관성 보장:** 관리자가 쿠폰 정보를 수정하거나 삭제할 경우, 기존 캐시 데이터가 부정확해질 수 있으므로 즉시 캐시를 비우는 로직을 추가했습니다.

<br>

<b>✨ 구현 내용 </b>

- **RedisTemplate 활용:** `coupons:user:page:`라는 프리픽스를 사용하여 쿠폰 페이지별로 데이터를 캐싱했습니다.
- **TTL 설정:** 캐시의 유효 기간을 **10분**으로 설정하여 메모리를 효율적으로 관리하고자했습니다.
- **Cache Eviction(캐시 무효화):** `CouponService`에서 쿠폰의 정보가 변경(`updateCoupon`)되거나 삭제(`deleteCoupon`)될 때 `clearUserCouponListCache()` 메서드를 호출하여 캐시를 초기화하도록 설계

<br>

<b>💡 결과 및 효과</b>

- **응답 속도 향상:** 고속 메모리 기반의 Redis를 활용함으로써 DB 접근 횟수를 줄여, 대규모 트래픽 상황에서도 응답 시간을 평균 29.7% 단축했습니다.
    
    **[ K6 테스트 ]**
    
    - **테스트 환경:** MacBook Air (로컬) / 가상 유저 150명 (VUs) / 1분 지속
    **대상 인프라 사양:** AWS EC2 t3.small (2 vCPU, 2 GiB RAM) 대비 고부하 시뮬레이션
        - Before
          <img width="800" height="681" alt="image" src="https://github.com/user-attachments/assets/f9cd1a78-e81f-4b4f-b39c-07f3807d8023" />
        - After
          <img width="806" height="677" alt="image" src="https://github.com/user-attachments/assets/9d1427db-35ff-41eb-a0fb-6126ecc87dd8" />
      - **성능 비교 지표**

      | 지표 | 캐싱 전(DB Only) | 캐싱 후(Redis) | 개선율 |
      | --- | --- | --- | --- |
      | 평균 응답 시간 | 9.46ms | 6.65ms | 약 29.7% 개선 |
      | 중앙값 | 7.43ms | 4.90ms | 약 34.0% 개선 |
      | 상위 95% 응답 | 22.43ms | 15.55ms | 약 30.7% 개선 |
      | 최대 응답 시간 | 237.94ms | 74.94ms | 약 68.5% 안정화 |
- **DB 리소스 보호:** 반복적인 `SELECT` 쿼리를 차단하여 데이터베이스의 CPU 및 I/O 부하를 감소시켰고, 이는 서비스 전체의 안정성 향상으로 이어집니다.

<br>

<b>📝 회고</b>

**개선한 점**

- 반복 조회가 빈번한 쿠폰 목록의 특성을 반영해 Redis를 도입했으며, 이를 통해 DB 부하를 줄이고 응답 속도를 약 29.7% 향상시켰습니다.

**향후 고도화**

- **캐시 전략 고도화**: 현재는 페이지 단위로 캐싱 중이나, 향후 개인화 추천이나 필터링 기능 확장에 대비해 **Redis의 Hash 구조나 Sorted Set**을 활용한 세분화된 캐싱 전략을 구축하여 메모리 효율과 검색 속도를 극대화할 계획입니다.
- **동시성 제어 고도화**: 현재 비관적 락으로 정합성을 유지하고 있으나, 대규모 트래픽 시 DB 커넥션 병목이 우려됩니다. 이를 해결하기 위해 **Redisson 기반 분산 락**을 도입하여 DB 접근 전 순서를 제어하고, 캐시 만료 시 DB 부하가 몰리는 **Cache Stampede** 현상을 방지하는 로직을 보완하겠습니다.

<br> 
</details>

<details>
  <summary><b>👍 카테고리 조회 시 RedisCacheManager로 관리</b></summary>

<br>

  <b>⁉️  성능개선 포인트</b>
  
  현재 로컬 캐시(Caffeine)는 관리자(admin)가 카테고리를 수정 혹은 삭제하는 즉시 관리자 로컬캐시 내부 데이터는 삭제되는 반면, 사용자(user) 서버 내에 있는 캐시 메모리에는 여전히 과거의 카테고리 데이터가 남아있게 됩니다.

로컬 캐시를 쓰면서 모든 서버의 상태를 맞추기 위해 서버 A가 수정될 때마다 서버 B, C에게 수정된 내용을 신호로 보내야 합니다. → 캐시 일관성(Cache Coherency)

서버 수가 많아질수록 로직을 처리하는 트래픽보다 변경된 내용을 전달하면서 발생하는 트래픽의 규모가 훨씬 커질 수 있습니다. 이는 저장 공간의 부하와 더불어 네트워크 비용도 커지는 문제가 발생할 수 있습니다.

<br>

  <b>🙋‍♀️ 해결 방법</b>

  1. 모든 서버가 하나의 외부 캐시 저장소를 바라보는 구조인 분산캐시를 채택

  2. 분산캐시 Redis와 Memcached 중 Redis를 선택 → 데이터 타입 선택시 유리

      - Redis : Memcached는 데이터 타입을 문자열만 지원. 반면 Redis는 String, List, Set, Hashe, Sorted Set 등 다양한 데이터 타입 지원

  3. 단순 카테고리 목록조회 API 구현이기 때문에 Redistemplate으로 세부적인 데이터 조율❌
     ➡ @Cacheable을 사용하여 하나의 key값으로 value값을 제어⭕

<br>

  <b>✨ 구현 내용 </b>

  <details>
  <summary>CategoryRedisConfig.java</summary>

  ```java
    
    @Configuration
    public class CategoryRedisConfig {
    
        @Bean
        public CacheManager categoryCacheManager(RedisConnectionFactory connectionFactory) {
    
            // Serializer 에게 줄 규칙 정의
            ObjectMapper objectMapper = new ObjectMapper();
    
            // 날짜/시간 모듈 등록 및 타임스탬프 형식 비활성화
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
            // 1. Jackson의 ObjectMapper에게 "이건 List인데 안에 CategoryGetResponse가 들어있어" 라고 말해줌
            JavaType categoryListType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, CategoryGetResponse.class);
    
            // 2. 그 정보를 시리얼라이저에 넘겨줌
            Jackson2JsonRedisSerializer<List<CategoryGetResponse>> serializer =
                    new Jackson2JsonRedisSerializer<>(objectMapper, categoryListType);
    
            // 3. Redis 캐시 설정 구성
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    // null 값은 캐싱하지 않음
                    .disableCachingNullValues()
                    // 캐시 유효 기간 설정 (30분)
                    .entryTtl(Duration.ofMinutes(30))
                    // 키와 값의 직렬화 방식 설정 (아까 배운 JSON 방식 적용!)
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    
            return RedisCacheManager.RedisCacheManagerBuilder
                    .fromConnectionFactory(connectionFactory)
                    .cacheDefaults(config)
                    .build();
        }
    
    }
    
  ```
    
  </details>
  <details>
  <summary>CategoryServcie.java</summary>

  ```java

  @Service
  @RequiredArgsConstructor
  public class CategoryService {
  
      private final CategoryRepository categoryRepository;
      private final ProductRepository productRepository;
  
      /**
       * 카테고리 생성 비즈니스 로직
       */
      @Transactional
      public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
  
          Category category = new Category(request.getName());
          Category savedCategory = categoryRepository.save(category);
          return CategoryCreateResponse.from(savedCategory);
      }
  
      /**
       * 카테고리 목록조회 비즈니스 로직
       */
      @Cacheable(value = "categoryRedisCache", key = "'all'")
      @Transactional(readOnly = true)
      public List<CategoryGetResponse> getCategoryList() {
          List<Category> categoryList = categoryRepository.findByIsDeletedFalse();
          List<CategoryGetResponse> listGetResponse = categoryList.stream().map(CategoryGetResponse::from).toList();
          log.info("service DB에서 조회된 결과: {}", listGetResponse);
          return listGetResponse;
      }
  
      /**
       * 카테고리 수정 비즈니스 로직
       */
      @CacheEvict(value = "categoryRedisCache", allEntries = true)
      @Transactional
      public CategoryUpdateResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
          Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                  .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
          findCategory.update(request);
         return CategoryUpdateResponse.from(findCategory);
      }
  
      /**
       * 카테고리 삭제 비즈니스 로직
       */
      @CacheEvict(value = "categoryRedisCache", allEntries = true)
      @Transactional
      public CategoryDeleteResponse deleteCategory(Long categoryId) {
          // 카테고리 존재여부 확인
          Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                  .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
  
          // 카테고리에 상품이 있다면 삭제 불가능 예외처리
          if (productRepository.existsByCategoryId(categoryId)) {
              throw new CustomException(ErrorCode.CATEGORY_HAS_PRODUCTS);
          }
          findCategory.softDelete();
          return CategoryDeleteResponse.from(findCategory);
      }
  }
    
  ```
    
  </details>

   DAU(일간 활성 사용자 수)를 10,000명, CCU(동시접속자)는 5%인 500명이라 예상했을 때 피크타임을 고려하여 redis적용 전 후로 테스트를 진행

<br>

  <b>💡 결과 및 효과</b>

  <details>
  <summary>Redis 도입 전</summary>

  <img width="950" height="496" alt="image" src="https://github.com/user-attachments/assets/821ba031-b6ce-494d-a606-8119f646c0e7" />
  </details>

  <details>
  <summary>Redis 도입 후</summary>

  | 지표 (Metric) | DB 조회 (AS-IS) | Redis 적용 (TO-BE) | 개선 결과 |
  | --- | --- | --- | --- |
  | 처리량 (Throughput) | 1,790 req/s | 2,585 req/s | **약 44.4% 향상** |
  | 평균 응답 속도 (Avg) | 11.04 ms | 7.29 ms | **약 34.0% 단축** |
  | 지연 시간 (p95) | 21.69 ms | 13.24 ms | **약 38.9% 단축** |
  | 성공률 (Checks) | 99.95% | 99.97% | **안정성 소폭 상승** |
  </details>

  - **성능 오버헤드 발견**
성능 테스트 결과, `sync = true` 옵션이 보장하는 애플리케이션 레벨의 락(Lock) 획득 과정에서 발생하는 대기 시간이 Redis의 빠른 데이터 처리 속도라는 장점을 상쇄한다는 것을 확인했습니다.

- **Redis 적용 후 변화**

  ① DB 조회 시에는 초당 약 **1,790건**을 처리하던 시스템이 Redis 적용 후 **2,585건**까지 처리할 수 있게 되었습니다.
  
  ② 이는 동일한 서버 자원으로 **약 1.4배 더 많은 사용자 요청**을 수용할 수 있음을 의미하며, 서비스 확장성(Scalability) 측면에서 매우 큰 이점입니다.
  
  ③ 평균 응답 시간**:** **11.04ms**에서 **7.29ms**로 줄어들어 시스템의 기민함이 좋아졌습니다.
  
  ④ 꼬리 지연 시간(p95**):** 상위 5%의 느린 응답조차 **21.69ms**에서 **13.24ms**로 크게 개선되었습니다.

  <br>

<b>📝 회고</b>

**회고**

- 캐싱을 적용할 때 대부분의 경우들 처럼 Redis Template을 사용하지 않고 RedisCache Manager로 커스텀 캐싱으로 구현한 점이 포인트였던 것 같습니다. 모든 기능을 직접 제어하는 명령형 방식인 RedisTemplate을 적용하기에는 카테고리 목록 조회 API가 단순 조회 방식이였기 때문입니다. 커스텀 캐싱으로 코드의 가독성과, 비즈니스 로직과 캐싱을 분리하여 유지보수성을 높이는 방식이 이번 기능 개선 내용에서 적합했다고 생각합니다.

**개선할 점**

- 캐시 스탬피드 (Cache Stampede) 대비 로직 구상
    
    : 현재 카테고리 캐시의 TTL 30분으로 설정한 상태입니다. 하지만 유효시간이 만료되고 여러 사용자가 카테고리 API에 접근하여 대량 트래픽이 발생해서 '캐시 스탬피드' 현상이 일어날 수 있습니다. 여기서  접근자를 한명씩 제한하는 방법으로 분산락을 사용할 지 @Cacheable( sync = true) 를 도입할지 고민해볼 필요가 있을 것 같습니다. 도입을 한 후 데이터 응답시간 지연의 문제가 감안할 수 있을 정도인지 판단하는 과정도 필요할 것 같습니다.
  
</details>

<br>

---

<a id="team"></a>
## 👨‍👩‍👧‍👦 팀원 소개

<br>
<table>
  <tr>
    <td align="center" width="33%">
    <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/b2bafc15-45a1-4342-98c6-2346ef840f39" />

<br/>
  <b>👑 팀장</b><br/>
  <a href="https://github.com/LemonCoding99">백은서</a><br/><br/>
  <sub>
  ✔ 1:1 문의 게시판<br/>
  ✔ 1:1 채팅<br/>
  ✔ CI/CD
  </sub>
</td>

<td align="center" width="33%">
    <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/5f9ea7ac-eede-46d6-96b3-1cccd905f6eb" />

<br/>
  <b>부팀장</b><br/>
  <a href="https://github.com/jionnie">정지원</a><br/><br/>
  <sub>
  ✔ 인증/인가<br/>
  ✔ 검색<br/>
  ✔ 로그
  </sub>
</td>

<td align="center" width="33%">
    <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/bae24de6-717e-4a39-83ec-ffc241c69180" />

<br/>
  <b>팀원</b><br/>
  <a href="https://github.com/thk7964">김태호</a><br/><br/>
  <sub>
  ✔ 타임딜<br/>
  ✔ 결제<br/><br/>
  </sub>
</td>
  </tr>
  <tr>
    <td align="center" width="33%">
    <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/3b55282e-6a45-4d41-b8e8-eb6f43c4e0c5" />

<br/>
  <b>팀원</b><br/>
  <a href="https://github.com/bjh8130">백재현</a><br/><br/>
  <sub>
  ✔ 상품    <br/>
  ✔ 카테고리<br/>
  ✔ 리뷰   
  </sub>
</td>
    <td align="center" width="33%">
    <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/4095811e-a744-4832-90d7-2ab80f96a225" />
      
<br/>
  <b>팀원</b><br/>
  <a href="https://github.com/JH319">임정하</a><br/><br/>
  <sub>
  ✔ 쿠폰<br/><br/>
<br/>

  </sub>
</td>
    <td align="center" width="33%">
    <img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/c22c9491-3ebc-42cd-b9e9-901c5659a7cb" />

<br/>
  <b>팀원</b><br/>
  <a href="https://github.com/jangse0">장서연</a><br/><br/>
  <sub>
  ✔ 장바구니<br/>
  ✔ 주문<br/>
  ✔ s3
  </sub>
</td>
  </tr>
</table>
