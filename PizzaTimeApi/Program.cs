
using System.Data.Common;
using Npgsql;
using PizzaTime.Data;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();

var config = builder.Configuration.AddJsonFile("./appsettings.json", false, true).Build();

var appconfig = config.GetSection("Configuration").Get<WebAppConfiguration>();

var app = builder.Build();

var connectionDescriptor = new ServiceDescriptor(typeof(DbConnection), (IServiceProvider t)=>{
    var connection = new NpgsqlConnection(appconfig.DatabaseConnectionString);
    return connection;
});


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

