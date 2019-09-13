package hbs.com.picnic.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hbs.com.picnic.data.model.ChatMessage

interface ChatRepository {
    fun getChats(roomId: String): CollectionReference
    fun postChats(roomId: String, chatMessage: ChatMessage)
}

class ChatRepositoryImpl : ChatRepository {
    private val fireDatabase = FirebaseFirestore.getInstance()
    private val ROOM_MESSAGES = "room-messages/"
    private val CHAT = "chat"

    override fun getChats(roomId: String) = fireDatabase
        .collection(ROOM_MESSAGES)
        .document(roomId)
        .collection(CHAT)

    override fun postChats(roomId: String, chatMessage: ChatMessage) {
        fireDatabase
            .collection(ROOM_MESSAGES)
            .document(roomId)
            .collection(CHAT)
            .document()
            .set(chatMessage.convertToMap())
    }
}