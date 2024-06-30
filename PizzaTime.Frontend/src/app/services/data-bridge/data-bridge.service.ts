import { Injectable } from '@angular/core';
import { AddIngrRequest, AddPizzaRequest, IDataBridge, getMenuRowsForOrderRequest } from '../idatabridge';
import { CookieService } from 'ngx-cookie-service';
import axios, { Axios , AxiosRequestConfig} from 'axios';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ingredient, Menu, Order, Pizza, Pizzeria, ResponseMessage, User } from '@data';



class LoginRequest{constructor(public username: string, public password: string){}}


var gatewayUrl:string = "http://localhost:8000";

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
export class DataBridgeService extends IDataBridge{

  constructor(private cookieService: CookieService,
    private promiseClient: Axios,
    private fetcher : HttpClient
  ) {
    super();
  }

  // METHODS FOR ACCCOUNT

  //login definition




  override async login(username: string, password: string): Promise<number> {


    var data ={
      "username":username,
      "password":password
    }
    try {
      const response = await this.promiseClient.post(loginPath, data, {
        method:"POST",
        headers: {
          'Content-Type': 'application/json'
        }
      } );

      if(response.data.statusCode==206){
        this.lastError=response.data.statusReason;
        this.cookieService.set("Session", response.data.regToken);
        this.regModeOnly = true;
        return 206;
      }else if(response.data.statusCode!=0){
        this.lastError=response.data.statusReason;
        return response.data.statusCode;

      }

      this.cookieService.set("Session", response.data.sessionToken)
      //FOR ID TOKEN this.cookieService.set("Authorization", response.data.idToken)

      return response.data.statusCode;
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
  }

    //social login definition
    async socialLogin(): Promise<number> {

      var oauthToken:string = this.cookieService.get("Authorization");

      console.log("requested a social login with oauth token:" + oauthToken);
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

        if(response.data.statusCode==206){
          this.lastError=response.data.statusReason;
          this.cookieService.set("Session", response.data.regToken);
          this.regModeOnly = true;
          return 206;
        }else if(response.data.statusCode!=0){
          this.lastError=response.data.statusReason;
          return response.data.statusCode;
  
        }
  
        this.cookieService.set("Session", response.data.sessionToken)
        //FOR ID TOKEN this.cookieService.set("Authorization", response.data.idToken)
  
        return response.data.statusCode;
        } catch (error) {
          console.error('Error in social login:', error);
          throw error;
      }
    }


