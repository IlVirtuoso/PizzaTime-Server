import com.rabbitmq.client.*
import com.sun.security.ntlm.Server
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch


class AmqpExtensionTest {



    var service = MockCommunicationService();
    val exchange_name = "mock";
    fun server(channel: Channel, globalLatch: CountDownLatch){
        var name = channel.queueDeclare().queue
        var mex = "";
        var tag = "";
        var consumer =  DefaultConsumer(channel);
        val latch = CountDownLatch(1);
        channel.exchangeDeclare(exchange_name, "direct");
        channel.queueBind(name,exchange_name,"log");
        channel.basicPublish(exchange_name,"log",null,"Hello".encodeToByteArray());
        channel.basicConsume(name,false, consumer )

        latch.await();
        println(mex);
        globalLatch.countDown()
    }

    fun client(channel: Channel, globalLatch: CountDownLatch){
        var name = channel.queueDeclare().queue
        var mex = "";
        val exchange_name = "mock";
        val latch = CountDownLatch(1);
        channel.exchangeDeclare(exchange_name, "direct");
        channel.queueBind(name,exchange_name , "log");
        channel.basicConsume(
            name, DeliverCallback { consumerTag, message ->
                mex = message.body.decodeToString();
                latch.countDown()
            },
            CancelCallback { }
        )
        latch.await();
        println(mex);
        globalLatch.countDown()
    }

    @Test
    fun test(){
        val globalLatch = CountDownLatch(2);
        val channel = service.channel;
        Thread { -> server(channel,globalLatch) }.start();
        Thread{ -> client(channel,globalLatch)}.start();
        globalLatch.await()
    }
}