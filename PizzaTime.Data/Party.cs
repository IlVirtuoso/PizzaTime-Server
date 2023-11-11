namespace PizzaTime.Data;


public class Party
{
    public string PartyName { get; set; } = "";
    public User Holder { get; set; }
    public IList<User> Participants { get; set; } = new List<User>();
}



