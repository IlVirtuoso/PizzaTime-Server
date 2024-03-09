using System.Security.Cryptography.X509Certificates;
using Castle.Core.Logging;
using Microsoft.Extensions.Logging.Abstractions;
using Moq;
using PizzaTime.Api;
using PizzaTime.Data.Messages;

namespace PizzaTimeApi.UnitTest;

[TestFixture]
public class LoginTest : TestEnvironment
{


    [SetUp]
    public override void Setup()
    {
        base.Setup();
    }

    [TearDown]
    public override void TearDown()
    {
        base.TearDown();
    }

    [Test]
    public void TestUserLogin()
    {
        MockBridge.Setup(t=> t.GetUserSecret("TestUser")).Returns(()=> "none".ToSHA512().ToHashedString());
        var controller = BuildLoginController();
        AuthMessage request = new AuthMessage();
        request.Auth = AuthMessage.AuthType.USER;
        request.Identifier = "TestUser";
        request.Secret = "none";
        var result = controller.Post(request);
        Assert.That(Message.MessageType.Response == result.Type);
        Assert.That(result.Error == AuthMessage.ErrorReason.OK);
        Assert.That(result.Secret == "none".ToSHA512().ToHashedString());
    }

    
    [Test]
    public void TestPizzeriaLogin(){
        MockBridge.Setup(t => t.GetPizzeriaSecret("TestPizzeria")).Returns(()=> "none".ToSHA512().ToHashedString());
        var controller = BuildLoginController();
        AuthMessage request = new AuthMessage();
        request.Auth = AuthMessage.AuthType.PIZZERIA;
        request.Identifier = "TestPizzeria";
        request.Secret = "none";
        var result = controller.Post(request);
        Assert.That(Message.MessageType.Response == result.Type);
        Assert.That(result.Error == AuthMessage.ErrorReason.OK);
        Assert.That(result.Secret == "none".ToSHA512().ToHashedString());
    }
}