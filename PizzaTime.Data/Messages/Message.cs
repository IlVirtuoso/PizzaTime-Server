namespace PizzaTime.Data.Messages;
public class Message
{
    public enum MessageType
    {
        Error,
        Ack,
        Response,
        Request
    }

    public string Payload { get; set; } = "";
    public MessageType Type { get; protected set; }
}
