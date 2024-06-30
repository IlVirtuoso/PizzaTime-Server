import { Injectable } from '@angular/core';
import { IDataBridge } from '../idatabridge';
import { CookieService } from 'ngx-cookie-service';
import { Axios } from 'axios';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ingredient, Menu, Order, Pizza, Pizzeria, User } from '@data';

class LoginRequest{constructor(public username: string, public password: string){}}


var gatewayUrl:string = "localhost:8000";

//ACCOUNT URLs
var loginPath:string = gatewayUrl + "/login";
var socialLoginPath:string = gatewayUrl + "/socialLogin";
var registerPath:string = gatewayUrl + "/register";
var finalizeRegPath:string = gatewayUrl + "/finalizeRegistration";
var getAccountPath:string = gatewayUrl + "/getAccountInfo";
var setAccountPath:string = gatewayUrl + "/setAccountInfo";
var changePwdPath:string = gatewayUrl + "/changePassword";
var getJWTPath:string = gatewayUrl + "/getJWT";
var deleteAccountPath:string = gatewayUrl + "/deleteAccount";
var rechargePath:string = gatewayUrl + "/rechargeBalance";

//PIZZERIA URLs
var createPizzeriaPath:string = gatewayUrl + "/createPizzeria";
var getPizzeriaPath:string = gatewayUrl + "/getPizzeriaInfo";
var createMenuPath:string = gatewayUrl + "/createMenu";
var addAdditionPath:string = gatewayUrl + "/addAdditionToMenu";
var addpizzaPath:string = gatewayUrl + "/addPizzaToMenu";
var getMenuPath:string = gatewayUrl + "/getMenu";
var openPizzeriaPath:string = gatewayUrl + "/openPizzeria";
var closePizzeriaPath:string = gatewayUrl + "/closePizzeria";
var getRowsForOrderPath:string = gatewayUrl + "/getMenuForOrder";


//ITEMS URLs
var getAllPastryPath:string = gatewayUrl + "/getAllPastry";
var getAllSeasoningPath:string = gatewayUrl + "/getAllSeasoning";
var getAllPizzaPath:string = gatewayUrl + "/getAllPizza";
var getAllIngredientPath:string =  gatewayUrl + "/getAllIngredient";
var getIngredientPath:string = gatewayUrl + "/getIngredient";
var getPizzaPath:string = gatewayUrl + "/getPizza";



@Injectable({
  providedIn: 'root'
})
export class DataBridgeService {

  constructor(private cookieService: CookieService,
    private promiseClient: Axios,
    private fetcher : HttpClient
  ) {

  }

  // METHODS FOR ACCCOUNT

  //login definition
  async login(username: String, password: String): Promise<Boolean> {
    var data ={
      "username":username,
      "password":password
    }
    try {
      const response = await this.promiseClient.post(loginPath, data, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      //ERROR CODE 0 = success
        //RECALL to save the sessionToken
      //ERROR CODE 206 = redirect to finalize registration page
        //recall to save the regToken
      //Other ERRORS: invalid "login id or password"

      return response.data;
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
  }

    //social login definition
    async socialLogin(): Promise<Boolean> {

      var oauthToken:string = "PUT HERE THE GOOGLE OAUTH TOKEN"

      try {
        const response = await this.promiseClient.post(socialLoginPath,null, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':oauthToken
          }
        });

        //ERROR CODE 0 = success
          //RECALL to save the sessionToken
        //ERROR CODE 206 = redirect to finalize registration page
          //recall to save the regToken
        //ERROR CODE 401 = google token is expired: repeat social login
        //Other ERRORS: invalid "login id or password"

        return response.data;
      } catch (error) {
          console.error('Error in social login:', error);
          throw error;
      }
    }


