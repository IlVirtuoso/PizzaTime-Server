namespace PizzaTime.Data.Messages;
public class UserSignInMessage : AuthMessage
{
    public UserSignInMessage() : base()
    {
        Auth = AuthType.USER;
    }

    public string Email { get; set; } = "";
    public string Name { get; set; } = "";
    public string SurName { get; set; } = "";

}

public class PizzeriaSignInMessage : AuthMessage
{
    public PizzeriaSignInMessage() : base(){
        Auth = AuthType.PIZZERIA;
    }

    public string Email {get;set;} = "";
    public string Address {get;set;} = "";
    public string Name{get;set;} = "";

}