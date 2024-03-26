using System.Data.Common;
using System.Reflection.Metadata.Ecma335;
using System.Text;
using System.Text.Json.Nodes;
using System.Threading.Tasks.Dataflow;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
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


    [HttpPost("/user/login")]
    public JsonResult UserLogin([FromForm] string username, [FromForm] string password)
    {
        string hashed = password.ToSHA512().ToHashedString();
        if(!_bridge.UserExist(username)){
            return new JsonResult(new {Error= "Username doesn't exists"});
        }
        var userSecret = _bridge.GetUserSecret(username);
        if(userSecret != hashed){
            return new JsonResult(new {Error="Invalid Password"});
        }

        return new JsonResult(new {Error="OK"});
    }

    [HttpPost("/pizzeria/login")]
    public JsonResult PizzeriaLogin([FromForm] string piva, [FromForm] string password){
        string hashed= password.ToSHA512().ToHashedString();
        if(!_bridge.PizzeriaExist(piva)){
            return new JsonResult(new {Error= "Pizzeria doesn't exists"});
        }
        var pizzeriaSecret = _bridge.GetPizzeriaSecret(piva);
        if(pizzeriaSecret != hashed){
            return new JsonResult(new {Error= "Invalid password"});
        }
        return new JsonResult(new {Error = "OK", Secret= hashed});
    }



}

[ApiController]
[Route("api/signin")]
public class SignInController : PizzaController
{

    public SignInController(ILogger<SignInController> logger, IDataBridge bridge) : base(logger, bridge)
    {

    }


    [HttpPost("/user/signin")]
    public JsonResult UserSignin([FromForm] string username, [FromForm] string password, [FromForm] string email ,[FromForm] string name, [FromForm] string surname)
    {
        if (_bridge.UserExist(username))
        {
            return new JsonResult(new {Error= "User already exists"});
        }
        var newUser = new User
        {
            UserName = username,
            Email = email,
            Name = name,
            SurName = surname
        };
        _bridge.AddUser(newUser);
        var hashedSecret = password.ToSHA512().ToHashedString();
        _bridge.SetUserSecret(username,hashedSecret?? throw new ArgumentException("Illegal argument on hashed secret"));
        return new JsonResult(new {Error= "OK", Secret= hashedSecret});
    }


    [HttpPost("/pizzeria/signin")]
    public JsonResult PizzeriaSignin([FromForm] string piva, [FromForm] string email, [FromForm] string name, [FromForm] string address, [FromForm] string password)
    {
        if(_bridge.PizzeriaExist(piva)){
            return new JsonResult(new {Error="Pizzeria already exist"});
        }
        var newPizzeria = new Pizzeria
        {
            Email = email,
            Address = address,
            Piva = piva,
            Name = name
        };
        _bridge.AddPizzeria(newPizzeria);
        var hashedSecret = password.ToSHA512().ToHashedString();
        _bridge.SetPizzeriaSecret(piva,hashedSecret ?? throw new ArgumentException("Illegal argument on hashed secret"));
        return new JsonResult(new {Error = "OK", Secret=hashedSecret});

    }
}
