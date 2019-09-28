package hbs.com.picnic.data.local

import hbs.com.picnic.data.model.Bookmark
import io.realm.Realm

interface LocalRepository<T> where T : Bookmark {
    fun remove(item: T)
    fun update(item: T, result: T)
    fun insert(realm: Realm, item: T)
    fun upsert(realm: Realm, item: T)
    fun select(realm: Realm, item: T): T
    fun selectAll(realm: Realm, item: T): List<T>
}

class LocalRepositoryImpl<T : Bookmark> : LocalRepository<T> {
    val realm: Realm = Realm.getDefaultInstance()
    override fun insert(realm: Realm, item: T) {
        when (item) {
            is Bookmark -> {
                realm.createObject(item.javaClass, item.uniqueId).apply {
                    title = item.title
                    thumbnail = item.thumbnail
                    makedAt = item.makedAt
                    isBookmark = item.isBookmark
                }
            }
        }
    }

    override fun remove(item: T) {

    }

    override fun update(item: T, result: T) {
        when (item) {
            is Bookmark -> {
                (result as Bookmark).apply {
                    makedAt = item.makedAt
                    isBookmark = item.isBookmark
                    thumbnail = item.thumbnail
                    title = item.title
                }
            }
        }
    }

    override fun upsert(realm: Realm, item: T) {
        val selectItem = select(realm, item)
        if(selectItem==null){
            insert(realm, item)
        }else{
            update(item, selectItem as T)
        }
        /*select(realm, item)?.apply {
            update(item, this as T)
            if(this.realm.isInTransaction){
                this.realm.commitTransaction()
            }
        } ?: run {
            if(!this.realm.isInTransaction){
                this.realm.beginTransaction()
            }
            insert(realm, item)
        }*/
    }

    override fun select(realm: Realm, item: T): T {
        return realm.where(item.javaClass).equalTo("uniqueId", item.uniqueId).findFirst() as T
    }

    override fun selectAll(realm: Realm, item: T): List<T> {
        return realm.where(item.javaClass).equalTo("uniqueId", item.uniqueId).findAll()
    }
}