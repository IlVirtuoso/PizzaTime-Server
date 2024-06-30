

export class User {
  public constructor(
    public id:number,
    public username:string,
    public email: string = "",
    public phone: string,
    public mobile:string,
    public address: string = "",
    public firstName: string= "",
    public lastName: string= "",
    public surname: string = "",
    public glutenPrerence: boolean = false,
    public isVendor : boolean = false,
    public socialIdentity : boolean,
    public isRegistered: boolean,
    public float:number
  ) {

  }
}
