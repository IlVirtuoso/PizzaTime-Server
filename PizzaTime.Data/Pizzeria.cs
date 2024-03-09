namespace PizzaTime.Data;

public class Pizzeria
{
    public string Email { get; set; } = "";
    public string Piva { get; set; } = "";
    public string Address { get; set; } = "";
    public string Name {get;set;} = "";
    public static Pizzeria Generate(string iva)
    {
        Pizzeria pizzeria = new Pizzeria();
        pizzeria.Email = "testmail@test";
        pizzeria.Name = "testPizzeria";
        pizzeria.Piva = iva;
        pizzeria.Address = "testAddress, p.address";
        return pizzeria;
    }
}


