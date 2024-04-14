using System.Security.AccessControl;
using Castle.Core.Logging;
using FluentAssertions;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Logging.Abstractions;
using Microsoft.IdentityModel.Tokens;
using Microsoft.VisualStudio.TestPlatform.CommunicationUtilities;
using Moq;
using PizzaTime.Api;

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
        var controller = BuildSignInController();
        User commit = null;
        MockBridge.Setup(t=> t.AddUser(It.IsAny<User>())).Callback<User>(t=> commit = t).Returns(true);
        var user = new User
        {
            Name = "matteo",
            SurName = "ielacqua",
            Email = "matteo.ielacqua@gmail.com",
            UserName = "IlVirtuoso"
        };
        
        var response = controller.UserSignin(user.UserName, "none", user.Email, user.Name, user.SurName);
        commit.Should().BeEquivalentTo(user);
    }

    [Test]
    public void TestPizzeriaSignIn()
    {
        var controller = BuildSignInController();

    }


}
