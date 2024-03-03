import { Config, names, starWars, uniqueNamesGenerator } from "unique-names-generator";

export default class Pizzeria{
    public email : string = "";
    public piva: string = "";
    public address: string = "";

    public static generate(piva: string) : Pizzeria{
        const config : Config = {dictionaries:[names]};
        const st : Config = {dictionaries:[starWars]};
        var email = `${uniqueNamesGenerator(config)}.pizzeria@${uniqueNamesGenerator(st)}`;
        var address = `via ${uniqueNamesGenerator(config)} ${Math.floor(Math.random()*100)}`;
        return {email,piva,address};
    }
};