package hbs.com.picnic.utils

import hbs.com.picnic.R

enum class MenuType(val title:String, val fileName: String, val icon:Int) {
    DDUCK("떡 카페", "seoul_dduck.csv", R.drawable.ic_ricecake_line),
    HANOK("한옥나들이", "seoul_dduck.csv", R.drawable.ic_palace_black_line),
    HANBOK("한복체험","seoul_dduck.csv", R.drawable.ic_hanbok_line),
    KIDS("키즈카페", "seoul_kids.csv", R.drawable.ic_lego_line),
    FOOD_TRUCK("푸드트럭","seoul_foodtruck.csv", R.drawable.ic_foodtruck_line),
    ICECREAM("아이스크림", "seoul_icecream.csv", R.drawable.ic_ice_line),
    MARKET("시장거리", "seoul_market.csv", R.drawable.ic_store_line),
    ROAD("길 거리", "seoul_dduck.csv", R.drawable.ic_ricecake_line),
    PARK("공원산책", "seoul_park.csv", R.drawable.ic_ricecake_line)
}