import { Config, adjectives, countries, names, uniqueNamesGenerator } from "unique-names-generator";


class User{
    public userName: string = "";
    public email: string = "";
    public name : string = "";
    public surname: string = "";
    


    public static generate(userName: string): User{
        const nameconfig: Config = {
          dictionaries: [names],
        };

        const emailconf : Config = { dictionaries:[countries]}
        var name = uniqueNamesGenerator(nameconfig);
        var surname = uniqueNamesGenerator(nameconfig);
        var email = `${name}.${surname}@${uniqueNamesGenerator(emailconf)}`;

        return {userName:userName, name:name, surname:surname, email:email};
    }
}

export default User;