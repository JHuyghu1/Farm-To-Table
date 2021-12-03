package cs476.mavenproject;

import java.util.Scanner;

public class Login {

    Database DB;
    Buyer buyer;
    Farm farm;
    Scanner input;

    public Login(Database DB, Scanner input, Buyer buyer, Farm farm){

        this.DB = DB;
        this.input = input;
        this.buyer = buyer;
        this.farm = farm;

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

        System.out.println("Log into admin account | 0 to go back");
        System.out.println("-------------------------------------\n");
        
        System.out.print("Enter Password: ");

        Boolean authenticated = false;
        String password = "";

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
}
