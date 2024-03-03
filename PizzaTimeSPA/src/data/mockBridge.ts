import Pizza from "./Pizza";
import Pizzeria from "./Pizzeria";
import User from "./User";
import IBridge from "./ibridge";


export default class MockBridge implements IBridge{

    GetUserByName(userName: string): User{
        return User.generate(userName);
    }

    GetPizzeriaByName(piva: string) : Pizzeria{
        return Pizzeria.generate(piva);
    }

    GetPizzaByName(name: string): Pizza{
        return Pizza.generate(name);
    }

};
