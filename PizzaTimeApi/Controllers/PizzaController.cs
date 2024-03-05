using Microsoft.AspNetCore.Mvc;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers;
public class PizzaController: ControllerBase
{
    protected ILogger _logger;
    protected IDataBridge _bridge;
    public PizzaController(ILogger logger, IDataBridge bridge){
        _logger = logger;
        _bridge = bridge;
    }

    
}
