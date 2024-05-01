using System.Security.Cryptography.X509Certificates;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using PizzaTime.Data;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers
{
    public class UserController(ILogger logger, IDataBridge bridge) : PizzaController(logger, bridge)
    {
        [HttpGet("/user/{userName}")]
        [Authorize]
        public User? GetUser(string userName)
        {
            if (!_bridge.UserExist(userName))
            {
                return null;
            }
            return _bridge.GetUserByName(userName);
        }

        
       

    }
}