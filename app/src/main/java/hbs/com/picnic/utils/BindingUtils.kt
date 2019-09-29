package hbs.com.picnic.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import hbs.com.picnic.data.model.TourDetail
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("glide_url")
fun ImageView.setImageViewWithGlide(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    Glide.with(this)
        .load(url)
        .into(this)
}

@BindingAdapter("text_date")
fun TextView.setTextViewDate(madeAt: String?) {
    if (madeAt.isNullOrEmpty()) {
        return
    }
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일, HH시 mm분")
    val resultDate = Date(madeAt.toLong())
    text = sdf.format(resultDate)
}

@BindingAdapter("tour_guide")
fun TextView.setGuide(tourDetail: TourDetail?) {
    val tourDetailContent = tourDetail?.tourDetailResponse?.body?.tourDetailItems?.tourDetailContent ?: return
    val htmlText = when (tourDetailContent.contentTypeId.toInt()) {
        TourType.FOOD.value -> "개장 시간:" + checkNullData(tourDetailContent.openTimeFood) +
                "<br/>" + "쉬는 날: " + checkNullData(tourDetailContent.restDateFood) +
                "<br/>대표 메뉴: " + checkNullData(tourDetailContent.firstMenu) +
                "<br/>취급 메뉴: " + checkNullData(tourDetailContent.treatMenu)
        TourType.SHOPPING.value -> "장이 있는 날:" + checkNullData(tourDetailContent.fairDay) + "<br/>" +
                "개장 일: " + checkNullData(tourDetailContent.openDateShopping) + "<br/>" +
                "오픈 시간: " + checkNullData(tourDetailContent.openTime) + "<br/>" +
                "쉬는 날: " + checkNullData(tourDetailContent.restDateShopping)
        TourType.REPORTS.value -> "이용 시간: " + checkNullData(tourDetailContent.useTimeLeports) + "<br/>" +
                "개장 기간:" + checkNullData(tourDetailContent.openPeriod) + "<br/>" +
                "쉬는 날: " + checkNullData(tourDetailContent.restDateLeports)
        TourType.TRAVEL.value -> "코스 이용 시간: " + checkNullData(tourDetailContent.takeTime) + "<br/>" +
                "코스 총 거리: " + checkNullData(tourDetailContent.distance) + "<br/>" +
                "코스 일정: " + checkNullData(tourDetailContent.schedule)
        TourType.FESTIVAL.value -> "행사 시작 일: " + checkNullData(tourDetailContent.eventStartDate) + "<br/>" +
                "행사 끝나는 날: " + checkNullData(tourDetailContent.eventEndDate) + "<br/>" +
                "공연 시간: " + checkNullData(tourDetailContent.playtime)
        TourType.CULTURE.value -> "이용시간: " + checkNullData(tourDetailContent.useTimeCulture) + "<br/>" +
                "관람소요시간: " + checkNullData(tourDetailContent.spendTime) + "<br/>" +
                "쉬는 날: " + checkNullData(tourDetailContent.restDateCulture) + "<br/>" +
                "규모: " + checkNullData(tourDetailContent.scale)
        TourType.TOUR.value -> "이용시기: " + checkNullData(tourDetailContent.useSeason) + "<br/>" +
                "이용시간: " + checkNullData(tourDetailContent.useTime) + "<br/>" +
                "쉬는 날: " + checkNullData(tourDetailContent.restDate) + "<br/>" +
                "체험안내: " + checkNullData(tourDetailContent.expGuide)
        else -> ""
    }
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
        // we are using this flag to give a consistent behaviour
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }
}

@BindingAdapter("tour_info")
fun TextView.setTourInfo(tourDetail: TourDetail?) {
    val tourDetailContent = tourDetail?.tourDetailResponse?.body?.tourDetailItems?.tourDetailContent ?: return

    val htmlText = when (tourDetailContent.contentTypeId.toInt()) {
        TourType.FOOD.value -> "문의: " + checkNullData(tourDetailContent.infoCenterFood) + "<br/>예약 안내: " +
                checkNullData(tourDetailContent.reservationfood) + "<br/>주차시설: " +
                checkNullData(tourDetailContent.parkingFood)
        TourType.SHOPPING.value -> "문의: " + checkNullData(tourDetailContent.infoCenterShopping) + "<br/>주차시설: " +
                checkNullData(tourDetailContent.parkingShopping) + "<br/>매장안내: " +
                checkNullData(tourDetailContent.shopGuide)
        TourType.REPORTS.value -> "요금: " + checkNullData(tourDetailContent.useFeeLeports) + "<br/>문의: " +
                checkNullData(tourDetailContent.infoCenterLeports) + "<br/>주차시설: " +
                checkNullData(tourDetailContent.parkingLeports) + "<br/>주차요금: " +
                checkNullData(tourDetailContent.parkingfeeLeports)
        TourType.TRAVEL.value -> "문의: " + checkNullData(tourDetailContent.infoCenterTourCourse)
        TourType.FESTIVAL.value -> "문의: " + checkNullData(tourDetailContent.bookingPlace) +
                "<br/>주최자: " + checkNullData(tourDetailContent.sponsor1) +
                "<br/>연락처:" + checkNullData(tourDetailContent.sponsor1tel) +
                "<br/>추가행사:" + checkNullData(tourDetailContent.subevent)
        TourType.CULTURE.value -> "문의: " + checkNullData(tourDetailContent.infoCenterCulture) +
                "<br/>주차시설: " + checkNullData(tourDetailContent.parkingCulture) +
                "<br/>주차요금: " + checkNullData(tourDetailContent.parkingFee) +
                "<br/>할인정보: " + checkNullData(tourDetailContent.disCountInfo)
        TourType.TOUR.value -> "문의: " + checkNullData(tourDetailContent.infoCenterTourCourse) +
                "<br/>주차시설: " + checkNullData(tourDetailContent.parking)
        else -> ""
    }

    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
        // we are using this flag to give a consistent behaviour
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }

}

fun checkNullData(content: String?): String {
    return if (content.isNullOrEmpty()) {
        "정보 없음"
    } else {
        content
    }
}