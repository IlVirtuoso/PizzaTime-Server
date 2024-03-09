using System.Data.Common;
using System.Reflection.Metadata.Ecma335;
using System.Text;
using System.Text.Json.Nodes;
using System.Threading.Tasks.Dataflow;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTime.Data.Messages;
using PizzaTimeApi.Controllers;
using PizzaTimeApi.Database;
namespace PizzaTime.Api;

[ApiController]
[Route("api/login")]
public class Login : PizzaController
{

    public Login(ILogger<Login> logger, IDataBridge bridge) : base(logger, bridge)
    {

    }


    [HttpPost]
    public AuthMessage Post([FromBody] AuthMessage auth)
    {
        string? secret = null;
        switch (auth.Auth)
        {
            case AuthMessage.AuthType.PIZZERIA: secret = _bridge.GetPizzeriaSecret(auth.Identifier); break;
            case AuthMessage.AuthType.USER: secret = _bridge.GetUserSecret(auth.Identifier); break;
        }
        if (secret == null)
        {
            return AuthMessage.AuthErrorMessage(auth, AuthMessage.ErrorReason.NO_USER);
        }
        else if (secret == auth.Secret.ToSHA512().ToHashedString())
        {
            return AuthMessage.AuthPassedMessage(auth, secret);
        }
        return AuthMessage.AuthErrorMessage(auth, AuthMessage.ErrorReason.INVALID_PASSWORD);
    }



}

[ApiController]
[Route("api/signin")]
public class SignInController : PizzaController
{

    public SignInController(ILogger<SignInController> logger, IDataBridge bridge) : base(logger, bridge)
    {

    }


    [HttpPost]
    public AuthMessage Post([FromBody] AuthMessage message)
    {
        switch (message.Auth)
        {
            case AuthMessage.AuthType.PIZZERIA: return PizzeriaSignin(message as PizzeriaSignInMessage ?? throw new ArgumentException("Invalid message format"));
            case AuthMessage.AuthType.USER: return UserSignin(message as UserSignInMessage ?? throw new ArgumentException("Trying to login with invalid message format"));
        }
        return AuthMessage.AuthErrorMessage(message, AuthMessage.ErrorReason.INVALID_ENDPOINT);
    }

    public AuthMessage UserSignin(UserSignInMessage authMessage)
    {
        var user = authMessage.Identifier;
        if (_bridge.UserExist(user))
        {
            return AuthMessage.AuthErrorMessage(authMessage, AuthMessage.ErrorReason.NO_USER);
        }
        var newUser = new User
        {
            UserName = user,
            Email = authMessage.Email,
            Name = authMessage.Name,
            SurName = authMessage.SurName
        };
        _bridge.AddUser(newUser);
        var hashedSecret = authMessage.Secret.ToSHA512().ToHashedString();
        _bridge.SetUserSecret(user,hashedSecret?? throw new ArgumentException("Illegal argument on hashed secret"));
        return AuthMessage.AuthPassedMessage(authMessage,hashedSecret);
    }


    public AuthMessage PizzeriaSignin(PizzeriaSignInMessage message)
    {
        var pizzeria = message.Identifier;
        if(_bridge.PizzeriaExist(pizzeria)){
            return AuthMessage.AuthErrorMessage(message,AuthMessage.ErrorReason.NO_USER);
        }
        var newPizzeria = new Pizzeria
        {
            Email = message.Email,
            Address = message.Address,
            Piva = pizzeria,
            Name = message.Name
        };
        _bridge.AddPizzeria(newPizzeria);
        var hashedSecret = message.Secret.ToSHA512().ToHashedString();
        _bridge.SetPizzeriaSecret(pizzeria,hashedSecret ?? throw new ArgumentException("Illegal argument on hashed secret"));
        return AuthMessage.AuthPassedMessage(message,hashedSecret);
    }
}
