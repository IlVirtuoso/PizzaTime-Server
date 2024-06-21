import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonTransformingSerializer

class JsonRequest(val request: String, val body: String){
    companion object{
        fun <T> from(value : T){

        }
    }

    inline fun <reified T> As() : T{
        return Json.decodeFromString(body);
    }
}

class JsonResponse(val request : String, val messageId : String, val body: String){

    inline fun <reified T> As() : T{
        return Json.decodeFromString(body);
    }
}
