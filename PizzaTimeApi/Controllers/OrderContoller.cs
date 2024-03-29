using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers
{
    [Authorize]
    public class OrderContoller : PizzaController
    {
        public OrderContoller(ILogger logger, IDataBridge bridge) : base(logger, bridge)
        {
        }

        [HttpGet("/order/search")]
        public IEnumerable<Order> GetOrders([FromQuery] int orderId, [FromQuery] string? user){
            

            return null;
        }

        [HttpPost("/order/create")]
        public ActionResult CreateOrder([FromBody] int pizzeriaId, [FromBody] int pizzaId, [FromBody] uint quantity ){
            return null;
        }


    }
}