    //register definition
    async signin(firstName:string, lastName:string, username: string, password : string
    ): Promise<Boolean> {
      var data={
        "firstName":firstName,
        "lastName":lastName,
        "username": username,
        "password" : password
      }

      try {
        const response = await this.promiseClient.post(registerPath,data, {
          headers: {
            'Content-Type': 'application/json',
          }
        });

        //ERROR CODE 0 = success
          //RECALL to save the sessionToken
        //ERROR CODE 206 = redirect to finalize registration page
          //recall to save the regToken
        //Other ERRORS: invalid "login id or password"

        return response.data;
      } catch (error) {
          console.error('Ereror in registration:', error);
          throw error;
      }
    }

     //finalize registration definition
     async finalizeReg(firstName:string, lastName:string, address: string, phone : string, mobile:string
     ): Promise<Boolean> {

      var regToken:string = "PUT HERE THE REG TOKEN INSIDE THE COOKIE"

      var data={
         "firstName":firstName,
         "lastName":lastName,
         "address": address,
         "phone" : phone,
         "mobile":mobile
       }

       try {
         const response = await this.promiseClient.post(finalizeRegPath,data, {
           headers: {
             'Content-Type': 'application/json',
             'Authorization':regToken
           }
         });

         //ERROR CODE 0 = success
           //RECALL to save the sessionToken
         //ERROR CODE 203 = User avoid some important data: repeat
         //Other ERRORS: general errors

         return response.data;
       } catch (error) {
           console.error('Error in finalizing registration :', error);
           throw error;
       }
     }

      //set account definition
      async setUserData(firstName:string, lastName:string, address: string, phone : string, mobile:string
      ): Promise<Boolean> {

        var sessionToken:string = "PUT HERE THE SESSION TOKEN INSIDE THE COOKIE"

        var data={
          "firstName":firstName,
          "lastName":lastName,
          "address": address,
          "phone" : phone,
          "mobile":mobile
        }

        try {
          const response = await this.promiseClient.post(setAccountPath,data, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization':sessionToken
            }
          });

          //ERROR CODE 0 = success, user object is returned
          //ERROR CODE 401 = user is malicious or the session is expired
          //ERROR CODE 400 = "invalid data provided"

