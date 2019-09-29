# Picnic
![app_icon](https://user-images.githubusercontent.com/22374750/65391345-15672600-dda3-11e9-909f-8c6c0b4762aa.png)
### 소개 및 필요성
- 사용자의 취향에 맞게 서울의 다양한 플레이스를 큐레이션하는 어플리케이션 서비스입니다.
- 바쁜 일상 속에도, 실속 있게 휴가를 다니는 사람들이 늘어나고 있습니다. 그래서, 저희는 '국내 서울 근교의 사용자'에 맞춰 서비스를 제공하려고 합니다.

### 주요기능 및 서비스
- 서울시 공공데이터, Tour API를 기반으로, 사용자들에게 다양한 플레이스를 추천해주는 서울 피크닉 추천 어플리케이션.
- 위치 기반으로 반경 5km 안에 위치한 ‘관광, 문화, 레포츠, 맛집, 쇼핑, 축제, 여행’ 정보를 제공합니다.
- 플레이스에 대한 자세한 정보는 지도, 전화번호, 사진 등을 포함한 매거진 형태의 ‘콘텐츠’로 제공합니다.
- 사용자가 마음에 드는 콘텐츠를 스크랩하고, 공유할 수 있습니다.
- 콘텐츠에 대해서 다른 사용자들과 이야기를 나눌 수 있는 실시간 익명 채팅 서비스를 제공합니다.
- 특히, 서울시 공공데이터의 공원 API를 기반으로 주소 기반 플레이스 검색과 공원 기반 플레이스 검색을 제공합니다.

### 개발 및 Tools
○ 운영체제 : Android OS (targetSdkVersion : 28, minSdkVersion : 21)
○ 개발Tool : AndroidStudio 3.4.2
○ DB
- remote : Firebase Database, 클라우드 환경의 Node기반 Express 서버
- local : Realm Database
○ Git
- 안드로이드 : https://github.com/hakzzang/Picnic
- 서버 : https://github.com/hakzzang/PicnicServer
### 0. MVP 패턴을 위한 학습
- MVP 패턴을 학습하기 위해서, 기본적인 예제를 통한 MVP구조를 이해하고, 구조를 깔끔하게 하는 것이 목표1.
- 모델을 이해해서 각종 API를 효율적으로 사용할 수 있도록 하는 것이 목표2.

### 0-0. 기술적 학습
- 네스티드 스크롤뷰를 사용하지 않고, 다양한 뷰 표현하기 : https://gamjatwigim.tistory.com/102
- CoordinatorLayout 안에 SwipeRefreshLayout 사용하기 : https://gamjatwigim.tistory.com/104
### 0-1. 계산기 패턴을 통한 MVP Contract 구조
- https://medium.com/@PaperEd/android-how-to-mvp-1ff398b25cb1

### 0-2. TECH CONCERT: MOBILE 2019 - 예제에서는 알려주지 않는 Model 이야기
- https://tv.naver.com/v/9329728/list/486582

### 0-3. Todo APP을 통해서 각종 문제 해결 
- https://github.com/googlesamples/android-architecture/tree/todo-mvp/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp

### 1. View를 만들기위한 CustomView 학습
- https://medium.com/mindorks/android-custom-views-tutorial-part-1-115fa8d53be5

### 2. 프로젝트 분리
- Recommend : 메인 화면을 의미하고, 간단한 뷰를 통해서 피크닉 추천 장소를 보여준다.
- Content : 메인 화면에서 넘어와서 상세화면을 보여주려고 한다.

### 3. 네이버 지도 사용
- 네이티브 지도 : https://navermaps.github.io/android-map-sdk/guide-ko/0.html
- Static Map Image : https://docs.ncloud.com/ko/naveropenapi_v3/maps/static/static-map.html

### 4. API
- ToupAPI 3.0 : http://api.visitkorea.or.kr/main.do
- 공공데이터 API : https://www.data.go.kr/dataset/15000496/openapi.do
- 서울시 공공데이터 API : ;3;
- 카카오 링크 : https://developers.kakao.com/docs/android/kakaotalk-link
- 피크닉 서버 : 어딘가

### 5. Data
- Remote : Firebase, Custom Server
- Local : Realm

### 6. 서버 구성도
![서버_구성도](https://user-images.githubusercontent.com/22374750/65835236-c24f1f00-e31e-11e9-9dd8-ebe68a378f23.png)
