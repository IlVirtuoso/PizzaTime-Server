using PizzaTime.Data;

namespace PizzaTimeApi.Database;
public interface IDataBridge
{
    User? GetUserByName(string name);
    Pizza? GetPizzaByName(string name);
    Pizzeria? GetPizzeriaByPiva(string name);
    List<Pizzeria> GetPizzeriaByName(string name);

    string? GetUserSecret(string userName);
    string? GetPizzeriaSecret(string piva);

    bool SetUserSecret(string username, string secret);
    bool SetPizzeriaSecret(string piva, string secret);

    bool UserExist(string userName);

    bool PizzeriaExist(string piva);

    bool AddUser(User user);
    bool AddPizzeria(Pizzeria pizzeria);

    List<Order> GetOrdersFromUser(string userName);
    List<Order> GetOrdersFromPizzeria(string piva); 
    bool UpdateOrder(int orderId, Order.OrderState state);
    bool AddOrder(Order order);
    

}
