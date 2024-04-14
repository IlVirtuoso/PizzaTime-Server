using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTimeApi.Controllers;
using PizzaTimeApi.Database;

namespace PizzaTime.Api;

[Authorize]
[ApiController]
public class OrderController(ILogger logger, IDataBridge bridge) : PizzaController(logger, bridge)
{
    public record OrderRequest(int pizzaId, int quantity, string piva);
    public record OrderEditRequest(int orderId, Order.OrderState newState);


    [HttpPost("/order/user/{userName}/submit")]
    public ActionResult SubmitOrder(string username,[FromBody] OrderRequest request)
    {
        return new JsonResult(new OkMessage());
    }

    [HttpPost("/order/user/{userName}/cancel")]
    public ActionResult CancelOrder(string username, [FromBody] int orderId){
        return new JsonResult(new OkMessage());
    }
    

    [HttpGet("/order/user/{userName}/orders")]
    public IEnumerable<Order> GetUserOrders(string userName, [FromQuery] Order.OrderState? status = Order.OrderState.PENDING)
    {
        return _bridge.GetOrdersFromUser(userName).Where(t => t.State == status);
    }


    [HttpGet("/order/pizzeria/{piva}/orders")]
    public IEnumerable<Order> GetPizzeriaOrders(string piva, [FromQuery] Order.OrderState? status = Order.OrderState.PENDING){
        return _bridge.GetOrdersFromPizzeria(piva).Where(t => t.State == status);
    }

    [HttpPost("/order/pizzeria/{piva}/edit")]
    public ActionResult SetOrderResult(string piva, [FromBody] OrderEditRequest request ){
        return new JsonResult(new OkMessage());
    }

    




}

