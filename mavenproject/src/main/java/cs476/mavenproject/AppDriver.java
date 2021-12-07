package cs476.mavenproject;

import java.util.ArrayList;
import java.util.HashMap;
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

    private enum ListType {
        FOLLOWING,
        FOLLOWERS,
        SEARCH,
        SIMILAR,
    }
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
                    farm_mainMenu();
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
                Utils.invalidSelection(selection);
                login();

        }


    }
    
    // ------- BUYER ---------

    private void buyerMainMenu() {
        String selection;

        String title = "Main Menu for: " + mainBuyer.username();
        Utils.underlineString(title);

        System.out.println("1 - Users");
        System.out.println("2 - Products");
        System.out.println("3 - Cart");
        System.out.println("4 - Purchase History");
        System.out.println("5 - Logout");


        
        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        Utils.clearConsole();

        switch(selection){

            // View Users Menu
            case "1":
                buyerUserMenu();;
                break;

            // View Poduct Menu
            case "2":
                buyerProductMenu();;
                break;
            
            // View Buyer Cart
            case "3":
                buyerCartMenu();
                break;

            // View Buyer Cart
            case "4":
                mainBuyer.updatePurchaseHistory();
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
                mainBuyer = null;
                login();
                break;

            default:
                Utils.invalidSelection(selection);
                buyerMainMenu();

        }
        
    }

        // ------- PRODUCT ACTIONS MENU ---------

            private void buyerProductMenu() {

                String selection = "";
        
                System.out.println("Product Menu");
                System.out.println("------------");
                System.out.println("1 - Search");
                System.out.println("2 - Food For Thought");
                System.out.println("3 - Back");
        
        
                System.out.print("\nYour selection: ");
                selection = input.nextLine();

                Utils.clearConsole();

                switch(selection){
                    case "1":
                            buyerProductMenu_Search();
                    break;
        
                    case "2":
                        buyer_product_reccomendations();
                        break;
                
                    case "3":
                        buyerMainMenu();
                        break;
        
                    default:
                        Utils.invalidSelection(selection);
                        buyerProductMenu();
                        break;
                }
        
            }

            // Search
            private void buyerProductMenu_Search() {

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

                String chooseSub = "Choose a subcategory - " + category.name();
                Utils.underlineString(chooseSub);
                
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
                
                ArrayList<Product> productList = DB.customProductSearch(DB, categories, subCategory);


                boolean productsFound = Utils.printProductsByFarm(DB, productList, false, mainBuyer);

                //If no products found start a new search
                if(!productsFound){
                    Utils.clearConsole();
                    System.out.println("No product found try again!\n");
                    buyerProductMenu_Search();

                } else {
                    search_productOptions(productList);
                }

            }

            private void search_productOptions(ArrayList<Product> foundProducts){

                HashMap<Integer,Product> allProductsOnPage = new HashMap<Integer,Product>();

                for(Product p: foundProducts){
                    allProductsOnPage.put(p.identity(),p);
                }

                String selection = "";

                System.out.println("---------------------------------------------------");
                System.out.println("1 - Add product to cart | 2 - New Search | 3 - Home");
                System.out.println("---------------------------------------------------");

                while(selection != "valid"){

                    System.out.print("Your selectioin: ");
                    selection = input.nextLine();

                    switch(selection){
                        case "1":
                            Utils.clearConsole();
                            selection = "valid";
                            productOptions_addToCart(foundProducts, false);                    
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
                        //TODO: Check invalid
                        Utils.invalidSelection(selection);
                        break;
                    }

                }

            }

            private void productOptions_addToCart(ArrayList<Product> foundProducts, boolean isRec){


                Utils.printProductsByFarm(DB, foundProducts, false, mainBuyer);
                
                String capacityString = mainBuyer.cart.currentWeight() + "/" + Constants.WEIGHT_LIMIT + " grams";
                
                String selectionOptions = "Select the product | " + "Cart Capacity: " + capacityString;
                Utils.surroundString(selectionOptions);
                
                String selection = "";
                boolean validSelction = false;

                int quantity = 0;
                Product selectedProduct = null;
                int maxQuantity = 0;


                System.out.print("Product ID: ");
                selection = input.nextLine();

                try{
                    int productId = Integer.parseInt(selection);
                    if(!foundProducts.stream().anyMatch(p -> productId == p.identity())) throw new Exception("Product not in search results!");
                    selectedProduct = foundProducts.stream().filter(p -> p.identity() == productId).findFirst().orElse(null);
                    if(selectedProduct.quantity() == 0) throw new Exception("We're all out!");
                    maxQuantity = mainBuyer.cart.maxQuantity(selectedProduct);

                    if(maxQuantity < 1) throw new Exception("You don't have enough room for that product!");
                    validSelction = true;

                }catch(NumberFormatException e){
                    Utils.clearConsole();
                    Utils.printError("Enter an integer!");
                    productOptions_addToCart(foundProducts, isRec);
                    
                }catch(Exception e){
                    Utils.clearConsole();
                    Utils.printError(e.getMessage());
                    productOptions_addToCart(foundProducts, isRec);
                }

                

                validSelction = false;


                while(!validSelction){
                    System.out.print("Quantity: ");
                    selection = input.nextLine();

                    if(selectedProduct != null)
                        try{

                            int tempQuantity = Integer.parseInt(selection);
                            if(tempQuantity < 1) throw new Exception("Enter a quantity greater then 0!");
                            if(tempQuantity < 1 || tempQuantity > selectedProduct.quantity()) throw new Exception("Not enough quantity!");
                            if(tempQuantity > maxQuantity) throw new Exception("You only have enough room for " + maxQuantity + "!");

                            quantity = tempQuantity;

                            validSelction = true;
                
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

                if(isRec){
                    buyer_product_reccomendations();
                } else {
                    buyerMainMenu();
                }
            }

            private ArrayList<Product> print_buyer_product_recs(){

                String username = mainBuyer.username();
                ArrayList<Product> allProductsOnPage = new ArrayList<Product>();

                Utils.underlineString("Food For Thought | Press ENTER to go back");
        
                ArrayList<Product> favoritePurchases = DB.favoriteItems(DB, categories, username);
                allProductsOnPage.addAll(favoritePurchases);

                Utils.underlineString("Your Favorite Purchases");
        
                for(Product p: favoritePurchases){
                    System.out.println(p.salesString() + "\n--");
                }
        
                if( favoritePurchases.size() > 0){
                    System.out.println();
                    Utils.underlineString("Product Recommendations");
                    ArrayList<Product> recs = DB.productRecommendations(DB, categories, favoritePurchases.get(0));
                    for(Product p: recs){
                        System.out.println("ID:"+p.identity() + " " + p.subCategory().name() + p.name() + "\n--");
                    }

                }
                return allProductsOnPage;
            }

            private void buyer_product_reccomendations(){

        
                print_buyer_product_recs();
    
                input.nextLine();

                Utils.clearConsole();
                buyerMainMenu();   

            }
        
            // ------- USER ACTIONS MENU ---------

        private void buyerUserMenu() {

            String selection = "";

            System.out.println("User Menu");
            System.out.println("---------");
            System.out.println("1 - Search");
            System.out.println("2 - Recomendations");
            System.out.println("3 - Following");
            System.out.println("4 - Followers");
            System.out.println("5 - Back");


            System.out.print("\nYour selection: ");
            selection = input.nextLine();

            switch(selection){
                case "1":
                    Utils.clearConsole();
                    searchUserEntry();
                    break;

                case "2":
                    Utils.clearConsole();
                    viewSimilarUsers();
                    break;

                case "3":
                    Utils.clearConsole();
                    viewRelatedUsers(true);
                    break;

                case "4":
                    Utils.clearConsole();
                    viewRelatedUsers(false);
                    break;
            
                case "5":
                    Utils.clearConsole();
                    buyerMainMenu();
                    break;

                default:
                    Utils.clearConsole();
                    Utils.invalidSelection(selection);
                    buyerUserMenu();
                    break;
            }

        
            
        }

            //Search
            private void searchUserEntry() {

                String targetUsername = "";
                ArrayList<Buyer> foundUsers = null;

                System.out.println("Search for users");
                System.out.println("----------------");

                System.out.print("\nEnter a username: ");
                targetUsername = input.nextLine();

                foundUsers = DB.findBuyersByUsername(DB, categories, mainBuyer.username(), targetUsername);

                Utils.clearConsole();

                if(foundUsers.isEmpty()){
                    Utils.printError("No Users Found!");
                    buyerUserMenu();

                } else {

                    Boolean smallResult = foundUsers.size() <= Constants.INDEX_LIMIT;

                    if(smallResult)
                        userViewSmall(foundUsers, ListType.SEARCH);
                    else
                        userViewLarge(foundUsers, 0, ListType.SEARCH);

                }
                
            }
            
            private void viewRelatedUsers(boolean following){

                ArrayList<Buyer> foundUsers = null;
                String username = mainBuyer.username();

                foundUsers = following
                                ? DB.getFollowing(DB, categories, username)
                                : DB.getFollowers(DB, categories, username);

                ListType type = following
                                ? ListType.FOLLOWING
                                : ListType.FOLLOWERS;

                if(foundUsers.isEmpty()){

                        if(following) System.out.println("You aren't following anyone!\n");
                        else System.out.println("You have no followers!\n");
                        buyerUserMenu();
    
                } else {

                    Boolean smallResult = foundUsers.size() <= Constants.INDEX_LIMIT;

                    if(smallResult) userViewSmall(foundUsers, type); else userViewLarge(foundUsers, 0, type);

                }
                    
                
            }

            private void viewSimilarUsers(){

                String username = mainBuyer.username();
                ArrayList<Buyer> foundUsers = DB.recommendNewFollowers(username);

                ListType type = ListType.SIMILAR;

                if(foundUsers.isEmpty()){

                        Utils.printError("No recommendations, follow more people!");
                        buyerUserMenu();
    
                } else {

                    Boolean smallResult = foundUsers.size() <= Constants.INDEX_LIMIT;

                    if(smallResult) userViewSmall(foundUsers, type); else userViewLarge(foundUsers, 0, type);

                }
                    
                
            }

            private void userViewSmall(ArrayList<Buyer> allUsers, ListType type){

                String selection = "";
                Boolean validSelection = false;

                Utils.printFoundUsersTitle(allUsers);

                    int index = 1;

                    for(Buyer foundUser: allUsers){
                        Utils.printFoundUser(foundUser, index++);
                    }
                    
                    System.out.println();

                    String userOptions = "1 - Select User | 2 - Search | 3 - Home";
                    Utils.surroundString(userOptions);

                    while(!validSelection){

                        System.out.print("Your selection: ");
                        selection = input.nextLine();

                        switch(selection){
                            case "1":
                                validSelection = true;
                                selectUserFromList(allUsers, 0, Constants.INDEX_LIMIT-1, type);
                                break;

                            case "2":
                                validSelection = true;
                                Utils.clearConsole();
                                searchUserEntry();
                                break;

                            // Home
                            case "3":
                                validSelection = true;
                                Utils.clearConsole();
                                buyerMainMenu();
                                break;

                            default:
                            //TODO: check invalid
                            Utils.invalidSelection(selection);
                            break;

                        }

                    }

            }
        
            private void userViewLarge(ArrayList<Buyer> allUsers, int StartIndex, ListType type){

                Utils.printFoundUsersTitle(allUsers);

                int indexLimit = Constants.INDEX_LIMIT;
                int endIndex = StartIndex;

                for(int i = StartIndex; i < (StartIndex+indexLimit) && i < allUsers.size(); i++){
                    Utils.printFoundUser(allUsers.get(i), i+1);
                    endIndex++;
                }
                    
                indexUserViewMenu(allUsers, StartIndex, endIndex, type);


            }

            private void indexUserViewMenu(ArrayList<Buyer> allUsers, int startIndex, int endIndex, ListType type){
                String menu = null;

                String selection = "";
                Boolean validSelection = false;
                int indexLimit = Constants.INDEX_LIMIT;


                if(startIndex == 0){
                    menu = "1 - Next Page | 2 - Select User | 3 - Search | 4 - Home";
                } else if(endIndex+1 > allUsers.size()) {
                    menu = "1 - Previous Page | 2 - Select User | 3 - Search | 4 - Home";
                } else {
                    menu = "1 - Previous Page | 2 - Next Page | 3 - Select User | 4 -  Search | 5 - Home";
                }
                
                Utils.surroundString(menu);

                while(!validSelection){

                    System.out.print("Your selection: ");
                    selection = input.nextLine();

                    if(startIndex == 0){
                        switch(selection){
                            case "1":
                                validSelection = true;
                                Utils.clearConsole();
                                userViewLarge(allUsers, endIndex, type);
                                break;

                            case "2":
                                validSelection = true;
                                selectUserFromList(allUsers, startIndex, endIndex, type);
                                break;

                            // New Search
                            case "3":
                                validSelection = true;
                                Utils.clearConsole();
                                searchUserEntry();
                                break;

                            // Home
                            case "4":
                                validSelection = true;
                                Utils.clearConsole();
                                buyerMainMenu();
                                break;

                            default:
                            Utils.clearConsole();
                            Utils.invalidSelection(selection);
                            userViewLarge(allUsers, startIndex, type);
                            
                            break;

                        }

                    } else if(endIndex+1 > allUsers.size()) {
                        switch(selection){
                            case "1":
                                validSelection = true;
                                Utils.clearConsole();
                                userViewLarge(allUsers, startIndex-indexLimit, type);
                                break;

                            case "2":
                                validSelection = true;
                                selectUserFromList(allUsers, startIndex, endIndex, type);                            
                                break;

                            // New Search
                            case "3":
                                validSelection = true;
                                Utils.clearConsole();
                                searchUserEntry();
                                break;

                            // Home
                            case "4":
                                validSelection = true;
                                Utils.clearConsole();
                                buyerMainMenu();
                                break;

                            default:
                            Utils.clearConsole();
                            Utils.invalidSelection(selection);
                            userViewLarge(allUsers, startIndex, type);
                            break;

                        }

                    } else {
                        switch(selection){
                            case "1":
                                validSelection = true;
                                Utils.clearConsole();
                                userViewLarge(allUsers, startIndex-indexLimit, type);
                                break;

                            case "2":
                                validSelection = true;
                                Utils.clearConsole();
                                userViewLarge(allUsers, endIndex, type);
                                break;

                            case "3":
                                validSelection = true;
                                selectUserFromList(allUsers, startIndex, endIndex, type);
                                break;


                            // New Search
                            case "4":
                                validSelection = true;
                                Utils.clearConsole();
                                searchUserEntry();
                                break;

                            // Home
                            case "5":
                                validSelection = true;
                                Utils.clearConsole();
                                buyerMainMenu();
                                break;

                            default:
                                Utils.clearConsole();
                                Utils.invalidSelection(selection);
                                userViewLarge(allUsers, startIndex, type);
                                break;

                        }

                    }
                }

            }
            
            private void selectUserFromList(ArrayList<Buyer> allUsers, int startIndex, int endIndex, ListType type){

                String selection = "";
                Boolean validSelection = false;
                
                while(!validSelection){

                    System.out.print("Enter number next to user: ");
                    selection = input.nextLine();

                    try{

                        int selectedInt = Integer.parseInt(selection);

                        if(selectedInt > endIndex || selectedInt < startIndex+1 || selectedInt > allUsers.size()){
                            throw new Exception("Can't find number!");
                        }
                        Buyer selectedUser = allUsers.get(selectedInt-1);
                        Utils.clearConsole();
                        selectedUserAction(selectedUser, allUsers, startIndex, type);

                    }catch(NumberFormatException e){

                        Utils.clearConsole();
                        Utils.printError("Enter an integer!");
                        selectUserFromList(allUsers, startIndex, endIndex, type);

                    }catch(Exception e){

                        Utils.clearConsole();
                        Utils.printError(e.getMessage());
                        selectUserFromList(allUsers, startIndex, endIndex, type);
                    }


                }

            }
            
            private void selectedUserAction(Buyer selectedUser, ArrayList<Buyer> allUsers, int startIndex, ListType type){

                String following = selectedUser.isFollowing()
                                    ? "True"
                                    : "False";
                
                String title = "Selected User: " + selectedUser.username() + " | Following: " + following + '\n';
                
                String menu = selectedUser.isFollowing()
                                ? "1 - Unfollow User | 2 - Back | 3 - Home"
                                : "1 - Follow User | 2 - Back | 3 - Home";

                String selection = "";

                System.out.println(title);

                Utils.surroundString(menu);

                System.out.print("Your selection: ");
                selection = input.nextLine();

                if(selectedUser.isFollowing()){
                    switch(selection){

                        case"1":
                            DB.unfollowUser(mainBuyer.username(), selectedUser.username());
                            selectedUser.isFollowing(false);
                            Utils.clearConsole();
                            System.out.println("You unfollowed " + selectedUser.username() + "!\n");
                            
                            if(type == ListType.FOLLOWERS){
                                viewRelatedUsers(false);
                            }else if(type == ListType.FOLLOWING){
                                viewRelatedUsers(true);
                            } else if(type == ListType.SEARCH){
                                userViewLarge(allUsers, startIndex, type);
                            } else if (type == ListType.SIMILAR){
                                userViewLarge(allUsers, startIndex, type);
                            }
                            break;
        
                        case"2":
                            Utils.clearConsole();
                            if(type == ListType.FOLLOWERS){
                                viewRelatedUsers(false);
                            }else if(type == ListType.FOLLOWING){
                                viewRelatedUsers(true);
                            } else if(type == ListType.SEARCH){
                                userViewLarge(allUsers, startIndex, type);
                            } else if (type == ListType.SIMILAR){
                                userViewLarge(allUsers, startIndex, type);
                            }
                            break;
        
                        case"3":
                            Utils.clearConsole();
                            buyerMainMenu();
                            break;

                        default:
                            Utils.clearConsole();
                            Utils.invalidSelection(selection);
                            selectedUserAction(selectedUser, allUsers, startIndex, type);
                            break;
                    }
                    
                } else {

                    switch(selection){

                        case"1":
                            DB.followUser(mainBuyer.username(), selectedUser.username());
                            selectedUser.isFollowing(true);
                            Utils.clearConsole();
                            System.out.println("You followed " + selectedUser.username() + "!\n");

                            if(type == ListType.FOLLOWERS){
                                viewRelatedUsers(false);
                            }else if(type == ListType.FOLLOWING){
                                viewRelatedUsers(true);
                            } else if(type == ListType.SEARCH){
                                userViewLarge(allUsers, startIndex, type);
                            } else if (type == ListType.SIMILAR){
                                userViewLarge(allUsers, startIndex, type);
                            }
                            break;
        
                        case"2":
                            Utils.clearConsole();
                            userViewLarge(allUsers, startIndex, type);
                            break;
        
                        case"3":
                            Utils.clearConsole();
                            buyerMainMenu();
                            break;

                        default:
                            Utils.clearConsole();
                            Utils.invalidSelection(selection);
                            selectedUserAction(selectedUser, allUsers, startIndex, type);
                            break;
                        
        
                    }
        
                }

            


            }

        
        // ------- BUYER CART ---------

        private void buyerCartMenu() {
            
            String selection = "";

            String capacityString = mainBuyer.cart.currentWeight() + "/" + Constants.WEIGHT_LIMIT + " grams";
            String cartInfo = "Your Cart | Capacity: " + capacityString + " | Total Cost: $" + mainBuyer.cart.currentCost();

            Utils.underlineString(cartInfo);
            System.out.println();



            mainBuyer.cart.viewCart(true, false);

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
                    if(cartId != -1){
                        mainBuyer.cart = new Cart(DB, categories, mainBuyer.username());
                        mainBuyer.updatePurchaseHistory();
                        System.out.print("Your order number is " + cartId + "\n");
                    }
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
                    Utils.invalidSelection(selection);
                    buyerCartMenu();
                    break;
            }
            
        }
    
        private void buyerCartMenu_Edit(){

            String selection = "";
            Boolean valid = false;

            int productId = 0;

            System.out.println("Your Cart");
            System.out.print("---------\n");

            mainBuyer.cart.viewCart(true, false);

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

            Product selectedProduct = DB.findProduct(DB, categories, productId);

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
                        //TODO: Check invalid
                        Utils.invalidSelection(selection);
                        break;


                }

            }

        }
        
        private void buyerCartMenu_Edit_Increase(Product product){

            boolean valid = false;
            String selection = "";
            int increaseBy = 0;

            int poductId = product.identity();
            int cartQuantity = mainBuyer.cart.contains(poductId);
            int productQuantity = product.quantity() - cartQuantity;
            int maxQuantity = mainBuyer.cart.maxQuantity(product);

            String inCart = "In your Cart: " + cartQuantity;
            String available = "Available: " + productQuantity;

            Utils.underlineString(inCart);
            System.out.println();
            Utils.underlineString(available);

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
        
        private void buyerCartMenu_Edit_Decrease(Product product){

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

        private void buyerHistory(){

            Utils.underlineString("Purchase History | Press ENTER to go back");
            
            mainBuyer.viewPurchaseHistory();
            input.nextLine();

            Utils.clearConsole();
            buyerMainMenu();

        }
            
    // ------- FARM ---------
    private void farm_mainMenu(){

        String selection;

        System.out.println("Farm Menu - " + mainFarm.name());
        System.out.println("----------");
        System.out.println("1 - Inventory");
        System.out.println("2 - Sales");
        System.out.println("3 - Food For Thought");
        System.out.println("4 - Logout");

        System.out.print("\nYour selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
                Utils.clearConsole();
                farm_inventory();
                break;

            case "2":
                Utils.clearConsole();
                ArrayList<Product> sales = mainFarm.soldHistory();
                indexSalesView(sales, 0);
                break;

            case "3":
                Utils.clearConsole();
                farm_reccomendations();
                break;
            
            case "4":
                Utils.clearConsole();
                login();
                break;

            default:
                Utils.clearConsole();
                Utils.invalidSelection(selection);
                farm_mainMenu();

        }

    }
    
    private void farm_inventory(){
        String title = "Your Inventory | Press ENTER to go back";
        Utils.underlineString(title);

        mainFarm.viewInventory();

        input.nextLine();
        Utils.clearConsole();
        farm_mainMenu();
            
  
    }
    
    private void farm_reccomendations(){

        String username = mainFarm.name();
        String title = "Food For Thought | Press ENTER to go back";
        Utils.underlineString(title);

        ArrayList<Product> topSellers = DB.topFiveSellers(DB, categories, username);

        Utils.underlineString("Your Top Sellers");

        for(Product p: topSellers){
            System.out.println(p.salesString() + "\n--");
        }

        if( topSellers.size() > 0){
            System.out.println();
            Utils.underlineString("Product Recommendations");
            ArrayList<Product> recs = DB.productRecommendations(DB, categories, topSellers.get(0));
            for(Product p: recs){
                System.out.println(p.salesString() + "\n--");
            }

        }



        input.nextLine();
        Utils.clearConsole();
        farm_mainMenu();
    }

    private void indexSalesView(ArrayList<Product> products, int startIndex){
        int indexLimit = Constants.INDEX_LIMIT;

        String menu = null;
        String selection = "";
        int endIndex = startIndex;

        double totalRevenue = products.stream().mapToDouble(p-> p.price() * p.totalSold()).sum();
        String title = "Sales Portal | Total Revenue: $" + totalRevenue;
        Utils.underlineString(title);

        if(products.isEmpty()){

            System.out.println("You havn't made any salses yet!\n");

        } else {
            for(int i = startIndex; i < (startIndex+indexLimit) && i < products.size(); i++){
                System.out.println(products.get(i).salesString() + "\n--");
                endIndex++;
            }
        }

        

        if(products.size() < indexLimit){
            menu = "1 - Back";
            
        }else if(startIndex == 0){
            menu = "1 - Next Page | 2 - Back";
        } else if(endIndex > products.size()) {
            menu = "1 - Previous Page | 2 - Back";
        } else {
            menu = "1 - Previous Page | 2 - Next Page | 3 - Back";
        }
        
        Utils.surroundString(menu);


        System.out.print("Your selection: ");
        selection = input.nextLine();

        if(products.size() < indexLimit){

            switch(selection){
                case "1":
                    Utils.clearConsole();
                    farm_mainMenu();
                    break;

                default:
                    Utils.clearConsole();
                    Utils.invalidSelection(selection);
                    indexSalesView(products, startIndex);
                    break;

                }

        }else if(startIndex == 0){

            switch(selection){
                case "1":
                    Utils.clearConsole();
                    indexSalesView(products, startIndex);
                    break;

                case "2":
                    Utils.clearConsole();
                    farm_mainMenu();
                    break;

                default:
                Utils.clearConsole();
                Utils.invalidSelection(selection);
                indexSalesView(products, startIndex);
                break;

            }

        } else if(endIndex > products.size()) {
            switch(selection){
                case "1":
                    Utils.clearConsole();
                    indexSalesView(products, startIndex-indexLimit);
                    break;

                // Home
                case "4":
                    Utils.clearConsole();
                    farm_mainMenu();
                    break;

                default:
                    Utils.clearConsole();
                    Utils.invalidSelection(selection);
                    indexSalesView(products, startIndex);
                    break;

            }

        } else {

            switch(selection){
                case "1":
                    Utils.clearConsole();
                    indexSalesView(products, startIndex-indexLimit);
                    break;

                case "2":
                    Utils.clearConsole();
                    indexSalesView(products, endIndex);
                    break;

                // Home
                case "5":
                    Utils.clearConsole();
                    farm_mainMenu();
                    break;

                default:
                    Utils.clearConsole();
                    Utils.invalidSelection(selection);
                    indexSalesView(products, startIndex);

            }

        }


    
    }
   
    // ------- ADMIN ---------
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
            Utils.clearConsole();
            HeadQ.viewLiveOrders();
            adminMainMenu();
            break;

            case "2":
            Utils.clearConsole();
            adminMainMenu_Farms();
            break;
            
            case "3":
            Utils.clearConsole();
            login();
            break;

            default:
            Utils.clearConsole();
            Utils.invalidSelection(selection);
            adminMainMenu();

        }
        
    }

    private void adminMainMenu_Farms() {
        String selection;

        System.out.println("Head Quarters Inventory");
        System.out.println("-----------------------");

        boolean noFarms = HeadQ.viewFarms() < 1;

        System.out.println();

        String options = "1 - Select Farm | 2 - Add Farm | 3 - Back";
        Utils.surroundString(options);

        System.out.print("Your selection: ");
        selection = input.nextLine();

        switch(selection){
            case "1":
            Utils.clearConsole();

                if(noFarms){
                    adminMainMenu_Farms();
                } else {
                    Farm farm = HeadQ.selectFarm();
                    Utils.clearConsole();
                    adminMainMenu_Farm(farm);    
                }
                break;

            case "2":
                Utils.clearConsole();
                HeadQ.createNewFarm();
                adminMainMenu_Farms();
                break;
            
            case "3":
                Utils.clearConsole();
                adminMainMenu();
                break;

            default:
                Utils.clearConsole();
                Utils.invalidSelection(selection);
                adminMainMenu_Farms();

        }
        
    }

    private void adminMainMenu_Farm(Farm farm) {

        String selection = "";
        boolean validSelction = false;

        String title = farm.name() + " Inventory";
        Utils.underlineString(title);

        farm.viewInventory();
        
        System.out.println();

        String options = "1 - Add New Product | 2 - Restock Product | 3 - Back";
        Utils.surroundString(options);

        while(!validSelction){
            System.out.print("Your selection: ");
            selection = input.nextLine();

            switch(selection){
                case "1":
                    validSelction = true;
                    Utils.clearConsole();
                    HeadQ.createProduct(farm);
                    adminMainMenu_Farm(farm);
                    break;
                
                case "2":
                Utils.clearConsole();
                    if(farm.pullInventory() > 0){
                        validSelction = true;
                        HeadQ.restockProduct(farm);
                        adminMainMenu_Farm(farm);
                    } else {
                        validSelction = true;
                        adminMainMenu_Farm(farm);
                    }
                    break;

                case "3":
                    validSelction = true;
                    Utils.clearConsole();
                    adminMainMenu_Farms();
                    break;

                default:
                    //TODO: Check invalid
                    Utils.invalidSelection(selection);

                }
        
        }
    }


}
