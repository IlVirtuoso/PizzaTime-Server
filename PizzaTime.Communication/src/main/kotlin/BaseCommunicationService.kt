import com.rabbitmq.client.AMQP.Queue
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Channel



open class BaseCommunicationService(var amqpUser : String, var amqpPassword: String, var amqpHost : String){
    companion object{
        fun create_connection(user: String, password: String, host: String):Connection {
            val factory = ConnectionFactory()
            factory.setUri("amqp://$user:$password@$host")
            return factory.newConnection();
        }
    }



    open val connection: Connection = create_connection(amqpUser, amqpPassword,amqpHost)

    open val channel: Channel ; get() {
        return connection.createChannel();
    }



}