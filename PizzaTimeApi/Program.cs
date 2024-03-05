
using System.Data.Common;
using Npgsql;
using PizzaTime.Data;
using PizzaTimeApi.Database;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();

var config = builder.Configuration.AddJsonFile("./appsettings.json", false, true).Build();

var appconfig = config.GetSection("Configuration").Get<WebAppConfiguration>();

var connectionDescriptor = new ServiceDescriptor(typeof(DbConnection), (IServiceProvider t)=>{
    var connection = new NpgsqlConnection(appconfig.ToNpgConnectionString());
    connection.Open();
    return connection;
},ServiceLifetime.Singleton);

var databridgeService = new ServiceDescriptor(typeof(IDataBridge), (IServiceProvider s)=>{
    return new DataBridge(s.GetService<DbConnection>() ?? throw new ArgumentException("No database found"));
});


builder.Services.Add(connectionDescriptor);

var app = builder.Build();




// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();


app.MapControllerRoute(
    name: "default",
    pattern: "{controller}/{action}/{id?}");

app.MapFallbackToFile("index.html"); ;

app.Run();

