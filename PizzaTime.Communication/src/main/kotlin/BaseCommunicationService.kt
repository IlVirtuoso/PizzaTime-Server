import com.rabbitmq.client.AMQP.Queue
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Channel



open class BaseCommunicationService( amqpUser : String, amqpPassword: String,  amqpHost : String){
    companion object{
        fun create_connection(user: String, password: String, host: String):Connection {
            val factory = ConnectionFactory()
            factory.host = host
            factory.port = 5672
            factory.username = user;
            factory.password = password;
            return factory.newConnection("server");
        }
    }


    open val connection: Connection = create_connection(amqpUser, amqpPassword,amqpHost)

    val channel: Channel ; get() {
        return connection.createChannel();
    }



}