package hbs.com.picnic.data.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class TourDetail(@SerializedName("response") var tourDetailResponse: TourDetailResponse? = null) {
    fun parseData(jsonString: String) = Gson().fromJson(jsonString, TourDetail::class.java)
}

data class TourDetailResponse(@SerializedName("header") val header: TourDetailHeader, @SerializedName("body") val body: TourDetailItems)

data class TourDetailHeader(@SerializedName("resultCode") val resultCode: Int, @SerializedName("resultMsg") val resultMsg: String)

data class TourDetailItems(@SerializedName("items") val tourDetailItems: TourDetailItem)

data class TourDetailItem(@SerializedName("item") val tourDetailContent: TourDetailContent)

data class TourDetailContent(
    @SerializedName("contenttypeid") val contentTypeId:String,

    @SerializedName("accomcount") val accomcount: String,
    @SerializedName("chkbabycarriage") val chkbabycarriage: String,
    @SerializedName("chkcreditcard") val chkcreditcard: String,
    @SerializedName("chkpet") val chkpet: String,
    @SerializedName("expagerange") val expAgeRange: String,
    @SerializedName("expguide") val expGuide: String,
    @SerializedName("infocenter") val infoCenter: String,
    @SerializedName("opendate") val openDate: String,
    @SerializedName("parking") val parking: String,
    @SerializedName("restdate") val restDate: String,
    @SerializedName("useseason") val useSeason: String,
    @SerializedName("usetime") val useTime: String,

    @SerializedName("chkbabycarriageculture") val chkbabycarriageculture: String,
    @SerializedName("chkcreditcardculture") val chkcreditcardculture: String,
    @SerializedName("chkpetculture") val chkpetculture: String,
    @SerializedName("accomcountculture") val accomcountCulture: String,
    @SerializedName("discountinfo") val disCountInfo: String,
    @SerializedName("infocenterculture") val infoCenterCulture: String,
    @SerializedName("parkingculture") val parkingCulture: String,
    @SerializedName("parkingfee") val parkingFee: String,
    @SerializedName("restdateculture") val restDateCulture: String,
    @SerializedName("usefee") val useFee: String,
    @SerializedName("usetimeculture") val useTimeCulture: String,
    @SerializedName("scale") val scale: String,
    @SerializedName("spendtime") val spendTime: String,

    @SerializedName("agelimit") val ageLimit: String,
    @SerializedName("bookingplace") val bookingPlace: String,
    @SerializedName("discountinfofestival") val discountInfoFestival: String,
    @SerializedName("eventenddate") val eventEndDate: String,
    @SerializedName("eventplace") val eventPlace: String,
    @SerializedName("eventstartdate") val eventStartDate: String,
    @SerializedName("festivalgrade") val festivalGrade: String,
    @SerializedName("placeinfo") val placeInfo: String,
    @SerializedName("playtime") val playtime: String,
    @SerializedName("program") val program: String,
    @SerializedName("usetimefestival") val useTimeFestival: String,
    @SerializedName("sponsor2") val sponsor2: String,
    @SerializedName("sponsor2tel") val sponsor2tel: String,

    @SerializedName("distance") val distance: String,
    @SerializedName("infocentertourcourse") val infoCenterTourCourse: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("taketime") val takeTime: String,
    @SerializedName("theme") val theme: String,

    @SerializedName("accomcountleports") val accomcountLeports: String,
    @SerializedName("expagerangeleports") val expagerangeLeports: String,
    @SerializedName("infocenterleports") val infoCenterLeports: String,
    @SerializedName("openperiod") val openPeriod: String,
    @SerializedName("parkingfeeleports") val parkingfeeLeports: String,
    @SerializedName("parkingleports") val parkingLeports: String,
    @SerializedName("reservation") val reservation: String,
    @SerializedName("restdateleports") val restDateLeports: String,
    @SerializedName("scaleleports") val scaleLeports: String,
    @SerializedName("usefeeleports") val useFeeLeports: String,
    @SerializedName("usetimeleports") val useTimeLeports: String,

    @SerializedName("fairday") val fairDay: String,
    @SerializedName("infocentershopping") val infoCenterShopping: String,
    @SerializedName("opendateshopping") val openDateShopping: String,
    @SerializedName("opentime") val openTime: String,
    @SerializedName("parkingshopping") val parkingShopping: String,
    @SerializedName("restdateshopping") val restDateShopping: String,
    @SerializedName("saleitem") val saleItem: String,
    @SerializedName("saleitemcost") val saleItemCost: String,
    @SerializedName("scaleshopping") val scaleShopping: String,
    @SerializedName("shopguide") val shopGuide: String,

    @SerializedName("discountinfofood") val discountInfoFood: String,
    @SerializedName("firstmenu") val firstMenu: String,
    @SerializedName("infocenterfood") val infoCenterFood: String,
    @SerializedName("opentimefood") val openTimeFood: String,
    @SerializedName("parkingfood") val parkingFood: String,
    @SerializedName("restdatefood") val restDateFood: String,
    @SerializedName("scalefood") val scaleFood: String,
    @SerializedName("seat") val seat: String,
    @SerializedName("treatmenu") val treatMenu: String
) {
    //Tour
    /*수용인원	contentTypeId=12
    (관광지)	수용인원
        accomcountculture	수용인원
        expagerange	체험가능연령	체험가능연령
        expguide	체험안내	체험안내
        infocenter	문의및안내	문의및안내
        opendate	개장일	개장일
        parking	주차시설	주차시설
        restdate	쉬는날	쉬는날
        useseason	이용시기	이용시기
        usetime	이용시간)*/


    /* Culture
    * accomcountculture	수용인원
        discountinfo	할인정보	할인정보
        infocenterculture	문의및안내	문의및안내
        parkingculture	주차시설	주차시설
        parkingfee	주차요금	주차요금
        restdateculture	쉬는날	쉬는날
        usefee	이용요금	이용요금
        usetimeculture	이용시간	이용시간
        scale	규모	규모
        spendtime	관람소요시간
*/

    /* Festival
      * agelimit	관람가능연령	contentTypeId=15
        (행사/공연/축제)	관람가능연령
        bookingplace	예매처	예매처
        discountinfofestival	할인정보	할인정보
        eventenddate	행사종료일	행사종료일
        eventplace	행사장소	행사장소
        eventstartdate	행사시작일	행사시작일
        festivalgrade	축제등급	축제등급
        placeinfo	행사장위치안내	행사장위치안내
        playtime	공연시간	공연시간
        program	행사프로그램	행사프로그램
        spendtimefestival	관람소요시간	관람소요시간
        usetimefestival	이용요금
        sponsor2	주관사정보	주관사정보
        sponsor2tel	주관사연락처
*/

    /* Travel
        distance	코스총거리
        infocentertourcourse	문의및안내	문의및안내
        schedule	코스일정	코스일정
        taketime	코스총소요시간	코스총소요시간
        theme	코스테마
    */

    //Reports
    /*accomcountleports	수용인원
        expagerangeleports	체험가능연령	체험가능연령
        infocenterleports	문의및안내	문의및안내
        openperiod	개장기간	개장기간
        parkingfeeleports	주차요금	주차요금
        parkingleports	주차시설	주차시설
        reservation	예약안내	예약안내
        restdateleports	쉬는날	쉬는날
        scaleleports	규모	규모
        usefeeleports	입장료	입장료
        usetimeleports	이용시간	이용시간
    */

    /* Shopping
        fairday	장서는날	장서는날
        infocentershopping	문의및안내	문의및안내
        opendateshopping	개장일	개장일
        opentime	영업시간	영업시간
        parkingshopping	주차시설	주차시설
        restdateshopping	쉬는날	쉬는날
        saleitem	판매품목	판매품목
        saleitemcost	판매품목별가격	판매품목별가격
        scaleshopping	규모	규모
        shopguide	매장안내	매장안내
    */

    /*
    * Food
discountinfofood	할인정보	할인정보
firstmenu	대표메뉴	대표메뉴
infocenterfood	문의및안내	문의및안내
opentimefood	영업시간	영업시간
parkingfood	주차시설	주차시설
reservationfood	예약안내	예약안내
restdatefood	쉬는날	쉬는날
scalefood	규모	규모
seat	좌석수	좌석수
treatmenu	취급메뉴	취급메뉴
*/
}