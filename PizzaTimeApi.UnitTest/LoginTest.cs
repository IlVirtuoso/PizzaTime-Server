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
        AuthMessage request = new AuthMessage
        {
            Auth = AuthMessage.AuthType.USER,
            Identifier = "TestUser",
            Secret = "none"
        };
        var result = controller.Post(request);
        Assert.Multiple(() =>
        {
            Assert.That(result.Type, Is.EqualTo(Message.MessageType.Response));
            Assert.That(result.Error, Is.EqualTo(AuthMessage.ErrorReason.OK));
            Assert.That(result.Secret, Is.EqualTo("none".ToSHA512().ToHashedString()));
        });
    }

    [Test]
    public void TestPizzeriaLogin()
    {
        MockBridge.Setup(t => t.GetPizzeriaSecret("TestPizzeria")).Returns(()=> "none".ToSHA512().ToHashedString());
        var controller = BuildLoginController();
        AuthMessage request = new AuthMessage
        {
            Auth = AuthMessage.AuthType.PIZZERIA,
            Identifier = "TestPizzeria",
            Secret = "none"
        };
        var result = controller.Post(request);
        Assert.Multiple(() =>
        {
            Assert.That(Message.MessageType.Response == result.Type);
            Assert.That(result.Error, Is.EqualTo(AuthMessage.ErrorReason.OK));
            Assert.That(result.Secret, Is.EqualTo("none".ToSHA512().ToHashedString()));
        });
    }
}