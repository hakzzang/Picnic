package hbs.com.picnic.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedManager {
    val SHARED_NAME ="shared_picnic"

    private fun getManager(context: Context):SharedPreferences = context.getSharedPreferences(SHARED_NAME, MODE_PRIVATE)

    private fun getEditor(context:Context):SharedPreferences.Editor = getManager(context).edit()

    fun getBoolean(context:Context, key:String):Boolean = getManager(context).getBoolean(key, false)

    fun putBoolean(context:Context, key:String, value:Boolean){
        getEditor(context).putBoolean(key, value).commit()
    }
}