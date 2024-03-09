using Microsoft.AspNetCore.Http.HttpResults;
using Moq;
using PizzaTime.Data.Messages;

namespace PizzaTimeApi.UnitTest;

[TestFixture]
public class SignInTest : TestEnvironment
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
    public void TestUserSignIn()
    {
        MockBridge.Setup(t => t.AddUser(It.IsAny<User>())).Returns(true);
        MockBridge.Setup(t => t.SetUserSecret("TestUser","none".ToSHA512().ToHashedString())).Returns(true);
        var controller = BuildSignInController();
        AuthMessage signInMessage = new AuthMessage
        {
            Auth = AuthMessage.AuthType.USER,
            Identifier = "TestUser",
            Secret = "none"
        };
        var result = controller.Post(signInMessage);
        Assert.That(result.Secret == "none".ToSHA512().ToHashedString());
        Assert.That(result.Error == AuthMessage.ErrorReason.OK);
        Assert.That(result.Type == Message.MessageType.Response);
    }

}
