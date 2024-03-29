using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers
{
    public class UserController : PizzaController
    {
        public UserController(ILogger logger, IDataBridge bridge) : base(logger, bridge)
        {
        }


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

        [HttpGet("/user/{userName}/orders")]
        [Authorize]
        public IEnumerable<Order> GetUserOrders(string userName, [FromQuery] Order.OrderState? status = Order.OrderState.PENDING)
        {
            return _bridge.GetOrdersFromUser(userName).Where(t => t.State == status);
        }

        [HttpPost("/user/{userName}/orders/submit")]
        [Authorize]
        public JsonResult SubmitOrder(string username, [FromBody] int pizzaId, [FromBody] int quantity, [FromBody] int piva)
        {
            
            return new JsonResult(new {User = username, PizzaId = pizzaId, Quantity = quantity, PIVA= piva});
        }



    }
}