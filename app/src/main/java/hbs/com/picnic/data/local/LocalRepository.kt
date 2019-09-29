package hbs.com.picnic.data.local

import hbs.com.picnic.data.model.Bookmark
import io.realm.Realm

interface LocalRepository{
    fun remove(realm: Realm, item: Bookmark)
    fun update(item: Bookmark, result: Bookmark)
    fun insert(realm: Realm, item: Bookmark)
    fun upsert(realm: Realm, item: Bookmark)
    fun select(realm: Realm, bookmarkId: String): Bookmark?
    fun selectAll(realm: Realm): List<Bookmark>
}

class LocalRepositoryImpl: LocalRepository{
    val realm: Realm = Realm.getDefaultInstance()
    override fun insert(realm: Realm, item: Bookmark) {
        realm.createObject(item.javaClass, item.uniqueId).apply {
            title = item.title
            thumbnail = item.thumbnail
            madeAt = item.madeAt
            isBookmark = item.isBookmark
        }
    }

    override fun remove(realm: Realm, item: Bookmark) {
        val bookmark = realm.where(Bookmark::class.java).equalTo("uniqueId", item.uniqueId).findAll()
        bookmark.deleteAllFromRealm()
    }

    override fun update(item: Bookmark, result: Bookmark) {
        result.apply {
            madeAt = item.madeAt
            isBookmark = item.isBookmark
            thumbnail = item.thumbnail
            title = item.title
        }
    }

    override fun upsert(realm: Realm, item: Bookmark) {
        val selectItem = select(realm, item.uniqueId)
        if(selectItem==null){
            insert(realm, item)
        }else{
            update(item, selectItem)
        }
    }

    override fun select(realm: Realm, bookmarkId: String): Bookmark? {
        return realm.where(Bookmark::class.java).equalTo("uniqueId", bookmarkId).findFirst()
    }

    override fun selectAll(realm: Realm): List<Bookmark> {
        return realm.where(Bookmark::class.java).findAll()
    }
}