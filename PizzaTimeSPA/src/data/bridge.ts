import Pizza from "./Pizza";
import Pizzeria from "./Pizzeria";
import User from "./User";
import IBridge from "./ibridge";

class Bridge implements IBridge{
    
    GetUserByName(userName: string): User {
        throw new Error("Method not implemented.");
    }
    GetPizzeriaByName(piva: string): Pizzeria {
        throw new Error("Method not implemented.");
    }
    GetPizzaByName(name: string): Pizza {
        throw new Error("Method not implemented.");
    }


    public init() : Promise<void>{
        return fetch("/test").then();
    }

    public open(): void{

    }
    
}

export default Bridge;