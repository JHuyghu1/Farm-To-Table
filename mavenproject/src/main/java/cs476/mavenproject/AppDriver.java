package cs476.mavenproject;

import java.util.ArrayList;
import java.util.Scanner;

import cs476.mavenproject.Categories.Category;

public class AppDriver {

    Database DB;
    Buyer mainBuyer = null;
    Farm mainFarm = null;
    Categories categories = new Categories();
    Login Login;
    HeadQ HeadQ;
    Scanner input;

    static String INVAL_SEL = " is an invalid selction!";

    public AppDriver(Database DB, Scanner input){
        this.DB = DB;
        this.input = input;
        Login = new Login(DB, input, categories);
        HeadQ = new HeadQ(DB, input, categories);
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
                    buyerMainMenu();
                } else {
                    login();
                }
                break;

            case "2":
                Utils.clearConsole();
                Farm farmAuth = Login.loginFarm();
                if(farmAuth != null){
                    mainFarm = farmAuth;
                    farmMainMenu();
                } else {
                    login();
                }
            break;
            
            case "3":
                Utils.clearConsole();
                Boolean adminAuth = Login.loginAdmin();
                if(adminAuth){
                    adminMainMenu();
                } else {
                    login();
                }
                break;

            case "4":
                Utils.clearConsole();
                mainBuyer = Login.createNewUser();
                Utils.clearConsole();
                buyerMainMenu();
                break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL + '\n');
                login();

        }


    }
    
    // ------- BUYER ACTIOINS ---------

    public void buyerMainMenu() {
        String selection;

        System.out.println("Main Menu for: " + mainBuyer.username());
        System.out.print("---------------");
        Utils.underlineString(mainBuyer.username());

        System.out.println("1 - Users");
        System.out.println("2 - Products");
        System.out.println("3 - Cart");
        System.out.println("4 - Purchase History");
        System.out.println("5 - Logout");


        
        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){

            // View Users Menu
            case "1":
                Utils.clearConsole();
                buyerUserMenu();;
                break;

            // View Poduct Menu
            case "2":
                Utils.clearConsole();
                buyerProductMenu();;
                break;
            
            // View Buyer Cart
            case "3":
                Utils.clearConsole();
                buyerCartMenu();
                break;

            // View Buyer Cart
            case "4":
                Utils.clearConsole();
                if(mainBuyer.purchaseHistory().isEmpty()){
                    System.out.println("You havn't made a purchase yet!");
                    System.out.println("-------------------------------\n");
                    buyerMainMenu();    
                } else {
                    buyerHistory();
                }
                break;

            // Logout
            case "5":
                Utils.clearConsole();
                mainBuyer = null;
                login();
                break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL + '\n');
                buyerMainMenu();

        }
        
    }

    public void buyerProductMenu() {

        String selection = "";

        System.out.println("Product Menu");
        System.out.println("------------");
        System.out.println("1 - Custom Search");
        System.out.println("2 - Recomendations");
        System.out.println("3 - Back");


        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
                    Utils.clearConsole();
                    buyerProductMenu_Search();
            break;

            case "2":
            break;
        
            case "3":
                Utils.clearConsole();
                buyerMainMenu();
                break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL + '\n');
                buyerProductMenu();
                break;
        }

    }

        // ------- PRODUCT MENU ---------

        // Search Product
        public void buyerProductMenu_Search() {

            String selection = "";
            Category category = null;
            SubCategory subCategory = null;

            System.out.println("Choose a category | 0 - Back");
            System.out.println("----------------------------");
            System.out.println("1. Vegetables");
            System.out.println("2. Fruits");
            System.out.println("3. Herbs\n");


            //Category Selection
            while(selection != "valid"){

                System.out.print("\nYour selection: ");
                selection = input.nextLine();

                switch(selection){

                    case"0":
                        Utils.clearConsole();                    
                        buyerProductMenu();
                        break;

                    case"1":
                        category = categories.vegetables;
                        selection = "valid";
                        break;

                    case"2":
                        category = categories.fruits;
                        selection = "valid";
                        break;

                    case"3":
                        category = categories.herbs;
                        selection = "valid";
                        break;

                    default:
                        System.out.println("\n" + selection + " is an invalid selection!");
                        break;

                }
            } // End - Category selction

            Utils.clearConsole();

            System.out.println("Choose a subcategory - " + category.name());
            System.out.print("-----------------------");
            Utils.underlineString(category.name());
            
            category.viewSubCategories();

            selection = "";

            //SubCategory Selection
            while(selection != "valid"){
                System.out.print("\nYour selection: ");
                selection = input.nextLine();

                try{
                    int selctionInt = Integer.parseInt(selection);

                    int numSubcategories = category.table.size();

                    if( selctionInt < 0 || selctionInt > numSubcategories){
                        System.out.println("\n" + selection + " is an invalid selection!");
                    } else {
                        subCategory = category.get(selctionInt);
                        selection = "valid";
                    }
                    
                }catch(Exception e){

                    System.out.println("\n" + selection + " is an invalid selection!");
                }
        
            }

            Utils.clearConsole();
            
            Boolean productFound = false;

            //Used to add products to cart
            ArrayList<Product> allProductsFound = new ArrayList<Product>();

            //Search For Products
            for(Farm farm : HeadQ.farms()){
                ArrayList<Product> foundProducts = DB.customProductSearch(DB, categories, farm.name(), subCategory);
                if(!foundProducts.isEmpty()){
                    productFound = true;
                    System.out.println("Farm: " + farm.name());
                    System.out.print("------");
                    Utils.underlineString(farm.name());
                    
                    for(Product p: foundProducts){
                        allProductsFound.add(p);
                        System.out.println(p.toString(false, mainBuyer.cart.contains(p.identity())));
                    }
                }
            }

            //If no products found start a new search
            if(!productFound){
                Utils.clearConsole();
                System.out.println("No product found try again!\n");
                buyerProductMenu_Search();

            } else {
                search_productOptions(subCategory);
            }

        }

        public void search_productOptions(SubCategory subCategory){

            String selection = "";

            System.out.println("\n---------------------------------------------------");
            System.out.println("1 - Add product to cart | 2 - New Search | 3 - Home");
            System.out.println("---------------------------------------------------");

            while(selection != "valid"){

                System.out.print("Your selectioin: ");
                selection = input.nextLine();

                switch(selection){
                    case "1":
                        Utils.clearConsole();
                        selection = "valid";
                        productOptions_addToCart(subCategory);                    
                        break;
                    
                    case "2":
                        Utils.clearConsole();
                        selection = "valid";
                        buyerProductMenu();
                        break;
                    
                    case "3":
                        Utils.clearConsole();
                        selection = "valid";
                        buyerMainMenu();
                        break;


                    default:
                    System.out.println(selection + INVAL_SEL);
                    break;
                }

            }

        }

        public void productOptions_addToCart(SubCategory subCategory){
            
            //Used to add products to cart
            ArrayList<Product> allProductsFound = new ArrayList<Product>();

            //Search For Products
            for(Farm farm : HeadQ.farms()){
                ArrayList<Product> foundProducts = DB.customProductSearch(DB, categories, farm.name(), subCategory);
                if(!foundProducts.isEmpty()){
                    System.out.println("Farm: " + farm.name());
                    System.out.print("------");
                    Utils.underlineString(farm.name());
                    
                    for(Product p: foundProducts){
                        allProductsFound.add(p);
                        System.out.println(p.toString(false, mainBuyer.cart.contains(p.identity())));
                    }
                }
            }

            String capacityString = mainBuyer.cart.currentWeight() + "/" + Constants.WEIGHT_LIMIT + " grams";
            
            System.out.print("\n------------------------------------");
            Utils.underlineString(capacityString);
            System.out.println("Select the product | " + "Cart Capacity: " + capacityString);
            System.out.print("------------------------------------");
            Utils.underlineString(capacityString);
            

            String selection = "";

            int quantity = 0;
            Product selectedProduct = null;
            int maxQuantity = 0;

            while(selection != "valid"){

                System.out.print("Product ID: ");
                selection = input.nextLine();

                try{
                    int productId = Integer.parseInt(selection);
                    if(!allProductsFound.stream().anyMatch(p -> productId == p.identity())) throw new Exception("Product not in search results!");
                    selectedProduct = allProductsFound.stream().filter(p -> p.identity() == productId).findFirst().orElse(null);
                    
                    maxQuantity = mainBuyer.cart.maxQuantity(selectedProduct);

                    if(maxQuantity < 1) throw new Exception("You don't have enough room for that product!");

                    selection = "valid";
                }catch(NumberFormatException e){
                    System.out.println("Enter an integer!");
                    
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

            }

            selection="";

            while(selection != "valid"){
                System.out.print("Quantity: ");
                selection = input.nextLine();

                if(selectedProduct != null)
                    try{

                        int tempQuantity = Integer.parseInt(selection);
                        if(tempQuantity < 1) throw new Exception("Enter a quantity greater then 0!");
                        if(tempQuantity < 1 || tempQuantity > selectedProduct.quantity()) throw new Exception("Not enough quantity!");
                        if(tempQuantity > maxQuantity) throw new Exception("You only have enough room for " + maxQuantity + "!");

                        quantity = tempQuantity;

                        selection = "valid";
            
                    }catch(NumberFormatException e){
                        System.out.println("Enter an integer!");

                    }catch(Exception e){

                        System.out.println(e.getMessage());
            
                    }
            }

            mainBuyer.cart.add(selectedProduct, quantity);

            Utils.clearConsole();

            if(quantity > 1){
                System.out.println("Products added to cart!\n");
            } else {
                System.out.println("Product added to cart!\n");
            }

            buyerMainMenu();

        }

        // ------- USER MENU ---------

        public void buyerUserMenu() {

            String selection = "";

            System.out.println("User Menu");
            System.out.println("---------");
            System.out.println("1 - Custom Search");
            System.out.println("2 - Recomendations");
            System.out.println("3 - Following");
            System.out.println("4 - Followers");
            System.out.println("5 - Back");


            System.out.print("\nYour selection: ");
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
                    Utils.clearConsole();
                    buyerMainMenu();
                    break;

                default:
                    Utils.clearConsole();
                    System.out.println(selection + INVAL_SEL + '\n');
                    buyerUserMenu();
                    break;
            }

        
            
        }

        // ------- BUYER CART ---------

        public void buyerCartMenu() {
            
            String selection = "";

            String capacityString = mainBuyer.cart.currentWeight() + "/" + Constants.WEIGHT_LIMIT + " grams";
            String cartInfo = "Capacity: " + capacityString + " | Total Cost: $" + mainBuyer.cart.currentCost();

            System.out.println("Your Cart | " + cartInfo);
            System.out.print("------------");
            Utils.underlineString(cartInfo);
            System.out.println();



            mainBuyer.cart.viewCart(true);

            System.out.println("\n---------------------------------------");
            System.out.println("1 - Checkout | 2 - Edit Item | 3 - Back");
            System.out.println("---------------------------------------");


            System.out.print("\nYour Selection: ");
            selection = input.nextLine();

            switch(selection){
                case "1":
                Utils.clearConsole();
                if(mainBuyer.cart.isEmpty()){
                    System.out.println("No items to buy!");
                    System.out.println("-----------------\n");
                    buyerCartMenu();
                    
                } else {
                    int cartId = mainBuyer.cart.checkout(input, false);
                    Utils.clearConsole();
                    mainBuyer.cart = new Cart(DB, categories, mainBuyer.username());
                    mainBuyer.updatePurchaseHistory();
                    System.out.print("Your order number is " + cartId + "\n");
                    buyerMainMenu();
    
                }

                break;

                case "2":
                    Utils.clearConsole();
                    if(mainBuyer.cart.isEmpty()){
                        System.out.println("No items to edit!");
                        System.out.println("-----------------\n");

                        buyerCartMenu();

                    } else {
                        buyerCartMenu_Edit();    
                    }
                    break;
                
                case "3":
                    Utils.clearConsole();
                    buyerMainMenu();
                    break;

                default:
                    Utils.clearConsole();
                    System.out.println(selection + INVAL_SEL);
                    buyerCartMenu();
                    break;
            }
            
        }
    
        public void buyerCartMenu_Edit(){

            String selection = "";
            Boolean valid = false;

            int productId = 0;

            System.out.println("Your Cart");
            System.out.print("---------\n");

            mainBuyer.cart.viewCart(true);

            System.out.println("\n----------------------");
            System.out.println("Choose an item to edit");
            System.out.println("----------------------");

            while(!valid){
                System.out.print("Item ID: ");
                selection = input.nextLine();

                try{

                    productId = Integer.parseInt(selection);
                    if(mainBuyer.cart.contains(productId) < 1) throw new Exception("Product isn't in your cart!");
                    valid = true;

                }catch(NumberFormatException e){
                    System.out.println("Enter an integer!");

                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }

            Utils.clearConsole();

            Product selectedProduct = DB.findProduct(categories, DB, productId);

            int poductId = selectedProduct.identity();
            int cartQuantity = mainBuyer.cart.contains(poductId);
            int productQuantity = selectedProduct.quantity() - cartQuantity;
            int maxQuantity = mainBuyer.cart.maxQuantity(selectedProduct);


            System.out.println("Chosen Product");
            System.out.print("--------------\n");

            mainBuyer.cart.printProduct(productId);

            System.out.println("\n--------------------------------------------------------");
            System.out.println("1 - Increase Quantity | 2 - Decrease Quantity | 3 - Back");
            System.out.println("--------------------------------------------------------\n");

            valid = false;

            while(!valid){
                System.out.print("Your Selection: ");
                selection = input.nextLine();

                switch(selection){
                    case"1":
                    if(productQuantity - cartQuantity < 1){
                        System.out.println("No product available to to add");

                    } else if(maxQuantity == 0){
                        System.out.println("Not enough capacity left");

                    }else {
                        Utils.clearConsole();
                        buyerCartMenu_Edit_Increase(selectedProduct);
                        valid = true;
                    }
                    break;

                    case"2":
                        Utils.clearConsole();
                        buyerCartMenu_Edit_Decrease(selectedProduct);
                        break;

                    case"3":
                        Utils.clearConsole();
                        buyerCartMenu_Edit();
                        break;

                    default:
                        System.out.println(selection + INVAL_SEL);
                        break;


                }

            }

        }
        
        public void buyerCartMenu_Edit_Increase(Product product){

            boolean valid = false;
            String selection = "";
            int increaseBy = 0;

            int poductId = product.identity();
            int cartQuantity = mainBuyer.cart.contains(poductId);
            int productQuantity = product.quantity() - cartQuantity;
            int maxQuantity = mainBuyer.cart.maxQuantity(product);

            System.out.println("\nIn your Cart: " + cartQuantity);
            System.out.print("--------------");
            Utils.underlineString(Integer.toString(cartQuantity));

            System.out.println("\nAvailable: " + productQuantity);
            System.out.print("-----------");
            Utils.underlineString(Integer.toString(productQuantity));

            while(!valid){
                System.out.print("\nIncrease quantity by: ");
                try{
                    selection = input.nextLine();
                    increaseBy = Integer.parseInt(selection);
                    if(increaseBy < 1) throw new Exception("Enter a number greater then 0!");
                    if(increaseBy > maxQuantity) throw new Exception("You can only add up to " + maxQuantity + " more!");
                    if(increaseBy > productQuantity) throw new Exception("Not enough product available");

                    valid = true;

                }catch(NumberFormatException e){
                    System.out.println("Enter an integer!");

                }catch(Exception e){
                    System.out.println(e.getMessage());

                }
            }

            mainBuyer.cart.add(product, increaseBy);

            Utils.clearConsole();
            System.out.println("Increased Quantity! ID: " + poductId + " +" + increaseBy + "\n");
            buyerCartMenu();

        }
        
        public void buyerCartMenu_Edit_Decrease(Product product){

            boolean valid = false;
            String selection = "";
            int deacreaseBy = 0;


            int poductId = product.identity();
            int cartQuantity = mainBuyer.cart.contains(poductId);

            System.out.println("\nIn your Cart: " + cartQuantity);
            System.out.println("-------------");

            while(!valid){
                System.out.print("\nDeacrease quantity by: ");
                try{
                    selection = input.nextLine();
                    deacreaseBy = Integer.parseInt(selection);
                    if(deacreaseBy < 1) throw new Exception("Enter a number greater then 0!");
                    if(deacreaseBy > cartQuantity) deacreaseBy = cartQuantity;
                    valid = true;

                }catch(NumberFormatException e){
                    System.out.println("Enter an integer!");

                }catch(Exception e){
                    System.out.println(e.getMessage());

                }
            }

            mainBuyer.cart.remove(product, deacreaseBy);

            Utils.clearConsole();

            if(deacreaseBy == cartQuantity){
                System.out.println("Removed! ID: " + poductId + '\n');
            } else {
                System.out.println("Decreased Quantity! ID:" + poductId + " -" + deacreaseBy + '\n');
            }

            buyerCartMenu();

        }

        // ------- PURCHASE HISTORY ---------

        public void buyerHistory(){

            System.out.println("Purchase History | Press ENTER to go back");
            System.out.print("------------------------------------------\n");

            mainBuyer.viewPurchaseHistory();
            input.nextLine();

            Utils.clearConsole();
            buyerMainMenu();

        }
            
    // ------- FARM ACTIOINS ---------
    public void farmMainMenu(){

        String selection;

        System.out.println("Farm Menu - " + mainFarm.name());
        System.out.println("----------");
        System.out.println("1 - Inventory");
        System.out.println("2 - Sales");
        System.out.println("3 - Recommendations");
        System.out.println("4 - Logout");

        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
                Utils.clearConsole();
                farmMainMenu_Inventory();
                break;

            case "2":
                Utils.clearConsole();
                break;

            case "3":
                Utils.clearConsole();
                break;
            
            case "4":
                Utils.clearConsole();
                login();
                break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL);
                farmMainMenu();

        }

    }
    
    public void farmMainMenu_Inventory(){
        String selection = "";

        System.out.println("Your Inventory | 1 - Back");
        System.out.println("-------------------------");

        mainFarm.viewInventory(false);


        while(selection != "valid"){

            System.out.print("\nYour selection: ");
            selection = input.nextLine();
    
            switch(selection){
                case "1":
                    Utils.clearConsole();
                    farmMainMenu();
                    selection = "valid";
                    break;

                default:
                    System.out.println(selection + INVAL_SEL);
            }
        }
        

    }
    
    // ------- ADMIN ACTIONS ---------
    public void adminMainMenu() {
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
            adminMainMenu_Inventory();
            break;
            
            case "3":
            Utils.clearConsole();
            login();
            break;

            default:
            Utils.clearConsole();
            System.out.println(selection + INVAL_SEL);
            adminMainMenu();

        }
        
    }

    public void adminMainMenu_Inventory() {
        String selection;

        System.out.println("Head Quarters Inventory");
        System.out.println("-----------------------");
        HeadQ.viewInventory();

        System.out.println("\n-------------------------------------------------------------------");
        System.out.println("1 - Add Farm | 2 - Add New Product | 3 - Restock Product  | 4 - Back");
        System.out.println("--------------------------------------------------------------------");

        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
                Utils.clearConsole();
                HeadQ.createNewFarm();
                adminMainMenu_Inventory();
                break;

            case "2":
                Utils.clearConsole();
                HeadQ.createProduct();
                adminMainMenu_Inventory();
                break;
            
            case "3":
                Utils.clearConsole();
                HeadQ.viewInventory();
                HeadQ.restockProduct();
                adminMainMenu_Inventory();
                break;

            case "4":
                Utils.clearConsole();
                adminMainMenu();
                break;

            default:
                Utils.clearConsole();
                System.out.println(selection + INVAL_SEL);
                adminMainMenu_Inventory();

        }
        
    }

}
