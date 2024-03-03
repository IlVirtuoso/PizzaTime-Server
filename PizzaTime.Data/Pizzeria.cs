namespace PizzaTime.Data;

public class Pizzeria
{
    public string Email { get; set; } = "";
    public string Piva { get; set; } = "";
    public string Address { get; set; } = "";

    public static Pizzeria Generate()
    {
        Pizzeria pizzeria = new Pizzeria();
        pizzeria.Email = "testmail@test";
        pizzeria.Piva = new Random().Next(1000000,99999999).ToString();
        pizzeria.Address = "";
        return pizzeria;
    }
}


