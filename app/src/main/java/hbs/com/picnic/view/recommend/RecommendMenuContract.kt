package hbs.com.picnic.view.recommend

import hbs.com.picnic.utils.MenuType

interface RecommendMenuContract {
    interface View{
        fun setMenuList(menus:List<MenuType>)
        fun goToListMap(locations:String)
    }

    interface Presenter{
        fun clickMenu(type: MenuType)
        fun makeMenuList()
    }
}