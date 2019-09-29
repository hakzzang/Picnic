package hbs.com.picnic.ui.bookmark

import hbs.com.picnic.data.model.Bookmark

interface BookmarkContract {
    interface Presenter {
        fun initView()
        fun getBookmarks()
    }

    interface View {
        fun initView()
        fun updateBookmarkView(bookmarks: List<Bookmark>)
    }
}