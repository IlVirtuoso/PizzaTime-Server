import { User } from "./User";

export class Pizzeria extends User {
  public constructor(
    email: string,
    phone: string,
    piva: string,
    name: string,
    address: string
  ) {
    super(email, phone, address);
  }
}
