using Microsoft.AspNetCore.Mvc;

namespace PizzaTime.Api;

[ApiController]
[Route("api/[controller]")]
public class HelloController:ControllerBase
{
    private ILogger _logger;
    public HelloController(ILogger<HelloController> logger){
        _logger = logger;
    }

    [HttpPost]
    public string Post(){
        return "Hello";
    }
}
