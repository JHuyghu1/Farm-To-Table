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

    public Buyer createNewUser(Database DB){

        System.out.println("Create a new account \n");


        String username = "";
        String password = "";
        String address = "";

        while(username == ""){
            System.out.println("Enter your new username");
            String tempName = input.nextLine();

            if(!DB.usernameExists(tempName)){
                username = tempName;
            } else {
                System.out.println("That username is already taken!\n");
            }
        }

        System.out.println("Enter your pasword");
        password = input.nextLine();

        System.out.println("Enter your address");
        address = input.nextLine();

        Buyer tempBuyer = DB.createBuyerNode(DB, username, password, address);
        
        return tempBuyer;
    }
}
