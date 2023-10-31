namespace PizzaTime.Data;

public class Pizzeria
{
    public string Email { get; set; } = "";
    public string Piva { get; set; } = "";
    public IList<Pizza> Menu { get; set; } = new List<Pizza>();
    public string Address { get; set; } = "";
}


public class PizzeriaAuth
{
    public string Email { get; set; } = "";
    public string HashedPassword { get; set; } = "";
}

