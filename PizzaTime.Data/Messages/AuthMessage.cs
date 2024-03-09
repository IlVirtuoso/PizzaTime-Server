namespace PizzaTime.Data.Messages;

[Serializable]
public class AuthMessage : Message
{
    public enum AuthType
    {
        PIZZERIA,
        USER
    }

    public enum ErrorReason
    {
        OK,
        INVALID_PASSWORD,
        NO_USER,
        INVALID_ENDPOINT
    }
    public AuthType Auth { get; set; }
    public string Identifier { get; set; } = "";
    public string Secret { get; set; } = "";
    public ErrorReason Error { get; set; } = ErrorReason.OK;
    public static AuthMessage AuthErrorMessage(AuthMessage request, ErrorReason reason)
    {
        AuthMessage message = request;
        message.Error = reason;
        message.Type = MessageType.Error;
        return message;
    }

    public static AuthMessage AuthPassedMessage(AuthMessage request, string hashedSecret)
    {
        AuthMessage message = request;
        message.Secret = hashedSecret;
        message.Type = MessageType.Response;
        return message;
    }

}
