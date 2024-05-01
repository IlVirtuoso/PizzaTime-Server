using System.Data.Common;
using System.Security.Cryptography;

namespace PizzaTime.Data;



[Serializable]
public class Pizza
{
    public uint Id { get; set; } = 0;
    public string Name { get; set; } = "";
    public string Description { get; set; } = "";
    public double Price { get; set; }
    public static Pizza Generate(string name)
    {
        Pizza pizza = new Pizza();
        pizza.Id = (uint)new Random().Next();
        pizza.Name = "Secendant Worker";
        pizza.Description = "This is a test description";
        return pizza;
    }
}