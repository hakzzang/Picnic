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
        TourType.FOOD.value -> tourDetailContent.openTimeFood+"\n" +"쉬는 날: "+tourDetailContent.restDateFood + "\n대표 메뉴: "+tourDetailContent.firstMenu
        TourType.SHOPPING.value -> tourDetailContent.fairDay + "(${tourDetailContent.openDateShopping})" + "\n" + tourDetailContent.openTime + "\n" +
                "쉬는 날: " + tourDetailContent.restDateShopping
        TourType.REPORTS.value -> tourDetailContent.useTimeLeports + "\n" + tourDetailContent.openPeriod + "\n" + "쉬는 날: " + tourDetailContent.restDateLeports
        TourType.TRAVEL.value -> tourDetailContent.takeTime + "(${tourDetailContent.distance})" + "\n" + tourDetailContent.schedule
        TourType.FESTIVAL.value -> tourDetailContent.eventStartDate + "~" + tourDetailContent.eventEndDate + "\n" + tourDetailContent.playtime
        TourType.CULTURE.value -> tourDetailContent.useTimeCulture + " (${tourDetailContent.spendTime})" + "\n" +
                "쉬는 날: " + tourDetailContent.restDateCulture + "\n" + tourDetailContent.scale
        TourType.TOUR.value -> tourDetailContent.useSeason + " " + tourDetailContent.useTime + "\n" +
                "쉬는 날: " + tourDetailContent.restDate + "\n" + tourDetailContent.expGuide
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

    text = when (tourDetailContent.contentTypeId.toInt()) {
        TourType.FOOD.value -> tourDetailContent.infoCenterFood
        TourType.SHOPPING.value -> tourDetailContent.infoCenterShopping
        TourType.REPORTS.value -> tourDetailContent.infoCenterLeports
        TourType.TRAVEL.value -> tourDetailContent.infoCenter + "\n" + tourDetailContent.sponsor2 + ":" + tourDetailContent.sponsor2tel
        TourType.FESTIVAL.value -> tourDetailContent.bookingPlace
        TourType.CULTURE.value -> tourDetailContent.infoCenterCulture
        TourType.TOUR.value -> tourDetailContent.infoCenterTourCourse
        else -> ""
    }
}