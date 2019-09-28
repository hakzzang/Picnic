package hbs.com.picnic.ui.bookmark.presenter

import hbs.com.picnic.ui.bookmark.BookmarkContract
import hbs.com.picnic.ui.bookmark.usecase.BookmarkUseCaseImpl
import hbs.com.picnic.utils.BaseContract

class BookmarkPresenter(private val bookmarkView: BookmarkContract.View) : BookmarkContract.Presenter, BaseContract.Presenter() {
    private val bookmarkUseCase = BookmarkUseCaseImpl()

    override fun initView() {
        bookmarkView.initView()
    }

    override fun getBookmarks(uniqueId: String) {
        bookmarkView.updateBookmarkView(bookmarkUseCase.getBookMarks(uniqueId))
    }


}