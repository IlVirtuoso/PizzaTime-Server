import com.google.gson.Gson


class JsonRequest(val request: String, val body: String){
    companion object{
        fun <T> from(value : T){

        }
    }

    inline fun <reified T> As() : T{
        return Gson().fromJson(body, T::class.java)
    }
}

class JsonResponse(val request : String, val messageId : String, val body: String){

    inline fun <reified T> As() : T{
        return Gson().fromJson(body, T::class.java)
    }
}
