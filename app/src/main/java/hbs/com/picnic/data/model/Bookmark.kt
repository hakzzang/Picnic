package hbs.com.picnic.data.model

import io.realm.RealmObject

data class Bookmark(
    val title: String,
    val thumbnail: String,
    val date: String,
    val isBookmark: Boolean
) : RealmObject()