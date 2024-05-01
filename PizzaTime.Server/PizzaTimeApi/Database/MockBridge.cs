using PizzaTime.Data;

namespace PizzaTimeApi.Database
{
    public class MockBridge : IDataBridge
    {
        public bool AddPizzeria(Pizzeria pizzeria)
        {
            throw new NotImplementedException();
        }

        public bool AddUser(User user)
        {
            throw new NotImplementedException();
        }

        public List<Order> GetOrdersFromPizzeria(string piva)
        {
            throw new NotImplementedException();
        }

        public List<Order> GetOrdersFromUser(string userName)
        {
            throw new NotImplementedException();
        }

        public Pizza? GetPizzaByName(string name)
        {
            throw new NotImplementedException();
        }

        public List<Pizzeria> GetPizzeriaByName(string name)
        {
            throw new NotImplementedException();
        }

        public Pizzeria? GetPizzeriaByPiva(string name)
        {
            throw new NotImplementedException();
        }

        public string? GetPizzeriaSecret(string piva)
        {
            throw new NotImplementedException();
        }

        public User? GetUserByName(string name)
        {
            throw new NotImplementedException();
        }

        public string? GetUserSecret(string userName)
        {
            throw new NotImplementedException();
        }

        public bool PizzeriaExist(string piva)
        {
            throw new NotImplementedException();
        }

        public bool SetPizzeriaSecret(string piva, string secret)
        {
            throw new NotImplementedException();
        }

        public bool SetUserSecret(string username, string secret)
        {
            throw new NotImplementedException();
        }

        public bool UserExist(string userName)
        {
            throw new NotImplementedException();
        }
    }
}