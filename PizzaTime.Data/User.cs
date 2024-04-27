namespace PizzaTime.Data;

[Serializable]
public class User
{
    public string UserName { get; set; } = "";
    public string Name { get; set; } = "";
    public string SurName { get; set; } = "";
    public string Email { get; set; } = "";
    public virtual string Role {get;set;} = "User";
    public static User Generate(string userName)
    {
        User user = new User();
        user.UserName = userName;
        user.Name = "Test";
        user.SurName = "Test";
        user.Email = "test@test.com";
        return user;
    }
}


public class Rider : User{
    public override string Role => "Rider";
}



