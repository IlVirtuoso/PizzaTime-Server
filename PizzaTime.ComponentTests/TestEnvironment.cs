using Docker.DotNet;
using NUnit.Framework;
using Docker.DotNet.Models;
using System.Diagnostics;
namespace PizzaTime.ComponentTests
{
    public class TestEnvironment
    {
        protected DockerClient Client { get; private set; } = null;
        public virtual void Startup()
        {

        }

        protected void StartClient()
        {

            
            Client = new DockerClientConfiguration(new Uri("unix:///run/user/1000/podman/podman-machine-default-api.sock")).CreateClient();

        }

        protected async Task BuildContainer()
        {
           await Client.Images.BuildImageFromDockerfileAsync(new ImageBuildParameters()
            {
                Tags = new List<string> (){ "pizzatimeservice:lastest" },
                Dockerfile = "/home/drfaust/Scrivania/uni/Magistrale/TAAS/project/PizzaTimeService/PizzaTimeApi/Docker/Service.dockerfile",
            }, Stream.Null , null, new Dictionary<string, string>() { }, new Progress<JSONMessage>(msg => Debug.WriteLine($"{msg.Status}")));



        }

        protected async Task StartContainer()
        {

        }

        protected async Task StartDatabase()
        {

        }

        [Test]
        public void TestContainer()
        {
            StartClient();

            var contianer = Client.Containers.ListContainersAsync(
                new ContainersListParameters()
            ).Result;

        }

        [Test]
        public void TestContainerBuild(){
            StartClient();
            BuildContainer().Wait();

        }

    }
}