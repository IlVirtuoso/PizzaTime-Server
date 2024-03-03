class Pizza{
    public name : string = "";
    public id: number = 0.0;
    public description: string = "";
    public price: number = 0.0;

    public static generate(name: string) : Pizza{
        var id = Math.floor(Math.random()*100);
        var description = "a good pizza description with a lot of ingredients goes here";
        var price = Math.floor(Math.random()*1000);
        return {name,id,description,price};
    }
}

export default Pizza;