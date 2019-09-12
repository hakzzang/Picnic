package hbs.com.picnic.data.model

data class ChatMessage(
    var userId: String = "",
    var name: String = "",
    var message: String = "",
    var timestamp: String = ""
) {
    fun convertToMap(): HashMap<String, Any> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["userId"] = userId
        hashMap["name"] = name
        hashMap["message"] = message
        hashMap["timestamp"] = timestamp
        return hashMap
    }

    fun convertMapTo(receiveMap: Map<String, Any>) {
        receiveMap["name"]?.apply {
            name = this.toString()
        }
        receiveMap["message"]?.apply {
            message = this.toString()
        }
        receiveMap["userId"]?.apply {
            userId = this.toString()
        }
        receiveMap["timestamp"]?.apply {
            timestamp = this.toString()
        }
    }
}