namespace PizzaTime.Data;
public class Order
{
    public User User { get; set; }
    public Pizzeria Pizzeria { get; set; }
    public Pizza Pizza { get; set; }
    public uint Quantity { get; set; }
}

public class PartyOrder
{
    public Party PartyName { get; set; }
    public IList<Order> Orders{get;set;}
}