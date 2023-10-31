namespace PizzaTime.Data;



[Serializable]
public class Pizza
{
    public string Name { get; set; } = "";
    public string Description { get; set; } = "";
    public double Price { get; set; }
    public IList<Pizzeria> Holders { get; set; } = new List<Pizzeria>();
}












