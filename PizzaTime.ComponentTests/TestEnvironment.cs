using NUnit.Framework;
using System.Diagnostics;
using System.Runtime.InteropServices;
using System.Net.WebSockets;
namespace PizzaTime.ComponentTests
{
    public class TestEnvironment
    {
        DirectoryInfo oldFolder;
        DirectoryInfo mainFolder;
        public async virtual Task Startup()
        {
            oldFolder = new DirectoryInfo(Directory.GetCurrentDirectory());
            mainFolder = ToProjectFolder().EnumerateDirectories().Where(t=> t.Name == "Docker").First();
            Directory.SetCurrentDirectory(mainFolder.FullName);
            await ComposeUp();
            Directory.SetCurrentDirectory(oldFolder.FullName);
        }

        protected DirectoryInfo ToProjectFolder(){
            var executionSrc = System.Reflection.Assembly.GetEntryAssembly().Location;
            var cwd = Directory.GetParent(executionSrc) ?? throw new ArgumentException("Invalid directory");
            while(!cwd.GetDirectories().Any(t=> t.Name.Contains("Docker"))){
                cwd = cwd.Parent;
            }
            return cwd;
        }

        private Task RunProcAndWait(ProcessStartInfo ps){
            return Task.Factory.StartNew(()=>{
                    var process = Process.Start(ps);
                    process.WaitForExit(10000);             
            });
        }

        protected async Task ComposeUp(){
            var pc = new ProcessStartInfo("python3");
            pc.Arguments = "composer.py -u";
            await RunProcAndWait(pc);  
        }

        protected async Task ComposeDown(){
            
        }



        protected async virtual Task CleanUp(){
            
        }

        [Test]
        public void TestContainer()
        {
            var cwd = Directory.GetCurrentDirectory();
            var newd = ToProjectFolder().EnumerateDirectories().Where(t=> t.Name == "Docker").First();
            Directory.SetCurrentDirectory(newd.FullName);
            ComposeUp().Wait();
            ComposeDown().Wait();
            Directory.SetCurrentDirectory(cwd);
        }

    }
}