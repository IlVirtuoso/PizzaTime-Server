namespace PizzaTime.Data.Messages;

public enum AuthType
{
    PIZZERIA,
    USER
}


[Serializable]
public class AuthMessage : Message
{
    public AuthType AuthType { get; protected set; }
    public string Identifier { get; protected set; } = "";
    public string Secret { get; protected set; } = "";

    public static AuthMessage AuthErrorMessage(AuthMessage request,string error){
        AuthMessage message = request;
        message.Payload = error;
        message.Type = MessageType.Error;
        return message;
    }

    public static AuthMessage AuthPassedMessage(AuthMessage request, string hashedSecret){
        AuthMessage message = request;
        message.Payload = hashedSecret;
        message.Secret = hashedSecret;
        return message;
    }

}
