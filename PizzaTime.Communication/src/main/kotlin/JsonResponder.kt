import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.RpcServer
import com.rabbitmq.client.StringRpcServer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


class JsonResponder(channel: Channel, val maps : Map<String, (JsonRequest) -> JsonResponse>) : RpcServer(channel) {
    override fun handleCall(requestBody: ByteArray, replyProperties: AMQP.BasicProperties): ByteArray {
        val request=  Json.decodeFromString<JsonRequest>(requestBody.decodeToString());
        if(!maps.containsKey(request.request)){
            throw IllegalArgumentException("invalid request ${request.request}")
        }
        val response = maps[request.request]?.invoke(request);
        return Json.encodeToString(response).encodeToByteArray();
    }
}