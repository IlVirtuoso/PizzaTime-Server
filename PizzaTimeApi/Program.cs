
using PizzaTime.Bridge;
using PizzaTime.Data;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();

var config = builder.Configuration.AddJsonFile("./appsettings.json", false, true).Build();

builder.Services.AddBridge(config.GetSection("Configuration").Get<WebAppConfiguration>()?? throw new ArgumentException("Configuration is corrupted"));


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

