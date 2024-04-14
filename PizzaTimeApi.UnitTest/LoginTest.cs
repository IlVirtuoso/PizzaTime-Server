using System.Security.Cryptography.X509Certificates;
using Castle.Core.Logging;
using Microsoft.Extensions.Logging.Abstractions;
using Moq;
using PizzaTime.Api;


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
        
    }

    [Test]
    public void TestPizzeriaLogin()
    {
       
    }
}