package hbs.com.picnic.content.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hbs.com.picnic.R
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.view.content.ChattingDiffUtil
import kotlinx.android.synthetic.main.item_chatting_dialog.view.*

class ChattingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val chattingList: ArrayList<ChatMessage> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting_dialog, parent, false)
        return ChattingViewHolder(view)
    }

    override fun getItemCount(): Int = chattingList.size

    fun setData(newChattingList: List<ChatMessage>) {
        val diffCallback = ChattingDiffUtil(chattingList, newChattingList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        chattingList.clear()
        chattingList.addAll(newChattingList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chattingViewHolder = holder as ChattingViewHolder
        chattingViewHolder.itemView.tv_chat_dialog.text = chattingList[position].message

        Glide
            .with(chattingViewHolder.itemView.iv_profile_image)
            .load(R.drawable.shrimp_burger)
            .apply(RequestOptions.circleCropTransform())
            .into(chattingViewHolder.itemView.iv_profile_image)
    }

    inner class ChattingViewHolder(view: View) : RecyclerView.ViewHolder(view)
}