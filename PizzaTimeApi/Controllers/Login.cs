using System.Data.Common;
using System.Reflection.Metadata.Ecma335;
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


    [HttpPost("/user")]
    public JsonResult UserLogin([FromBody] AuthMessage auth)
    {
        AuthMessage response = new AuthMessage();
        if (auth.AuthType != AuthType.USER)
        {
            response = AuthMessage.AuthErrorMessage(auth,"Wrong authentication api, please use the one for pizzeria");
        }
        var secret = _bridge.GetUserSecret(auth.Identifier);
        if (secret == null)
        {
            response = AuthMessage.AuthErrorMessage(auth,"No user found");
        }
        return new JsonResult(response);
    }

    [HttpPost("/pizzeria")]
    public JsonResult PizzeriaLogin([FromBody] AuthMessage auth)
    {
        return new JsonResult(new { Name = "hello", Result = "workd" });
    }
}

[ApiController]
[Route("api/signin")]
public class SignInController : PizzaController
{
    public class UserSignIn
    {
        public string UserName { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
    }
    public SignInController(ILogger<SignInController> logger, IDataBridge bridge) : base(logger, bridge)
    {

    }

    [HttpPost]
    public JsonResult Post([FromBody] UserSignIn signin)
    {
        var user = _bridge.GetUserByName(signin.UserName);

        return new JsonResult(new { Message = "Hello", Result = "World" });
    }


    public class PizzeriaSignIn
    {
        public string PIVA { get; set; }
        public string Email { get; set; }
        public string Address { get; set; }
    }

    [HttpPost("/pizzeria")]
    public JsonResult SignInPizzeria([FromBody] PizzeriaSignIn signIn)
    {
        return new JsonResult(new { Message = "Hello", Result = "World" });

    }

}
