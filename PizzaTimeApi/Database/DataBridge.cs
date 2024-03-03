using System.Data.Common;
using PizzaTime.Data;
namespace PizzaTimeApi.Database;
public class DataBridge
{
    private DbConnection _connection;
    public DataBridge(DbConnection connection)
    {
        _connection = connection;
    }

    

    public User GetUserByName(string username){
        throw new NotImplementedException();
    }

    

}
