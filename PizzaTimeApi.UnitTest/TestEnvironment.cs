using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Logging.Abstractions;
using Moq;
using PizzaTime.Api;
using PizzaTime.UnitTest.Mock;

namespace PizzaTimeApi.UnitTest;
public class TestEnvironment
{
    protected Login BuildLoginController()
    {
        var logger = new NullLogger<Login>();
        var login = new Login(logger, MockBridge.Object);
        return login;
    }

    protected SignInController BuildSignInController(){
        var logger = new NullLogger<SignInController>();
        var ctrl = new SignInController(logger,MockBridge.Object);
        return ctrl;
    }

    public Mock<IDataBridge> MockBridge {get;private set;}


    [SetUp]
    public virtual void Setup()
    {
        MockBridge = new Mock<IDataBridge>();
    }

    [TearDown]
    public virtual void TearDown()
    {

    }
}
