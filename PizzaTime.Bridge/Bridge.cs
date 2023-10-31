using System.Data.Common;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using PizzaTime.Data;
using Npgsql;

namespace PizzaTime.Bridge;


public class Bridge
{
    private WebAppConfiguration _config;
    private DbConnection _connection;
    public Bridge(WebAppConfiguration configuration){
        _config = configuration;
        _connection = new NpgsqlConnection(_config.DatabaseConnectionString);
    }
    public void Connect()
    {
        _connection.Open();
    }

    public Task ConnectAsync(){
        return _connection.OpenAsync();
    }
}

public static class BridgeBuilderExtension
{
    public static IServiceCollection AddBridge(this IServiceCollection builder, WebAppConfiguration configuration){
        var service = new ServiceDescriptor(typeof(Bridge),(servicebuilder)=>{
            return new Bridge(configuration);
        },ServiceLifetime.Singleton);
        builder.Add(service);
        return builder;
    }
}