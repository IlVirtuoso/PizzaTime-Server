import { User } from "./User";

export class Customer extends User {
  public constructor(
    email: string,
    phone: string,
    address: string,
    name: string,
    surname: string
  ) {
    super(email, phone, address);
  }
}
