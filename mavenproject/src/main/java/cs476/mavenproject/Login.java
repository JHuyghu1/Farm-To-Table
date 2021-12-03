package cs476.mavenproject;

import java.util.Scanner;

public class Login {

    Database DB;
    Scanner input;
    Categories categories;

    public Login(Database DB, Scanner input, Categories categories){

        this.DB = DB;
        this.input = input;
        this.categories = categories;

    }

    public Buyer createNewUser(){

        String username = "";
        String password = "";
        String address = "";

        System.out.println("Create a new account");
        System.out.println("--------------------\n");

        while(username == ""){
            System.out.print("Enter your new username: ");
            String tempName = input.nextLine();

            if(!DB.buyerUsernameExists(tempName)){
                username = tempName;
            } else {
                System.out.println("That username is already taken!\n");
            }
        }

        System.out.print("Enter your pasword: ");
        password = input.nextLine();

        System.out.print("Enter your address: ");
        address = input.nextLine();

        Buyer tempBuyer = DB.createBuyerNode(DB, username, password, address);
        
        return tempBuyer;
    }

    public Boolean loginAdmin(){

        String password = "";
        Boolean authenticated = false;

        System.out.println("Log into admin account | 0 to go back");
        System.out.println("-------------------------------------\n");
        
        System.out.print("Enter Password: ");


        while(password == ""){

            String inputPass = input.nextLine();

            switch(inputPass){
                case "0":
                    password = inputPass;
                    break;
                
                //Admin Password
                case "pass123":
                    password = inputPass;
                    authenticated = true;
                    break;

                default:
                System.out.print("Wrong password try again: ");
                break;
            }

        }

        Utils.clearConsole();

        return authenticated;

    }

    public Buyer loginBuyer(){

        String username = "";
        String password = "";
        Buyer retBuyer = null;
        System.out.println("Log into buyer account | 0 to go back");
        System.out.println("-------------------------------------\n");
        


        while(password == ""){

            System.out.print("Enter username: ");
            username = input.nextLine();
    
            if(username.equals("0")) break;
            
    
            System.out.print("Enter Password: ");
            String inputPass = input.nextLine();

            switch(inputPass){
                case "0":
                    password = inputPass;
                    break;

                default:
                    password = inputPass;
                    if(DB.verifyBuyerPassword(username, password)){
                        retBuyer =  DB.findBuyer(DB, username);
                    } else {
                        password = "";
                        System.out.println("Wrong username or password try again!\n");
                    }
                break;
            }

        }

        Utils.clearConsole();

        return retBuyer;


    }

    public Farm loginFarm(){

        String username = "";
        String password = "";
        Farm retFarmer = null;
        System.out.println("Log into farm account | 0 to go back");
        System.out.println("-------------------------------------\n");
        


        while(password == ""){

            System.out.print("Enter username: ");
            username = input.nextLine();
    
            if(username.equals("0")) break;
            
    
            System.out.print("Enter Password: ");
            String inputPass = input.nextLine();

            switch(inputPass){
                case "0":
                    password = inputPass;
                    break;

                default:
                    password = inputPass;
                    if(DB.verifyFarmPassword(username, password)){
                        retFarmer =  DB.findFarm(DB, categories, username);
                    } else {
                        password = "";
                        System.out.println("Wrong username or password try again!\n");
                    }
                break;
            }

        }

        Utils.clearConsole();

        return retFarmer;


    }
}
