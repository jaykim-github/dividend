# 미국 주식 배당금 정보를 제공하는 API 서비스 개발하기
- 제로베이스 강의를 토대로 만들어진 프로젝트입니다.

<br>

:page_with_curl: 프로젝트 소개
- 웹 페이지를 분석하여 스크래핑 기법을 이용, 필요한 데이터를 추출하여 저장
- 사용자별 데이터를 관리하고 예상 배당금 액수를 계산할 수 있음
- 서비스에서 캐시의 필요성을 이해하고 캐시 서버를 구성

<br> 

:books: 프로젝트 스택
- Spring boot, Java
- DB는 H2 DB(memory DB 모드)를 활용
- DB를 접근하는 방법은 Spring data jpa를 활용
- 캐시 서버를 구성하는 것은 Embedded redis를 활용
- 각각의 API들은 각자의 요청과 응답 객체 구조를 가짐

<br>

:black_circle: 프로젝트 기능

<br>

:one: API 설명
<br>
1. GET - finance/dividen/{companyName} 
- 회사 이름을 인풋으로 받아서 해당 회사의 메타 정보와 배당금 정보를 반환
- 잘못된 회사명이 입력으로 들어온 경우 400 status 코드와 에러메시지 반환

<br>

2. GET - company/autocomplete
- 자동완성 기능을 위한 API
- 검색하고자 하는 prefix 를 입력으로 받고, 해당 prefix 로 검색되는 회사명 리스트 중 10개 반환

<br>

3. GET - company
- 서비스에서 관리하고 있는 모든 회사 목록을 반환
- 반환 결과는 Page 인터페이스 형태

<br>

4. POST - company
- 새로운 회사 정보 추가
- 추가하고자 하는 회사의 ticker 를 입력으로 받아 해당 회사의 정보를 스크래핑하고 저장
- 이미 보유하고 있는 회사의 정보일 경우 400 status 코드와 적절한 에러 메시지 반환
- 존재하지 않는 회사 ticker 일 경우 400 status 코드와 적절한 에러 메시지 반환

<br>

5. DELETE - company/{ticker}
- ticker 에 해당하는 회사 정보 삭제
- 삭제시 회사의 배당금 정보와 캐시도 모두 삭제되어야 함

<br>


6. POST - auth/signup
- 회원가입 API
- 중복 ID 는 허용하지 않음
- 패스워드는 암호화된 형태로 저장되어야함

<br>


7. POST - auth/signin
- 로그인 API
- 회원가입이 되어있고, 아이디/패스워드 정보가 옳은 경우 JWT 발급
