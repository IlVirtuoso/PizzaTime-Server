import com.rabbitmq.client.*
import com.rabbitmq.client.impl.AMQBasicProperties
import java.util.concurrent.CountDownLatch
import kotlin.test.Test

class RPCTest {

    val mockCommunicationService = MockCommunicationService();

    fun rpcserver(latch : CountDownLatch){

        class SimpleRpc(channel: Channel?) : StringRpcServer(channel) {
            override fun handleStringCall(request: String?): String {
                if(request == "Move"){
                    return "MoveDone";
                }
                else if (request == "Exit"){
                    terminateMainloop();
                    return "Terminated";
                }
                return "";
            }
        }

        val server = SimpleRpc(mockCommunicationService.channel)
        val channel = mockCommunicationService.channel;
        channel.exchangeDeclare("rpc","direct")
        channel.queueBind(server.queueName,"rpc","commands");
        server.mainloop();
        latch.countDown();
    }

    fun rpcclient(latch : CountDownLatch){
        val channel = mockCommunicationService.channel;
        val replyQueue = channel.queueDeclare("intent",false,false,true,null).queue
        var client = RpcClient(
            RpcClientParams().channel(mockCommunicationService.channel)
                .exchange("rpc")
                .routingKey("commands")
                .replyTo(replyQueue)
        )

        channel.basicConsume(replyQueue,
            DeliverCallback{consumerTag, message ->  println(message.body.decodeToString())},
            CancelCallback {  }
        )
        client.doCall(null, "Move".encodeToByteArray());
        client.doCall(null,"Exit".encodeToByteArray());
        latch.countDown();
    }



    @Test
    fun testRpc(){
        val globalLatch = CountDownLatch(2)
        Thread{rpcserver(globalLatch)}.start()
        Thread{rpcclient(globalLatch)}.start()
        globalLatch.await()
    }
}