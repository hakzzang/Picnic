package hbs.com.picnic.utils

import android.content.Context

class ResorceProvider(val context: Context){
    fun getString(resource:Int) = context.getString(resource)
}