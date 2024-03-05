
namespace PizzaTime.UnitTest.Mock;
public class MockBridge : IDataBridge
{
    public Pizza GetPizzaByName(string name)
    {
        return Pizza.Generate(name);
    }

   

    public Pizzeria GetPizzeriaByName(string name)
    {
        return Pizzeria.Generate(name);

    }

    public User GetUserByName(string name)
    {
        return User.Generate(name);
    }
}


