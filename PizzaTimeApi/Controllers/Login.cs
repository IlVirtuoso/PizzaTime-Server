using System.Text.Json.Nodes;
using System.Threading.Tasks.Dataflow;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
namespace PizzaTime.Api;

[ApiController]
[Route("api/login")]
public class Login
{
    private ILogger _logger;
    public Login(ILogger<Login> logger)
    {
        _logger = logger;
        
    }


    public class UserAuth
    {
        public string UserName { get; set; }
        public string Password { get; set; }
    }

    public class PizzeriaAuth
    {
        public string PIVA { get; set; } = "";
        public string Password { get; set; } = "";
    }



    [HttpPost]
    public JsonResult Post([FromBody] UserAuth auth)
    {
        return new JsonResult(new { Name = "hello", Result = "workd" });
    }

    [HttpPost("/pizzeria")]
    public JsonResult LoginPizzeria([FromBody] PizzeriaAuth auth)
    {
        return new JsonResult(new { Name = "hello", Result = "workd" });
    }
}

[ApiController]
[Route("api/signin")]
public class SignInController : ControllerBase
{
    public class UserSignIn
    {
        public string UserName { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
    }
    public SignInController(ILogger<SignInController> logger)
    {
     
    }

    [HttpPost]
    public JsonResult Post([FromBody] UserSignIn signin)
    {
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
