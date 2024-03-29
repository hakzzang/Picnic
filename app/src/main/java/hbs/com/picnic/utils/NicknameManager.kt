package hbs.com.picnic.utils

import android.util.Log
import kotlin.math.abs

class NicknameManager {
    private val koreanAdjectives = arrayListOf<String>(
        "가냘픈",
        "가는",
        "가득찬",
        "가엾은",
        "가파른",
        "같은",
        "거센",
        "거친",
        "검은",
        "게으른",
        "게을러빠진",
        "게을러터진",
        "고달픈",
        "고른",
        "고마운",
        "고운",
        "고장난",
        "고픈",
        "곧은",
        "괜찮은",
        "구석진",
        "굳은",
        "굵은",
        "귀여운",
        "그런",
        "그렇게",
        "그른",
        "그리운",
        "기다란",
        "기쁜",
        "긴",
        "깊은",
        "깎아지른",
        "깨끗한",
        "나쁜",
        "나은",
        "난데없는",
        "날랜",
        "날카로운",
        "낮아지다",
        "낮은",
        "너그러운",
        "너른",
        "널따란",
        "넓은",
        "네모난",
        "노란",
        "놀란",
        "높은",
        "누런",
        "눅은",
        "느닷없는",
        "느린",
        "늦은",
        "더러운",
        "더운",
        "덜된",
        "동그란",
        "돼먹잖은",
        "된",
        "둥그런",
        "둥근",
        "뒤늦은",
        "드문",
        "딱한",
        "때늦은",
        "뛰어난",
        "뜨거운",
        "막다른",
        "많은",
        "맛있는",
        "매운",
        "먼",
        "멂",
        "멋진",
        "메마른",
        "메스꺼운",
        "모난",
        "못난",
        "못된",
        "못생긴",
        "무거운",
        "무딘",
        "무른",
        "무서운",
        "미끈미끈한",
        "미운",
        "바람직스러운",
        "반가운",
        "밝은",
        "밤늦은",
        "보드라운",
        "보람찬",
        "보잘것없는",
        "부드러운",
        "부른",
        "붉은",
        "비싼",
        "빠른",
        "빨간",
        "뻘건",
        "뼈저린",
        "뽀얀",
        "뿌연",
        "새로운",
        "서운한",
        "서툰",
        "섣부른",
        "설운",
        "섦",
        "성가신",
        "센",
        "수다스러운",
        "수줍은",
        "쉬운",
        "스스러운",
        "슬픈",
        "시원찮은",
        "싫은",
        "싱싱한",
        "싼",
        "쌀쌀맞은",
        "쏜살같은",
        "쓰디쓴",
        "쓰린",
        "쓴",
        "아니꼬운",
        "아닌",
        "아득해지다",
        "아름다운",
        "아름다운",
        "아름다워지다",
        "아쉬운",
        "아픈",
        "안된",
        "안쓰러운",
        "안타까운",
        "않은",
        "알맞은",
        "약빠른",
        "약은",
        "얇은",
        "어두운",
        "어려운",
        "어린",
        "언짢은",
        "엄청난",
        "없는",
        "여문",
        "열띤",
        "영",
        "예쁜",
        "오램",
        "올바른",
        "옳은",
        "외로운",
        "우스운",
        "의심쩍은",
        "이렇게",
        "이른",
        "이상한",
        "익은",
        "있는",
        "잔",
        "잘난",
        "잘빠진",
        "잘생긴",
        "재미있는",
        "저렇게",
        "적은",
        "젊은",
        "점잖은",
        "조그만",
        "좁은",
        "좋은",
        "주제넘은",
        "줄기찬",
        "즐거운",
        "지나친",
        "지저분한",
        "지혜로운",
        "진",
        "질긴",
        "짓궂은",
        "짙은",
        "짠",
        "짧음",
        "케케묵은",
        "탐스러운",
        "턱없는",
        "푸른",
        "하나같은",
        "한결같은",
        "행복한",
        "황당한",
        "흐린",
        "희망찬",
        "흰",
        "힘겨운",
        "힘찬"
    )

