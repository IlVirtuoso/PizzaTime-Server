using System.Data.Common;
using System.Numerics;

namespace PizzaTime.Api;
public class DatabaseManager
{
    private Queue<Task> _actionQueue = new Queue<Task>();
    private int _closeTimeout = 5000;
    private bool _stop = false;
    private ManualResetEvent _onActionRequested = new ManualResetEvent(false);
    private Mutex _queueAccess = new Mutex(false);
    private DbConnection _connection;
    private ILogger _logger;
    private Thread _executionThread;
    public DatabaseManager(ILogger<DatabaseManager> logger, DbConnection dbConnection)
    {
        _connection = dbConnection;
        _logger = logger;
        _executionThread = new Thread(() => { Process(); });
        _executionThread.Start();
    }
    public void SetTimeout(int timeout)
    {
        _closeTimeout = timeout;
    }

    private void Process()
    {
        while (!_stop)
        {
            _queueAccess.WaitOne();
            if (_actionQueue.Count == 0)
            {
                _queueAccess.ReleaseMutex();
                _onActionRequested.WaitOne();
                _onActionRequested.Reset();
                _queueAccess.WaitOne();
            }
            var t = _actionQueue.Dequeue();
            t.RunSynchronously();
            _queueAccess.ReleaseMutex();
        }
    }

    private void EnqueueTask(Task task)
    {
        _queueAccess.WaitOne();
        _onActionRequested.Set();
        _actionQueue.Enqueue(task);
        _queueAccess.ReleaseMutex();
    }

    public Task Execute(Action<DbConnection> action)
    {
        var t = new Task(()=>{
            action(_connection);
        });
        EnqueueTask(t);
        return t;
    }



}
