import IBridge from "./ibridge";
import MockBridge from "./mockBridge";

export class ApplicationServices {
    dataBridge?: IBridge;

    static instance?: ApplicationServices;
    public get Bridge(): IBridge{
        if(this.dataBridge == null){
            this.dataBridge = new MockBridge();
        }
        return this.dataBridge ;
    }

    private constructor(){

    }

    public static Instance(){
        if(!ApplicationServices.instance) {
            ApplicationServices.instance = new ApplicationServices();
        }
        return ApplicationServices.instance;
    }

    public AddApplicationBridge(bridge:IBridge){
        this.dataBridge = bridge;
    }



}