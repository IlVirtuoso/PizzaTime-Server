using PizzaTime.Data;

namespace PizzaTimeApi.Database;
public interface IDataBridge
{
    User GetUserByName(string name);
    Pizza GetPizzaByName(string name);
    Pizzeria GetPizzeriaByName(string name);
    
}
