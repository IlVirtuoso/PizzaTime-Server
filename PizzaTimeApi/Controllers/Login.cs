using System.Data.Common;
using System.Reflection.Metadata.Ecma335;
using System.Security.Claims;
using System.Text;
using System.Text.Json.Nodes;
using System.Threading.Tasks.Dataflow;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTimeApi.Controllers;
using PizzaTimeApi.Database;
namespace PizzaTime.Api;

[ApiController]
public class Login : PizzaController
{

    public Login(ILogger<Login> logger, IDataBridge bridge) : base(logger, bridge)
    {

    }


    [HttpPost("/try")]
    public ActionResult Try([FromBody] User user){
        return new JsonResult(new {Error="OK"});
    }

    [HttpPost("/user/login")]
    public async Task<ActionResult> UserLogin([FromForm] string username, [FromForm] string password)
    {
        string hashed = password.ToSHA512().ToHashedString();
        if (!_bridge.UserExist(username))
        {
            return new JsonResult(new { Error = "Username doesn't exists" });
        }
        var userSecret = _bridge.GetUserSecret(username);
        if (userSecret != hashed)
        {
            return new JsonResult(new { Error = "Invalid Password" });
        }

        var user = _bridge.GetUserByName(username);

        List<Claim> claims = new List<Claim>(){
            new(ClaimTypes.NameIdentifier,username),
            new(ClaimTypes.Role,"User"),
            new(ClaimTypes.Email,user.Email),
            new(ClaimTypes.Name, user.Name),
            new(ClaimTypes.Surname, user.SurName)
        };

        var userIdentity = new ClaimsIdentity(claims);
        var userPrincipal = new ClaimsPrincipal(userIdentity);

        await HttpContext.SignInAsync(userPrincipal);
        return new JsonResult(new { Error = "OK" });
    }

    [HttpPost("/pizzeria/login")]
    public async Task<ActionResult> PizzeriaLogin([FromForm] string piva, [FromForm] string password)
    {
        string hashed = password.ToSHA512().ToHashedString();
        if (!_bridge.PizzeriaExist(piva))
        {
            return new JsonResult(new { Error = "Pizzeria doesn't exists" });
        }
        var pizzeriaSecret = _bridge.GetPizzeriaSecret(piva);
        if (pizzeriaSecret != hashed)
        {
            return new JsonResult(new { Error = "Invalid password" });
        }

        var pizzeria = _bridge.GetPizzeriaByPiva(piva);
        var claims = new List<Claim>(){
            new(ClaimTypes.Name,pizzeria.Name),
            new(ClaimTypes.Role,"Pizzeria"),
            new(ClaimTypes.StreetAddress,pizzeria.Address),
            new(ClaimTypes.NameIdentifier,pizzeria.Piva),
            new(ClaimTypes.Email,pizzeria.Email)
        };

        var identity = new ClaimsPrincipal(new ClaimsIdentity(claims));
        await HttpContext.SignInAsync(identity);
        return new JsonResult(new { Error = "OK" });
    }


    [HttpPost("/logout")]
    [Authorize]
    public ActionResult Logout()
    {
        HttpContext.SignOutAsync();
        return new JsonResult(new { Error = "OK" });
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
    public JsonResult UserSignin([FromForm] string username, [FromForm] string password, [FromForm] string email, [FromForm] string name, [FromForm] string surname)
    {
        if (_bridge.UserExist(username))
        {
            return new JsonResult(new { Error = "User already exists" });
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
        _bridge.SetUserSecret(username, hashedSecret ?? throw new ArgumentException("Illegal argument on hashed secret"));
        return new JsonResult(new { Error = "OK", Secret = hashedSecret });
    }


    [HttpPost("/pizzeria/signin")]
    public JsonResult PizzeriaSignin([FromForm] string piva, [FromForm] string email, [FromForm] string name, [FromForm] string address, [FromForm] string password)
    {
        if (_bridge.PizzeriaExist(piva))
        {
            return new JsonResult(new { Error = "Pizzeria already exist" });
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
        _bridge.SetPizzeriaSecret(piva, hashedSecret ?? throw new ArgumentException("Illegal argument on hashed secret"));
        return new JsonResult(new { Error = "OK", Secret = hashedSecret });

    }
}
