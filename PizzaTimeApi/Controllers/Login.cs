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
            case AuthMessage.AuthType.PIZZERIA: secret = _bridge.GetPizzeriaSecret(auth.Identifier);break;
            case AuthMessage.AuthType.USER : secret = _bridge.GetUserSecret(auth.Identifier);break;
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

  

}
