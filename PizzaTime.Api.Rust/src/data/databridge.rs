
pub trait IDataBridge {
    
}

pub trait IDBConnection {
    
}

pub struct DataBridge {
    connection : &'static dyn IDBConnection,
}


impl DataBridge {
    pub fn new(connection : &'static dyn IDBConnection)-> Self{
        DataBridge{connection: connection}
    }
}