package cs476.mavenproject;

import java.util.Scanner;

public class Menus {

    Database DB;
    Buyer mainBuyer = new Buyer();
    Farm mainFarm = new Farm();
    Login Login;
    Scanner input;

    static String INVAL_SEL = " is an invalid selction!";

    public Menus(Database DB, Scanner input){
        this.DB = DB;
        this.input = input;
        Login = new Login(DB, input, mainBuyer, mainFarm);
    }
        
    public void login() {

        
        String selection;

        System.out.println("Welcome to Farm to Table");
        System.out.println("-------------------------\n");
        System.out.println("1 - Login as User");
        System.out.println("2 - Login as Farm");
        System.out.println("3 - Login as Admin");
        System.out.println("4 - Creat a new account");

        selection = input.nextLine();

        switch(selection){
            case "1":
            break;

            case "2":
            break;
            
            case "3":
            break;

            case "4":
            Utils.clearConsole();
            mainBuyer = Login.createNewUser(DB);
            Utils.clearConsole();
            buyerMain();
            break;

            default:
            Utils.clearConsole();
            System.out.println(selection + INVAL_SEL);
            login();

        }


    }
    
    public void buyerMain() {
        String selection;

        System.out.println("Main Menu for: " + mainBuyer.username());
        System.out.print("---------------");
        for(int i = 0; i < mainBuyer.username().length(); i++){
            System.out.print('-');
        }
        System.out.println("\n1 - Search Users");
        System.out.println("2 - Search Products");
        System.out.println("3 - View following");
        System.out.println("4 - View Cart");
        System.out.println("5 - Logout");

        selection = input.nextLine();

        switch(selection){
            case "1":
            break;

            case "2":
            break;
            
            case "3":
            break;

            case "4":
            break;

            case "5":
            break;

            default:
            Utils.clearConsole();
            System.out.println(selection + INVAL_SEL);
            login();

        }
        
    }

    public void buyerSearch(String type) {

        String selection;

        System.out.println("1 - Cusom Search");
        System.out.println("2 - Recomendations");
        System.out.println("3 - Back");

        selection = input.nextLine();

        switch(selection){
            case "1":
                if(type == "user"){

                } else if (type == "product"){

                }
            break;

            case "2":
                if(type == "user"){

                } else if (type == "product"){

                }
            break;
            
            case "3":
                if(type == "user"){

                } else if (type == "product"){

                }
            break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL);
                login();
        }
        
    }

    public void buyerCart() {
        String selection;

        System.out.println("1 - Checkout");
        System.out.print("| 2 - Edit Item");
        System.out.println("| 3 - Back");

        selection = input.nextLine();

        switch(selection){
            case "1":
            break;

            case "2":
            break;
            
            case "3":
            break;

            default:
            Utils.clearConsole();
            System.out.println(selection + INVAL_SEL);
            buyerCart();

        }
        
    }

}
