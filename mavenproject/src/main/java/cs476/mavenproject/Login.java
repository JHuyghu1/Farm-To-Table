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

            if(!DB.buyerUsernameExists(tempName)){
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

    public Buyer loginBuyer(Database DB){
        System.out.println("Login into your account \n");
        String username = "";
        String password = "";

        while(username == ""){
            System.out.println("Enter your username");
            String tempName = input.nextLine();
            //username = tempName;
            //String empty = "";

            if(DB.buyerUsernameExists(tempName)){
                username = tempName;}
            else{System.out.println("Username incorrect. No account found for that username.");}
        }

        System.out.println("Enter your password");
        password = input.nextLine();

        String tempPass = DB.verifyBuyerPassword(username);
        System.out.println("correct password:" + tempPass);

        if(password.equals(tempPass)){
            System.out.println("Login successful!");}
        else{ System.out.println("Password Incorrect");}
    
        Buyer tempBuyer = DB.findBuyer(DB, username);
        return tempBuyer;
    }

    public Farm loginFarm(Database DB){
        System.out.println("Login into your account \n");
        String username = "";
        String password = "";

        while(username == ""){
            System.out.println("Enter your username");
            String tempName = input.nextLine();

            if(DB.farmUsernameExists(tempName)){
                username = tempName;
            }else{
                System.out.println("Username incorrect. No account found for that username.");
            }
        }
        System.out.println("Enter your password");
        password = input.nextLine();

        String tempPass = DB.verifyFarmPassword(username);

        if(password.equals(tempPass)){
            System.out.println("Login successful!");}
        else{ System.out.println("Password Incorrect");}
        
        Farm tempFarm = DB.findFarm(DB, username);
        return tempFarm;
    }
}
