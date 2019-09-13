package hbs.com.picnic.view.content

import androidx.recyclerview.widget.DiffUtil
import hbs.com.picnic.data.model.ChatMessage
import javax.annotation.Nullable

class ChattingDiffUtil(private val oldList: List<ChatMessage>, private val newList: List<ChatMessage>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition] === newList[newPosition]
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}