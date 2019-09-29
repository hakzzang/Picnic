package hbs.com.picnic.utils

import android.util.Log
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import hbs.com.picnic.data.model.TourInfo
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.Reader
import java.io.StringReader

object XmlParser {
    fun xmlToToJson(xml: String): JSONObject? {
        val xmlToJson = XmlToJson.Builder(xml).build();
        return xmlToJson.toJson()
    }

    fun tourParser(xml: String): TourInfo {
        var tourInfo: TourInfo = TourInfo()
        val tourItemInfos: ArrayList<TourInfo.TourItemInfo>? = arrayListOf()

        var tourItemInfo: TourInfo.TourItemInfo? = null

        val parser: XmlPullParser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xml) as Reader?)

        var type = parser.eventType
        var value: String = ""
        while (type != XmlPullParser.END_DOCUMENT) {
            val tagname = parser.name
            when (type) {
                XmlPullParser.START_TAG ->
                    if (tagname == "item")
                        tourItemInfo = TourInfo.TourItemInfo()
                XmlPullParser.TEXT -> value = parser.text
                XmlPullParser.END_TAG ->
                    when (tagname) {
                        "item" ->
                            tourItemInfo?.let { tourItem ->
                                if(tourItemInfo.firstimage.isNotEmpty()){
                                    tourItemInfos?.add(tourItem)
                                }
                            }
                        "items" -> {
                            tourInfo.datas = tourItemInfos
                        }
                        "contentid" ->
                            tourItemInfo?.contentid = Integer.parseInt(value)
                        "contenttypeid" -> {
                            tourItemInfo?.contenttypeid = Integer.parseInt(value)
                            tourInfo.title = convertTitle(Integer.parseInt(value))
                        }
                        "mapx" ->
                            tourItemInfo?.mapx = value.toDouble()
                        "mapy" ->
                            tourItemInfo?.mapy = value.toDouble()
                        "tel" ->
                            tourItemInfo?.tel = value
                        "title" ->
                            tourItemInfo?.title = value
                        "cat2" ->
                            tourItemInfo?.cat2 = value
                        "firstimage" ->{
                            Log.d("XmlParser", value)
                            tourItemInfo?.firstimage = value
                        }
                        "firstimage2" ->{
                            Log.d("XmlParser2", value)
                            tourItemInfo?.firstimage2 = value
                        }
                    }
            }
            type = parser.next()
        }
        return tourInfo
    }

    private fun convertTitle(contentId:Int):String=
        when(contentId){
            TourType.FOOD.value->"맛있는 피크닉"
            TourType.SHOPPING.value->"쇼핑 피크닉"
            TourType.REPORTS.value->"레포츠 피크닉"
            TourType.TRAVEL.value->"여행 피크닉"
            TourType.FESTIVAL.value->"축제 피크닉"
            TourType.CULTURE.value->"문화 피크닉"
            TourType.TOUR.value->"관광 피크닉"
            else -> "오늘도 즐거운 피크닉♥"
        }
}