import com.rabbitmq.client.*
import com.rabbitmq.client.RpcClient.Response
import com.rabbitmq.client.impl.AMQBasicProperties
import com.rabbitmq.tools.jsonrpc.DefaultJsonRpcMapper
import com.rabbitmq.tools.jsonrpc.JacksonJsonRpcMapper
import com.rabbitmq.tools.jsonrpc.JsonRpcClient
import com.rabbitmq.tools.jsonrpc.JsonRpcMapper
import com.rabbitmq.tools.jsonrpc.JsonRpcMapper.JsonRpcRequest
import com.rabbitmq.tools.jsonrpc.JsonRpcServer
import com.rabbitmq.tools.jsonrpc.ServiceDescription
import java.util.concurrent.CountDownLatch
import kotlin.test.Test

class RPCTest {

    val mockCommunicationService = MockCommunicationService();

    fun rpcserver(latch: CountDownLatch) {

        class SimpleRpc(channel: Channel?) : StringRpcServer(channel) {
            override fun handleStringCall(request: String?): String {
                if (request == "Move") {
                    return "MoveDone";
                } else if (request == "Exit") {
                    this.terminateMainloop();
                    return "Terminated";
                }
                return "";
            }
        }

        val server = SimpleRpc(mockCommunicationService.channel)
        val channel = mockCommunicationService.channel;
        channel.exchangeDeclare("rpc", "direct")
        channel.queueBind(server.queueName, "rpc", "commands");
        server.mainloop();
        latch.countDown();
    }

    fun rpcclient(latch: CountDownLatch) {
        val channel = mockCommunicationService.channel;
        val replyQueue = channel.queueDeclare("intent", false, false, true, null).queue


        var client = RpcClient(
            RpcClientParams().channel(mockCommunicationService.channel)
                .exchange("rpc")
                .routingKey("commands")
                .replyHandler { t ->
                    var response = t as Response;
                    println(response.body.decodeToString())
                    return@replyHandler response;
                }
        )


        client.doCall(null, "Move".encodeToByteArray());
        client.doCall(null,"Exit".encodeToByteArray());

        latch.countDown();

    }


    @Test
    fun testRpc() {
        val globalLatch = CountDownLatch(2)
        Thread { rpcserver(globalLatch) }.start()
        Thread { rpcclient(globalLatch) }.start()
        globalLatch.await()
    }


    fun jsonServer(latch: CountDownLatch) {

    }

    fun jsonClient(latch: CountDownLatch) {

    }

    @Test
    fun testJsonRpc(){
        val globalLatch = CountDownLatch(2);

        Thread{}

        globalLatch.await();
    }
}