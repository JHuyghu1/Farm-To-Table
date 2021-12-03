package cs476.mavenproject;

import java.util.Scanner;

public class Menus {

    Database DB;
    Buyer mainBuyer = null;
    Farm mainFarm = null;
    Categories categories = new Categories();
    Login Login;
    HeadQ HeadQ;
    Scanner input;

    static String INVAL_SEL = " is an invalid selction!";

    public Menus(Database DB, Scanner input){
        this.DB = DB;
        this.input = input;
        Login = new Login(DB, input, mainFarm);
        HeadQ = new HeadQ(DB, input, categories);
    }

    public Categories getCategories(){
        return categories;
    }
        
    public void login() {

        
        String selection;

        System.out.println("Welcome to Farm to Table");
        System.out.println("------------------------");
        System.out.println("1 - Login as User");
        System.out.println("2 - Login as Farm");
        System.out.println("3 - Login as Admin");
        System.out.println("4 - Create a new account");

        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
                Utils.clearConsole();
                Buyer buyerAuth = Login.loginBuyer();
                if(buyerAuth != null){
                    mainBuyer = buyerAuth;
                    buyerMain();
                } else {
                    login();
                }
                break;

            case "2":
                Utils.clearConsole();
                //mainFarm = Login.loginFarm(DB);
                Utils.clearConsole();
                buyerMain();
                break;
            
            case "3":
                Utils.clearConsole();
                Boolean adminAuth = Login.loginAdmin();
                if(adminAuth){
                    adminMain();
                } else {
                    login();
                }
                break;

            case "4":
                Utils.clearConsole();
                mainBuyer = Login.createNewUser();
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
            Utils.clearConsole();
            buyerCart();
            break;

            case "5":
            Utils.clearConsole();
            mainBuyer = null;
            login();
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

        System.out.println("Cart for: " + mainBuyer.username());
        System.out.print("----------");
        for(int i = 0; i < mainBuyer.username().length(); i++){
            System.out.print('-');
        }


        System.out.print("\n1 - Checkout");
        System.out.print(" | 2 - Edit Item");
        System.out.println(" | 3 - Back");
        System.out.println("---------------------------------------");

        mainBuyer.cart.viewProducts();

        selection = input.nextLine();

        switch(selection){
            case "1":
            break;

            case "2":
            break;
            
            case "3":
            Utils.clearConsole();
            buyerMain();
            break;

            default:
            Utils.clearConsole();
            System.out.println(selection + INVAL_SEL);
            buyerCart();

        }
        
    }

    public void adminMain() {
        String selection;

        System.out.println("Admin Menu");
        System.out.println("----------");
        System.out.println("1 - Live Orders");
        System.out.println("2 - Inventory");
        System.out.println("3 - Logout");

        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
            break;

            case "2":
            Utils.clearConsole();
            adminInventory();
            break;
            
            case "3":
            Utils.clearConsole();
            login();
            break;

            default:
            Utils.clearConsole();
            System.out.println(selection + INVAL_SEL);
            adminMain();

        }
        
    }

    public void adminInventory() {
        String selection;

        System.out.println("Head Quarters Inventory");
        System.out.println("-----------------------");
        HeadQ.viewInventory();

        System.out.println("\n-------------------------------------------------------------------");
        System.out.print("1 - Add Farm");
        System.out.print(" | 2 - Add New Product");
        System.out.print(" | 3 - Restock Product");
        System.out.println(" | 4 - Back");
        System.out.println("-------------------------------------------------------------------");

        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
                Utils.clearConsole();
                HeadQ.createNewFarm();
                adminInventory();
                break;

            case "2":
                Utils.clearConsole();
                HeadQ.createProduct();
                adminInventory();
                break;
            
            case "3":
                Utils.clearConsole();
                HeadQ.viewInventory();
                HeadQ.restockProduct();
                adminInventory();
                break;

            case "4":
                Utils.clearConsole();
                adminMain();
                break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL);
                adminInventory();

        }
        
    }

}
