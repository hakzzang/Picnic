package hbs.com.picnic.ui.bookmark.usecase

import hbs.com.picnic.data.local.LocalRepository
import hbs.com.picnic.data.local.LocalRepositoryImpl
import hbs.com.picnic.data.model.Bookmark

interface BookmarkUseCase {
    fun getBookMarks(): List<Bookmark>
}

class BookmarkUseCaseImpl() : BookmarkUseCase {
    private val localRepository = LocalRepositoryImpl()
    override fun getBookMarks(): List<Bookmark> {
        localRepository.realm.beginTransaction()
        val bookmarks = localRepository.selectAll(localRepository.realm)
        localRepository.realm.commitTransaction()
        return bookmarks
    }
}