    //register definition
    override async signin(firstName:string, lastName:string, username: string, password : string
    ): Promise<number> {
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
        
      if(response.data.statusCode==206){
        this.lastError=response.data.statusReason;
        this.cookieService.set("Session", response.data.regToken);
        this.regModeOnly = true;
        return 206;
      }else if(response.data.statusCode!=0){
        this.lastError=response.data.statusReason;
        return response.data.statusCode;
      }

      this.cookieService.set("Session", response.data.sessionToken)
      //FOR ID TOKEN this.cookieService.set("Authorization", response.data.idToken)

      return response.data.statusCode;
      } catch (error) {
          console.error('Ereror in registration:', error);
          throw error;
      }
    }

     //finalize registration definition
     override async finalizeRegistration(firstName:string, lastName:string, address: string, phone : string, mobile:string
     ): Promise<number> {

      var regToken:string = this.cookieService.get("Session");

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

         if(response.data.statusCode==206){
          this.lastError=response.data.statusReason;
          this.cookieService.set("Session", response.data.regToken);
          this.regModeOnly = true;
          return 206;
        }else if(response.data.statusCode!=0){
          this.lastError=response.data.statusReason;
          return response.data.statusCode;
  
        }
  
        this.cookieService.set("Session", response.data.sessionToken)
        //FOR ID TOKEN this.cookieService.set("Authorization", response.data.idToken)
  
        return response.data.statusCode;
       } catch (error) {
           console.error('Error in finalizing registration :', error);
           throw error;
       }
     }

      //set account definition
      override async setUserData(firstName:string, lastName:string, address: string, phone : string, mobile:string
      ): Promise<boolean> {

        var sessionToken:string = this.cookieService.get("Session");

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

        if(response.data.statusCode!=0){
          this.lastError=response.data.statusReason;
          return false;
        }
  
        return true;
       
          //ERROR CODE 0 = success, user object is returned
          //ERROR CODE 401 = user is malicious or the session is expired
          //ERROR CODE 400 = "invalid data provided"

        } catch (error) {
            console.error('Error in setting account info:', error);
            throw error;
        }
      }



    // get account definition
    override async getUser(): Promise<User| null> {

      var sessionToken:string = this.cookieService.get("Session");

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

        if(response.data.statusCode!=0){
          this.lastError=response.data.statusReason;
          return null;
        }

        return response.data.account;
      } catch (error) {
          console.error('Error in getting account info:', error);
          throw error;
      }
    }


    // get account definition
    override async getUserBalance(): Promise<number> {

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
      override async deleteUser(): Promise<boolean> {

        var sessionToken:string = this.cookieService.get("Session");

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

          if(response.data.statusCode!=0){
            this.lastError=response.data.statusReason;
            return false;
          }
    
          return true;
        } catch (error) {
            console.error('Error in deleting account:', error);
            throw error;
        }
      }

      // get JWT
      override async getJWT(): Promise<boolean> {

        var sessionToken:string = this.cookieService.get("Session");

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

          if(response.data.statusCode!=0){
            this.lastError=response.data.statusReason;
            return false;
          }
    
          //this.cookieService.set("Session", response.data.sessionToken)
          this.cookieService.set("Authorization", response.data.idToken);
    
          return true;
        } catch (error) {
            console.error('Error in getting jwt:', error);
            throw error;
        }
      }


      //change password definition
      override async changePassword(oldPassword:string, newPassword:string
      ): Promise<boolean> {

        var sessionToken:string = this.cookieService.get("Session");

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

          if(response.data.statusCode!=0){
            this.lastError=response.data.statusReason;
            return false;
          }
  
          return true;
        } catch (error) {
            console.error('Error in changing password:', error);
            throw error;
        }
      }

      //recharge balance definition
      override async recharge(value:number
      ): Promise<boolean> {

        var sessionToken:string = this.cookieService.get("Session");

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

          if(response.data.statusCode!=0){
            this.lastError=response.data.statusReason;
            return false;
          }
  
          return true;  
        } catch (error) {
            console.error('Error in recharging the balance:', error);
            throw error;
        }
      }


    // METHODS FOR PIZZERIA HANDLING



    //create pizzeria definition
     override async createPizzeria(name:string, vatNumber:string, address: string
     ): Promise<boolean> {

      var sessionToken:string = this.cookieService.get("Session");

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

         if(response.data.statusCode!=0){
          this.lastError=response.data.statusReason;
          return false;
        }
  
        this.cookieService.set("Session", response.data.sessionToken)
        //FOR ID TOKEN this.cookieService.set("Authorization", response.data.idToken)
  
        return true;

        } catch (error) {
           console.error('Error in creating a pizzeria :', error);
           throw error;
       }
     }



    // get pizzeria definition
    override async getManagedPizzeria(): Promise<Pizzeria | null> {

      var sessionToken:string = this.cookieService.get("Session");

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

        if(response.data.statusCode!=0){
          this.lastError=response.data.statusReason;
          return null;
        }
        return response.data.pizzeria;
      } catch (error) {
          console.error('Error in getting pizzeria info:', error);
          throw error;
      }
    }


    //add addition definition
    override async addAdditionToMenu(additions:AddIngrRequest[]
    ): Promise<boolean> {

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
     override async addPizzaToMenu(pizzas:AddPizzaRequest[]
     ): Promise<boolean> {

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
    override async createMenu(): Promise<boolean> {

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
    override async getMenu(): Promise<Menu | null> {

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
    override async openPizzeria(): Promise<boolean> {

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
    override async closePizzeria(): Promise<boolean> {

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


      /*
     //get pizzeria's menu rows for an order pizza definition
     override async getMenuRowsForOrder(order:Order[]
     ): Promise<boolean> {

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
       */

     getMenuRowsForOrder(order:{order: getMenuRowsForOrderRequest[]}): Observable<Menu[]> {
      const headers = new HttpHeaders({
        'Authorization': this.cookieService.get("Authorization")
      });
  
      return this.fetcher.post(getRowsForOrderPath,order) as Observable<Menu[]>;
    }
    
    
     // METHODS TO HANDLE ITEMS


  getAvailableSeasoning(): Observable<Ingredient[]> {
    return this.fetcher.get(getAllSeasoningPath) as Observable<Ingredient[]>;
  }

  getAvailablePastry(): Observable<Ingredient[]> {
    return this.fetcher.get(getAllPastryPath) as Observable<Ingredient[]> ;
  }

  getAvailableIngredients(): Observable<Ingredient[]> {
    return this.fetcher.get(getAllIngredientPath) as Observable<Ingredient[]>;
  }

  getAvailablePizza(): Observable<Pizza[]> {
    return this.fetcher.get(getAllPizzaPath) as Observable<Pizza[]>;
  }


  /** TEMP */
  getPizzeriaOrders(piva: String): Observable<Order[]> {
    const headers = new HttpHeaders({
      'Authorization': this.cookieService.get("Authorization")
    });
    return this.fetcher.get(getAllPizzaPath) as Observable<Order[]>;
  }

  getOrdersForPizzeria(piva: String): Observable<Order[]> {
    const headers = new HttpHeaders({
      'Authorization': this.cookieService.get("Authorization")
    });
    return this.fetcher.get(getAllPizzaPath) as Observable<Order[]>;
  }

  getOrdersForUser(username: String): Observable<Order[]> {
    const headers = new HttpHeaders({
      'Authorization': this.cookieService.get("Authorization")
    });
    return this.fetcher.get(getAllPizzaPath) as Observable<Order[]>;
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
