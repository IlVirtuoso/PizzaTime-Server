
using System.Data.Common;
using Npgsql;
using PizzaTime.Data;
using PizzaTimeApi.Database;
using Swashbuckle.AspNetCore.Swagger;
using Swashbuckle.AspNetCore;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using System.Data;
var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();

var config = builder.Configuration.AddJsonFile("./appsettings.json", false, true).Build();


var appconfig = config.GetSection("Configuration").Get<WebAppConfiguration>();


ServiceDescriptor? dataBridgeService = null;
if (appconfig.Mocked)
{
    dataBridgeService = new ServiceDescriptor(typeof(IDataBridge),(IServiceProvider t)=>{
        return new MockBridge();
    },ServiceLifetime.Singleton);
}
else
{

    var connectionDescriptor = new ServiceDescriptor(typeof(IDbConnection), (IServiceProvider t) =>
    {
        var connection = new NpgsqlConnection(appconfig.ToNpgConnectionString());
        connection.Open();
        return connection;
    }, ServiceLifetime.Singleton);

    dataBridgeService = new ServiceDescriptor(typeof(IDataBridge), (IServiceProvider s) =>
    {
        return new DataBridge(s.GetService<IDbConnection>() ?? throw new ArgumentException("No database found"));
    }, ServiceLifetime.Singleton);
}

builder.Services.Add(dataBridgeService ?? throw new ArgumentException("Connection bridge not configured"));
builder.Services.AddSwaggerGen();
var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.MapGet("/debug/routes", (IEnumerable<EndpointDataSource> endpointSources) =>
       string.Join("\n", endpointSources.SelectMany(source => source.Endpoints)));
    app.UseSwaggerUI((options) =>
    {
        options.SwaggerEndpoint("/swagger/v1/swagger.json", "v1");
        options.RoutePrefix = string.Empty;
    }
    );
}



// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseStaticFiles();
app.UseRouting();

app.MapControllers();
app.MapFallbackToFile("index.html"); ;

app.Run();

