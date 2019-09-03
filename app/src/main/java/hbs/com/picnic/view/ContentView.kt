package hbs.com.picnic.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import hbs.com.picnic.R
import hbs.com.picnic.databinding.ViewContentBinding
import kotlinx.android.synthetic.main.item_content_subtitle.view.*
import kotlinx.android.synthetic.main.view_content.view.*

class ContentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        initView()
    }

    private fun initView(){
        ViewContentBinding.inflate(LayoutInflater.from(context), this, true).let {
            it.rvContents.apply {
                adapter = ContentAdapter()
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}