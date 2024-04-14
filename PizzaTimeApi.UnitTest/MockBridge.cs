

namespace PizzaTime.UnitTest.Mock;
public class MockBridge : IDataBridge
{
    public List<User> Users { get; set; } = new List<User>();
    public List<Pizzeria> Pizzerias { get; set; } = new List<Pizzeria>();
    public Dictionary<User, string> UserSecrets { get; set; } = new Dictionary<User, string>();
    public Dictionary<Pizzeria, string> PizzeriaSecrets { get; set; } = new Dictionary<Pizzeria, string>();

    public bool AddPizzeria(Pizzeria pizzeria)
    {
        if(!Pizzerias.Contains(pizzeria)){
            Pizzerias.Add(pizzeria);
            return true;
        }
        return false;
    }

    public bool AddUser(User user)
    {
        if(!Users.Contains(user)){
            Users.Add(user);
            return true;
        }
        return false;
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

    public Pizzeria? GetPizzeriaByPiva(string iva)
    {
        foreach(var p in Pizzerias){
            if(p.Piva == iva) {return p;}
        }
        return null;
    }

    public string? GetPizzeriaSecret(string piva)
    {
        return PizzeriaSecrets[GetPizzeriaByPiva(piva) ?? throw new ArgumentException("Entity not existent")];
    }

    public User? GetUserByName(string name)
    {
        foreach(var u in Users){
            if(u.UserName == name) {return u;}
        }
        return null;
    }

    public string? GetUserSecret(string userName)
    {
        if(!UserExist(userName)) return null;
       return UserSecrets[GetUserByName(userName) ?? throw new ArgumentException("Entity not existent")];
    }

    public bool PizzeriaExist(string piva)
    {
        return Pizzerias.Select(t=> t.Piva).Contains(piva);
    }

    public bool SetPizzeriaSecret(string piva, string secret)
    {
        if(!PizzeriaExist(piva)) return false;
        PizzeriaSecrets[GetPizzeriaByPiva(piva) ?? throw new ArgumentException("Entity not existent")] = secret;
        return true;
    }

    public bool SetUserSecret(string username, string secret)
    {
        if(!UserExist(username)) return false;
        UserSecrets[GetUserByName(username) ?? throw new ArgumentException("Entity not existent")] = secret;
        return true;
    }

    public bool UserExist(string userName)
    {
        return Users.Select(p=> p.Name).Contains(userName);
    }
}


