using System.Text.Json.Nodes;
using System.Threading.Tasks.Dataflow;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Bridge;
using PizzaTime.Data;
namespace PizzaTime.Api;

[ApiController]
[Route("api/login")]
public class Login
{
    private DbBridge _bridge;
    private ILogger _logger;
    public Login(ILogger<Login> logger, DbBridge bridge)
    {
        _logger = logger;
        _bridge = bridge;
    }


    public class UserAuth
    {
        public string UserName { get; set; }
        public string Password { get; set; }
    }

    [HttpPost]
    public JsonResult Post([FromBody] UserAuth auth)
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

    [HttpPost]
    public JsonResult Post([FromBody] UserSignIn signin){
        return new JsonResult(new {Message="Hello",Result="World"});
    }
}
