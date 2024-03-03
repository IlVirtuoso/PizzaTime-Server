
class Bridge{

    private _inited = false;


    public init() : Promise<void>{
        return fetch("/test").then();
    }

    public open(): void{

    }
    
}

export default Bridge;