package hbs.com.picnic.utils

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(val orientation: Int, val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        when (orientation) {
            RecyclerView.VERTICAL -> {
                val space: Int = space/2
                outRect.top = space
                outRect.bottom = space
            }

            RecyclerView.HORIZONTAL -> {
                val space: Int = space/2
                outRect.right = space
                outRect.left = space
            }
        }

    }
}