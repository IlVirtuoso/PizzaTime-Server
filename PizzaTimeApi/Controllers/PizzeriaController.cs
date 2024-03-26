using Microsoft.AspNetCore.Mvc;
using PizzaTime.Data;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers
{
    public class PizzeriaController : PizzaController
    {
        public PizzeriaController(ILogger logger, IDataBridge bridge) : base(logger, bridge)
        {
        }


        [HttpGet("pizzeria/{piva}")]
        public Pizzeria? GetPizzeria(string piva){
            if(!_bridge.PizzeriaExist(piva)){
                return null;
            }
            return _bridge.GetPizzeriaByPiva(piva);
        }


        [HttpGet("pizzeria/search")]
        public List<Pizzeria> GetPizzeriaByName([FromQuery] string name, [FromQuery] string address){
            return _bridge.GetPizzeriaByName(name).Where(t => t.Address == (address == ""? t.Address:address) ).ToList();
        }


        [HttpGet("pizzeria/{piva}/orders")]
        public IEnumerable<Order> GetOrdersForPizzeria(string piva, [FromQuery] Order.OrderState state = Order.OrderState.PENDING){
            return _bridge.GetOrdersFromPizzeria(piva).Where(t=> t.State == state);
        }


        

    }
}