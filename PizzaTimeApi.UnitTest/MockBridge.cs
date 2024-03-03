
namespace PizzaTime.UnitTest.Mock;
public class MockBridge : IDataBridge
{
    public Pizza GetPizzaByName(string name)
    {

    }

   

    public Pizzeria GetPizzeriaByName(string name)
    {
        throw new NotImplementedException();
    }

    public User GetUserByName(string name)
    {
        throw new NotImplementedException();
    }
}


