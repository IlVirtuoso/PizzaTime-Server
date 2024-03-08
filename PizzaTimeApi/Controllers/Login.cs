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
        else if (secret == auth.Secret.ToSHA512().ToString())
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
            case AuthMessage.AuthType.PIZZERIA: return PizzeriaSignin(message);
            case AuthMessage.AuthType.USER: return UserSignin(message);
        }
        return AuthMessage.AuthErrorMessage(message, AuthMessage.ErrorReason.INVALID_ENDPOINT);
    }

    public AuthMessage UserSignin(AuthMessage authMessage)
    {
        var user = authMessage.Identifier;
        if (_bridge.UserExist(user))
        {
            return AuthMessage.AuthErrorMessage(authMessage, AuthMessage.ErrorReason.NO_USER);
        }
        var newUser = new User();
        newUser.UserName = user;
        newUser.Email = authMessage.Attributes[0];
        newUser.Name = authMessage.Attributes[1];
        newUser.SurName = authMessage.Attributes[2];
        _bridge.AddUser(newUser);
        var hashedSecret = authMessage.Secret.ToSHA512().ToString();
        _bridge.SetUserSecret(user,hashedSecret?? throw new ArgumentException("Illegal argument on hashed secret"));
        return AuthMessage.AuthPassedMessage(authMessage,hashedSecret);
    }


    public AuthMessage PizzeriaSignin(AuthMessage message)
    {
        var pizzeria = message.Identifier;
        if(_bridge.PizzeriaExist(pizzeria)){
            return AuthMessage.AuthErrorMessage(message,AuthMessage.ErrorReason.NO_USER);
        }
        var newPizzeria = new Pizzeria();
        newPizzeria.Email = message.Attributes[0];
        newPizzeria.Address = message.Attributes[1];
        newPizzeria.Piva = pizzeria;
        _bridge.AddPizzeria(newPizzeria);
        var hashedSecret = message.Secret.ToSHA512().ToString();
        _bridge.SetPizzeriaSecret(pizzeria,hashedSecret ?? throw new ArgumentException("Illegal argument on hashed secret"));
        return AuthMessage.AuthPassedMessage(message,hashedSecret);
    }



}
