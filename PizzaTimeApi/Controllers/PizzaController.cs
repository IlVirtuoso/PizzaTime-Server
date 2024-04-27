using System.Text.Json.Serialization;
using Microsoft.AspNetCore.Mvc;
using PizzaTimeApi.Database;

namespace PizzaTimeApi.Controllers;
public class PizzaController: ControllerBase
{
    protected ILogger _logger;
    protected IDataBridge _bridge;
    public enum MessageType{
        OK,
        ERROR,
    }
    public abstract record class Message(MessageType type);  

    public record class Message<T>(T payload): Message(MessageType.OK);
    public record class OkMessage(): Message(MessageType.OK);
    public record class ErrorMessage(string Reason): Message(MessageType.ERROR);
    public PizzaController(ILogger logger, IDataBridge bridge){
        _logger = logger;
        _bridge = bridge;
    }

    protected IActionResult PerformBridgeOp<T>(Func<T> func, T expected, string errorReason){
        T res = func() ?? throw new ArgumentException("null value not allowed here");
        if(res.Equals(expected)){
            return new JsonResult(new OkMessage());
        }
        return new JsonResult(new ErrorMessage(errorReason));
    }
}
