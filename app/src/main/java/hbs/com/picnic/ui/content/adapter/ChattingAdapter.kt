package hbs.com.picnic.ui.content.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hbs.com.picnic.data.model.ChatMessage
import hbs.com.picnic.utils.AuthManager
import kotlinx.android.synthetic.main.item_chatting_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*


class ChattingAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(ChattingDiffCallBack()) {
    private val simpleDataFormat = SimpleDateFormat("HH:mm")
    private val MY_CHATTING_VIEWHOLDER = 0
    private val YOUR_CHATTING_VIEWHOLDER = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MY_CHATTING_VIEWHOLDER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(hbs.com.picnic.R.layout.item_your_chatting_dialog, parent, false)
            ChattingMyViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(hbs.com.picnic.R.layout.item_chatting_dialog, parent, false)
            ChattingViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        AuthManager.getUserId()?.let { myUserId ->
            return if (getItem(position).userId == myUserId) {
                MY_CHATTING_VIEWHOLDER
            } else {
                YOUR_CHATTING_VIEWHOLDER
            }
        }
        return YOUR_CHATTING_VIEWHOLDER
    }

    fun setData(newChattingList: List<ChatMessage>) {
        submitList(newChattingList)
        /*val diffCallback = ChattingDiffUtil(chattingList, newChattingList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        chattingList.clear()
        chattingList.addAll(newChattingList)
        diffResult.dispatchUpdatesTo(this)*/
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChattingViewHolder) {
            holder.bindView(position)
        } else if (holder is ChattingMyViewHolder) {
            holder.bindView(position)
        }
    }

    inner class ChattingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(position: Int) {
            itemView.tv_chat_dialog.text = getItem(position).message
            itemView.tv_profile_name.text = getItem(position).name

            itemView.tv_chat_timestamp.apply {
                text = simpleDataFormat.format(Date(getItem(position).timestamp.toLong()))
            }
            Glide
                .with(itemView.iv_profile_image)
                .load(hbs.com.picnic.R.drawable.shrimp_burger)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.iv_profile_image)
        }
    }

    inner class ChattingMyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(position: Int) {
            itemView.tv_chat_dialog.text = getItem(position).message
            itemView.tv_chat_timestamp.apply {
                text = simpleDataFormat.format(Date(getItem(position).timestamp.toLong()))
            }
        }
    }

    class ChattingDiffCallBack : DiffUtil.ItemCallback<ChatMessage>(){
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean = oldItem == newItem

    }
}