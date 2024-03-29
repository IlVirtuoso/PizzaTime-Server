using Docker.DotNet;
using NUnit.Framework;
using Docker.DotNet.Models;
using System.Diagnostics;
namespace PizzaTime.ComponentTests
{
    public class TestEnvironment
    {
        protected DockerClient DockerClient { get; private set; } = null;
        public virtual void Startup()
        {

        }

        protected void StartClient()
        {
            DockerClient = new DockerClientConfiguration(new Uri("unix:///run/user/1000/podman/podman.sock")).CreateClient();
        }

        protected async Task BuildContainer()
        {

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


            client.Containers.CreateContainerAsync(new Docker.DotNet.Models.CreateContainerParameters()
            {
                Image = "docker.io/library/postgres:latest"
            }).Wait();


        }


    }
}