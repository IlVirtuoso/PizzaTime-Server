using System.Reflection.Metadata.Ecma335;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers
{
    [Authorize]
    public class OrderContoller(ILogger logger, IDataBridge dataBridge) : PizzaController(logger, dataBridge)
    {

        [HttpGet("/order/{piva}/get")]
        [Authorize(Roles = "Pizzeria")]
        public IEnumerable<Order> GetOrders(string piva, [FromQuery] Order.OrderState state = Order.OrderState.PENDING){
            return _bridge.GetOrdersFromPizzeria(piva).Where(t=> t.State == state);
        }
        public record OrderCreate(int pizzeriaId, int pizzaId, uint quantity);

        [HttpPost("/order/create")]
        public IActionResult CreateOrder([FromBody] OrderCreate order){
            var submit = new Order{
                PizzaId = order.pizzeriaId,
                Quantity = order.quantity,
                State = Order.OrderState.PENDING,
                OrderId = 0
            };

            if(_bridge.AddOrder(submit)){
                return new JsonResult(new OkMessage());
            }
            return new JsonResult(new ErrorMessage("Something went wrong"));
        }

        [HttpPost("/order/cancel")]
        public IActionResult CancelOrder([FromBody] int orderId){
            return PerformBridgeOp(()=> _bridge.UpdateOrder(orderId,Order.OrderState.CANCELLED), true, "Something went wrong");
        }

        public record OrderUpdate(int orderId, Order.OrderState state);

        [HttpPost("/order/update")]
        public IActionResult UpdateOrderStatus([FromBody] OrderUpdate update){
            return PerformBridgeOp(() => _bridge.UpdateOrder(update.orderId,update.state), true, "something went wrong");
        }
        
    }
}