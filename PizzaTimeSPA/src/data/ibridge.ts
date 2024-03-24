import Pizza from "./Pizza";
import Pizzeria from "./Pizzeria";
import User from "./User";

interface IBridge{
    GetUserByName(userName: string): User;
    GetPizzeriaByName(piva: string) : Pizzeria;
    GetPizzaByName(name: string): Pizza;
    
};

export default IBridge;