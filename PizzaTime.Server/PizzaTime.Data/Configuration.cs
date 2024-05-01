
namespace PizzaTime.Data;
using Microsoft.Extensions.Configuration;

public class WebAppConfiguration
{
    public string Database { get; set; } = "";
    public string Host { get; set; } = "";
    public int Port { get; set; } = 0;
    public string UserAgent { get; set; } = "";
    public bool Mocked { get; set; } = false;

    public string ToNpgConnectionString()
    {
        return $"User ID={UserAgent};Password=Pizza1234!;Host={Host};Port={Port};Database={Database};Connection Lifetime=0;";
    }
}