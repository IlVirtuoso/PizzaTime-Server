using PizzaTimeApi.Controllers;
using PizzaTimeApi.UnitTest;

namespace PizzaTime.Api.UnitTest;

public class UserActionTest: TestEnvironment
{
    
    [Test]
    public void TestUserOrderSubmit(){

        var controller = BuildController<UserController>();
        var resp = controller.SubmitOrder("a",0,0,"1234567");
    }
}
