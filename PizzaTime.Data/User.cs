namespace PizzaTime.Data;

[Serializable]
public class User
{
    public string UserName { get; set; } = "";
    public string Name { get; set; } = "";
    public string SurName { get; set; } = "";
    public string Email { get; set; } = "";
}

public class UserAuth
{
    public string UserName { get; set; } = "";
    public string HashedPassword { get; set; } = "";
}