          return response.data;
        } catch (error) {
            console.error('Error in setting account info:', error);
            throw error;
        }
      }



    // get account definition
    async getUser(): Promise<User| null> {

      var sessionToken:string = "PUT HERE THE SESSION  TOKEN"

      try {
        const response = await this.promiseClient.get(getAccountPath, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':sessionToken
          }
        });

        //ERROR CODE 0 = success, return the account object
        //ERROR CODE 401 = session token is expired: repeat social login
        //Other ERRORS: general error

        return response.data.account;
      } catch (error) {
          console.error('Error in getting account info:', error);
          throw error;
      }
    }


    // get account definition
    async getUserBalance(): Promise<Number> {

      var account = this.getUser();

        //CHECK IF ACCOUNT IS NULL or NOT...
        //ERROR CODE 0 = success, return the account object
        //ERROR CODE 401 = session token is expired: repeat social login
        //Other ERRORS: general error
        var user = await account;
        if(user == null){
          throw new Error();
        }
        return user?.balance;
    }


      // get account definition
      async deleteUser(): Promise<Boolean> {

        var sessionToken:string = "PUT HERE THE SESSION  TOKEN"

        try {
          const response = await this.promiseClient.get(deleteAccountPath, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization':sessionToken
            }
          });

          //ERROR CODE 0 = success
          //ERROR CODE 401 = session token is expired: repeat social login
          //Other ERRORS: general error

          return response.data;
        } catch (error) {
            console.error('Error in deleting account:', error);
            throw error;
        }
      }

      // get JWT
      async getJWT(): Promise<Boolean> {

        var sessionToken:string = "PUT HERE THE SESSION  TOKEN"

        try {
          const response = await this.promiseClient.get(getJWTPath, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization':sessionToken
            }
          });

          //ERROR CODE 0 = success
          //ERROR CODE 401 = session token is expired: repeat social login
          //Other ERRORS: general error

          return response.data;
        } catch (error) {
            console.error('Error in getting jwt:', error);
            throw error;
        }
      }


      //change password definition
      async changePassword(oldPassword:string, newPassword:string
      ): Promise<Boolean> {

        var sessionToken:string = "PUT HERE THE SESSION TOKEN INSIDE THE COOKIE"

        var data={
          "oldPassword":oldPassword,
          "newPassword":newPassword
        }

        try {
          const response = await this.promiseClient.post(changePwdPath,data, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization':sessionToken
            }
          });

          //ERROR CODE 0 = success, user object is returned
          //ERROR CODE 401 = user is malicious or the session is expired
          //ERROR CODE 202 = "password too easy"

          return response.data;
        } catch (error) {
            console.error('Error in changing password:', error);
            throw error;
        }
      }

      //recharge balance definition
      async recharge(value:number
      ): Promise<Boolean> {

        var sessionToken:string = "PUT HERE THE SESSION TOKEN INSIDE THE COOKIE"

        try {
          const response = await this.promiseClient.get(rechargePath, {
            params:{"value":value},
            headers: {
              'Content-Type': 'application/json',
              'Authorization':sessionToken
            }

          });

          //ERROR CODE 0 = success, user object is returned
          //ERROR CODE 401 = user is malicious or the session is expired

          return response.data;
        } catch (error) {
            console.error('Error in recharging the balance:', error);
            throw error;
        }
      }


    // METHODS FOR PIZZERIA HANDLING



    //create pizzeria definition
     async createPizzeria(name:string, vatNumber:string, address: string
     ): Promise<Boolean> {

      var sessionToken:string = "PUT HERE THE SESSION TOKEN INSIDE THE COOKIE"

      var data={
         "name":name,
         "address": address,
         "vatNumber" : vatNumber,
       }

       try {
         const response = await this.promiseClient.post(createPizzeriaPath,data, {
           headers: {
             'Content-Type': 'application/json',
             'Authorization':sessionToken
           }
         });

         //ERROR CODE 0 = success
           //VERY IMPORTANT RECALL to save the sessionToken
         //ERROR CODE 203 = User avoid some important data: repeat
         //ERROR CODE 401 = user is malicious or the session is expired
         //Other ERRORS: general errors

         return response.data;
       } catch (error) {
           console.error('Error in creating a pizzeria :', error);
           throw error;
       }
     }



    // get pizzeria definition
    async getManagedPizzeria(): Promise<Pizzeria | null> {

      var sessionToken:string = "PUT HERE THE SESSION  TOKEN"

      try {
        const response = await this.promiseClient.get(getPizzeriaPath, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':sessionToken
          }
        });

        //ERROR CODE 0 = success, return the pizzeria object
        //ERROR CODE 401 = session token is expired or the user is not a manager
        //Other ERRORS: general error

        return response.data.account;
      } catch (error) {
          console.error('Error in getting pizzeria info:', error);
          throw error;
      }
    }


    //add addition definition
    async addAdditionToMenu(additions:Ingredient[]
    ): Promise<Boolean> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"
     var data={
        "additions":additions
      }

      try {
        const response = await this.promiseClient.post(addAdditionPath,data, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':idToken
          }
        });

        //ERROR CODE 0 = success
          //VERY IMPORTANT RECALL to save the sessionToken
        //ERROR CODE 401 = user is malicious or the session is expired

        return response.data;
      } catch (error) {
          console.error('Error in adding an ingredient :', error);
          throw error;
      }
    }

     //add pizza definition
     async addAPizzaToMenu(pizzas:Pizza[]
     ): Promise<Boolean> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"
      var data={
         "pizzas":pizzas
       }

       try {
         const response = await this.promiseClient.post(addpizzaPath,data, {
           headers: {
             'Content-Type': 'application/json',
             'Authorization':idToken
           }
         });

         //ERROR CODE 0 = success
           //VERY IMPORTANT RECALL to save the sessionToken
         //ERROR CODE 401 = user is malicious or the session is expired

         return response.data;
       } catch (error) {
           console.error('Error in adding a pizza :', error);
           throw error;
       }
     }



    // create menu definition
    async createMenu(): Promise<Boolean> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"

      try {
        const response = await this.promiseClient.get(createMenuPath, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':idToken
          }
        });

        //ERROR CODE 0 = success, return the pizzeria object
        //ERROR CODE 401 = session token is expired or the user is not a manager
        //Other ERRORS: general error

        return response.data.account;
      } catch (error) {
          console.error('Error in creaeting menu:', error);
          throw error;
      }
    }


    // get menu definition
    async getMenu(): Promise<Menu | null> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"

      try {
        const response = await this.promiseClient.get(getMenuPath, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':idToken
          }
        });

        //ERROR CODE 0 = success, return the pizzeria object
        //ERROR CODE 208 = menu doesn't exist: create calling the create menu
        //ERROR CODE 401 = session token is expired or the user is not a manager
        //Other ERRORS: general error

        return response.data.account;
      } catch (error) {
          console.error('Error in getting menu:', error);
          throw error;
      }
    }

    // open pizzeria definition
    async openPizzeria(): Promise<Boolean> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"

      try {
        const response = await this.promiseClient.get(openPizzeriaPath, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':idToken
          }
        });

        //ERROR CODE 0 = success, return the pizzeria object
        //ERROR CODE 401 = session token is expired or the user is not a manager
        //Other ERRORS: general error

        return response.data.account;
      } catch (error) {
          console.error('Error in opening the pizzeria :', error);
          throw error;
      }
    }


    // open pizzeria definition
    async closePizzeria(): Promise<Boolean> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"

      try {
        const response = await this.promiseClient.get(closePizzeriaPath, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization':idToken
          }
        });

        //ERROR CODE 0 = success, return the pizzeria object
        //ERROR CODE 401 = session token is expired or the user is not a manager
        //Other ERRORS: general error

        return response.data.account;
      } catch (error) {
          console.error('Error in closing the pizzeria :', error);
          throw error;
      }
    }



     //get pizzeria's menu rows for an order pizza definition
     async getMenuRowsForOrder(order:Order[]
     ): Promise<Boolean> {

      var idToken:string = "PUT HERE THE idtoken obatined with a getJWT"
      var data={
         "order":order
       }

       try {
         const response = await this.promiseClient.post(getRowsForOrderPath,data, {
           headers: {
             'Content-Type': 'application/json',
             'Authorization':idToken
           }
         });

         //ERROR CODE 0 = success, receive in orderData the rows requested
         //ERROR CODE 401 = user is malicious or the session is expired

         return response.data;
       } catch (error) {
           console.error('Error in getting rows for the order requested :', error);
           throw error;
       }
     }

    // METHODS TO HANDLE ITEMS



  getAvailableSeasonig(): Observable<Ingredient[]> {
    return this.fetcher.get(getAllSeasoningPath) as Observable<Ingredient[]>;
  }

  getAvailablePastry(): Observable<Ingredient[]> {
    return this.fetcher.get(getAllPastryPath) as Observable<Ingredient[]> ;
  }

  getAvailableIngredient(): Observable<Ingredient[]> {
    return this.fetcher.get(getAllIngredientPath) as Observable<Ingredient[]>;
  }

  getAvailablePizza(): Observable<Pizza[]> {
    return this.fetcher.get(getAllPizzaPath) as Observable<Pizza[]>;
  }



  /* get all seasoning definition
  async getAvailableSeasonig(): Promise<Ingredient[] | null> {


  try {
    const response = await this.promiseClient.get(getAllSeasoningPath, {
      headers: {
        'Content-Type': 'application/json',
      }
    });

    //ERROR CODE 0 = success, return the pizzeria object
    //ERROR CODE 208 = menu doesn't exist: create calling the create menu
    //ERROR CODE 401 = session token is expired or the user is not a manager
    //Other ERRORS: general error

    return response.data.account;
  } catch (error) {
      console.error('Error in getting all seasoning:', error);
      throw error;
  }
}*/



}
