package hbs.com.picnic.utils

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(val orientation: Int, val space: Int) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position: Int = parent.getChildAdapterPosition(view)
        val itemCnt: Int = state.itemCount

        when (orientation) {
            RecyclerView.VERTICAL -> {
                val topRect: Int = if (position == 0) space else space / 2
                val bottomRect: Int = if (position == itemCnt - 1) space  else space/ 2

                outRect.top = topRect
                outRect.bottom = bottomRect
            }

            RecyclerView.HORIZONTAL -> {/*
                val leftRect: Int = if (position == 0) space else space / 2
                val rightRect: Int = if (position == itemCnt - 1) space  else space/ 2*/
//                val leftRect: Int = space
//                val rightRect: Int = if (position == itemCnt - 1) space  else 0
                val leftRect: Int = space
                val rightRect: Int = space

                outRect.left = leftRect
                outRect.right = rightRect
            }
        }

    }
}