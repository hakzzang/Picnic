package hbs.com.picnic.data.local

import hbs.com.picnic.data.model.Bookmark
import io.realm.Realm

interface LocalRepository{
    fun remove(item: Bookmark)
    fun update(item: Bookmark, result: Bookmark)
    fun insert(realm: Realm, item: Bookmark)
    fun upsert(realm: Realm, item: Bookmark)
    fun select(realm: Realm, item: Bookmark): Bookmark?
    fun selectAll(realm: Realm, item: Bookmark): List<Bookmark>
}

class LocalRepositoryImpl: LocalRepository{
    val realm: Realm = Realm.getDefaultInstance()
    override fun insert(realm: Realm, item: Bookmark) {
        realm.createObject(item.javaClass, item.uniqueId).apply {
            title = item.title
            thumbnail = item.thumbnail
            makedAt = item.makedAt
            isBookmark = item.isBookmark
        }
    }

    override fun remove(item: Bookmark) {

    }

    override fun update(item: Bookmark, result: Bookmark) {
        result.apply {
            makedAt = item.makedAt
            isBookmark = item.isBookmark
            thumbnail = item.thumbnail
            title = item.title
        }
    }

    override fun upsert(realm: Realm, item: Bookmark) {
        val selectItem = select(realm, item)
        if(selectItem==null){
            insert(realm, item)
        }else{
            update(item, selectItem)
        }
    }

    override fun select(realm: Realm, item: Bookmark): Bookmark? {
        return realm.where(Bookmark::class.java).equalTo("uniqueId", item.uniqueId).findFirst()
    }

    override fun selectAll(realm: Realm, item: Bookmark): List<Bookmark> {
        return realm.where(Bookmark::class.java).equalTo("uniqueId", item.uniqueId).findAll()
    }
}