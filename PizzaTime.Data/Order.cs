namespace PizzaTime.Data;
public class Order
{
    public enum OrderState
    {
        PENDING,
        SERVING,
        SERVED
    }
    public int OrderId {get;set;}
    public string UserName { get; set; }
    public string PizzeriaId { get; set; }
    public int PizzaId { get; set; }
    public uint Quantity { get; set; }
    public OrderState State { get; set; }
}

public class PartyOrder
{
    public Party PartyName { get; set; }
    public IList<Order> Orders { get; set; }
}