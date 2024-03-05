using PizzaTime.Data;

namespace PizzaTimeApi.Database;
public interface IDataBridge
{
    User? GetUserByName(string name);
    Pizza? GetPizzaByName(string name);
    Pizzeria? GetPizzeriaByName(string name);

    string? GetUserSecret(string userName);
    string? GetPizzeriaSecret(string piva);

    void SetUserSecret(string username, string secret);
    void SetPizzeriaSecret(string piva, string secret);
}
