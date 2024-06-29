import com.google.gson.Gson
import com.rabbitmq.client.*
import com.rabbitmq.tools.jsonrpc.JsonRpcServer
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.startCoroutine
import kotlin.test.Test


class TestJsonRpc {
    val exchange = "tryExchange";

    class Sample(var sampleId: Long, var tubeId: Long, var description: String, var acked : Boolean)

    class Responder(channel: Channel): RpcServer(channel){

        override fun handleCall(
            requestProperties: AMQP.BasicProperties?,
            requestBody: ByteArray?,
            replyProperties:  AMQP.BasicProperties?
        ): ByteArray {

            when(requestProperties?.type){
                "Ack"->{
                    val sample = Gson().fromJson(requestBody?.decodeToString(), Sample::class.java)
                    sample.acked = true
                    return Gson().toJson(sample).encodeToByteArray();
                }
                "ExitRequest"->{
                    terminateMainloop();
                    return "OK".encodeToByteArray();
                }
            }

            return ByteArray(1);
        }
    }


    fun setupServer(channel: Channel, latch: CountDownLatch) {
        val server = Responder(channel);
        channel.exchangeDeclare(exchange,BuiltinExchangeType.DIRECT);
        channel.queueBind(server.queueName,exchange,"world");
        server.mainloop();
        latch.countDown()
    }

    fun setupClient(channel: Channel, countDownLatch: CountDownLatch) {
        val client = RpcClient(RpcClientParams().channel(channel).exchange("PizzaTime.IDP").routingKey("IDPServiceRequest"))
        val response= client.primitiveCall(AMQP.BasicProperties().builder().type("VerifyUserTokenRequest").build(),
            Gson().toJson(object{var token = "dasdhaisdh"}).encodeToByteArray()
            );
        println(response.decodeToString());
        client.primitiveCall(AMQP.BasicProperties().builder().type("ExitRequest").build(),ByteArray(0));
        countDownLatch.countDown()
    }


    @Test
    fun testJsonRpc() {
        var channel = ConnectionFactory().let { t ->
            t.host = "localhost"; t.username = "guest"; t.password = "guest"; return@let t
        }.newConnection().createChannel();

        val globalLatch = CountDownLatch(1);

        Thread { setupClient(channel, globalLatch) }.start()
        globalLatch.await();

    }
}