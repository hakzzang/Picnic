package hbs.com.picnic.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Bookmark(
    var title: String = "",
    var thumbnail: String = "",
    var makedAt: String = "",
    @PrimaryKey var uniqueId: String = "",
    var isBookmark: Boolean = false
) : RealmObject()