    private val koreanWords = arrayListOf<String>(
        "가구",
        "가로수",
        "간호사",
        "강당",
        "개나리",
        "겁나다",
        "게시판",
        "고구마",
        "고등학생",
        "골목길",
        "공기",
        "공주",
        "공중전화",
        "공휴일",
        "과외",
        "관람객",
        "교내",
        "국기",
        "그래픽",
        "근교",
        "금메달",
        "금요일",
        "기업인",
        "기침",
        "김밥",
        "깡패",
        "낚시꾼",
        "낚싯대",
        "남동생",
        "노랫소리",
        "노트",
        "놀이터",
        "다양성",
        "단골",
        "달러",
        "닭고기",
        "답장",
        "대상자",
        "대입",
        "대학교수",
        "도마",
        "동그라미",
        "동화책",
        "등산로",
        "딸기",
        "땅바닥",
        "떠나오다",
        "레몬",
        "레이저",
        "마라톤",
        "마사지",
        "마요네즈",
        "마중",
        "만화가",
        "매스컴",
        "몸살",
        "미움",
        "바가지",
        "바나나",
        "박스",
        "발등",
        "밤색",
        "밥맛",
        "밥솥",
        "배꼽",
        "배드민턴",
        "배부르다",
        "배우자",
        "버튼",
        "베개",
        "벨트",
        "병실",
        "보수적",
        "복사",
        "볶음밥",
        "부잣집",
        "부채",
        "불법",
        "비만",
        "비행장",
        "빗방울",
        "사업가",
        "사전",
        "사진기",
        "사회자",
        "상금",
        "상추",
        "샤워",
        "성별",
        "세탁",
        "세탁기",
        "소나기",
        "소시지",
        "손뼉",
        "손수",
        "손수건",
        "손잡이",
        "송아지",
        "송편",
        "쇼핑",
        "수고",
        "수도꼭지",
        "수돗물",
        "수만",
        "수업",
        "수요일",
        "수입품",
        "수저",
        "숟가락",
        "술병",
        "술자리",
        "스케줄",
        "스키",
        "습기",
        "시금치",
        "시들다",
        "시디",
        "식기",
        "식욕",
        "신사",
        "신청서",
        "신혼부부",
        "싼값",
        "쓰레기통",
        "안부",
        "앨범",
        "약국",
        "양배추",
        "양보",
        "양주",
        "어린이날",
        "엉터리",
        "여왕",
        "엽서",
        "영화배우",
        "예보",
        "오른발",
        "옥상",
        "온라인",
        "외갓집",
        "요일",
        "욕실",
        "용서",
        "우편",
        "운동화",
        "운전기사",
        "원숭이",
        "위험성",
        "음악가",
        "인사말",
        "인삼",
        "인상",
        "일기",
        "일자리",
        "입사",
        "자매",
        "자판기",
        "잔디밭",
        "장마",
        "장모님",
        "장사꾼",
        "재학",
        "저울",
        "전기",
        "전문직",
        "전시장",
        "전화기",
        "절약",
        "정답",
        "정오",
        "정장",
        "주일",
        "지방",
        "지폐",
        "직장인",
        "진달래",
        "진심",
        "채점",
        "책가방",
        "천국",
        "천재",
        "초등학교",
        "초보",
        "초콜릿",
        "촛불",
        "추측",
        "충고",
        "치과",
        "치약",
        "코피",
        "타자기",
        "터미널",
        "통역",
        "포도",
        "포인트",
        "피자",
        "학과",
        "할인",
        "혼잣말",
        "효도"
    )

    //userId는 8자리 이상이여야 한다.
    fun makeWording(userId: String): String {
        val positiveHash = abs(userId.hashCode())
        val hashCode = String.format("%012d", positiveHash)
        val firstIndex = hashCode.substring(0, 3).toInt() % 200
        val secondIndex = hashCode.substring(3, 6).toInt() % 200
        val thirdIndex = hashCode.substring(6, 9)

        return koreanAdjectives[firstIndex] + " " + koreanWords[secondIndex] + "-" + thirdIndex
    }
}