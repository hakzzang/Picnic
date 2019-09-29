package hbs.com.picnic.ui.bookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hbs.com.picnic.R
import hbs.com.picnic.data.model.Bookmark
import hbs.com.picnic.ui.bookmark.adapter.BookmarkAdapter
import hbs.com.picnic.ui.bookmark.presenter.BookmarkPresenter
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookmarkActivity : AppCompatActivity(), BookmarkContract.View {
    private val bookmarkAdapter = BookmarkAdapter()
    private val bookmarkPresenter = BookmarkPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        bookmarkPresenter.initView()
        bookmarkPresenter.getBookmarks()
    }

    override fun initView() {
        rv_bookmark.apply {
            adapter = bookmarkAdapter
            layoutManager = LinearLayoutManager(this@BookmarkActivity)
        }

        iv_toggle.setOnClickListener {
            finish()
        }
    }

    override fun updateBookmarkView(bookmarks: List<Bookmark>) {
        bookmarkAdapter.submitList(bookmarks)
    }
}