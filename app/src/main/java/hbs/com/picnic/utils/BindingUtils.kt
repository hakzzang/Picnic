package hbs.com.picnic.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import hbs.com.picnic.R
import hbs.com.picnic.data.model.TourDetail

@BindingAdapter("glide_url")
fun ImageView.setImageViewWithGlide(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    Glide.with(this)
        .load(url)
        .into(this)
}


@BindingAdapter("tour_guide")
fun TextView.setGuide(tourDetail: TourDetail?) {
    val tourDetailContent = tourDetail?.tourDetailResponse?.body?.tourDetailItems?.tourDetailContent ?: return
    text = when (tourDetailContent.contentTypeId.toInt()) {
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