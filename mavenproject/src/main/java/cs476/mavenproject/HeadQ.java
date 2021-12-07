package cs476.mavenproject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import cs476.mavenproject.Cart.CartStatus;
import cs476.mavenproject.Categories.Category;

public class HeadQ {

	private ArrayList<Farm> farms = new ArrayList<Farm>();
	Categories categories;
	Scanner input;
	Database DB;

	public HeadQ(Database DB, Scanner input, Categories categories) {
		this.DB = DB;
		this.input = input;
		this.categories = categories;
	}

	public int pullFarms(){
		farms = DB.getAllFrams(DB, categories);
		return farms.size();
	}
	public void createNewFarm() {

		String username = "";
		String displayName = "";
        String password = "";
		Boolean validEntry = false;

        System.out.println("Add a farm");
        System.out.println("--------------\n");

        while(!validEntry){

            System.out.print("Enter their new username: ");
            username = input.nextLine();
			username.toLowerCase();

            if(!Utils.validUsername(username)){
				System.out.println("Invalid Username!\n");

            } else if(DB.farmUsernameExists(username)){
				System.out.println("That username is already taken!\n");

			}else {
				validEntry = true;
            }
        }


		System.out.print("Enter their display name: ");
		displayName = input.nextLine();
        

        System.out.print("Enter their pasword: ");
        password = input.nextLine();


        Farm farm = DB.createFarmNode(DB, categories, username, displayName, password);
        
		farms.add(farm);

		Utils.clearConsole();

	}

	public void createProduct(Farm farm){

        String name = "";
		Category category = null;
        SubCategory subCategory = null;
        double price = 0;
		int quantity = 0;

        System.out.println("Add a product");
        System.out.println("--------------");

        System.out.print("Enter product name: ");
        name = input.nextLine();

		Utils.clearConsole();

		System.out.println("Choose a category");
		System.out.println("----------------");
		System.out.println("1. Vegtables");
		System.out.println("2. Fruits");
		System.out.println("3. Herbs\n");

		String selection = "";

		//Category Selection
		while(selection != "valid"){

			System.out.print("Your selection: ");
			selection = input.nextLine();

			switch(selection){

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
		System.out.println("--------------------");
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

		selection = "";

		System.out.println("Set the price");
		System.out.println("-------------");
		
		while(selection != "valid"){
			System.out.print("Price: $");
			selection = input.nextLine();
			try{
				price = Double.parseDouble(selection);
				selection = "valid";
			}catch(Exception e){
				System.out.println("\n" + selection + " is an invalid selection!");
			}
		}


		Utils.clearConsole();

		selection = "";

		System.out.println("Set the quantity");
		System.out.println("----------------");
		
		while(selection != "valid"){
			System.out.print("Quantity: ");
			selection = input.nextLine();
			try{
				quantity = Integer.parseInt(selection);
				selection = "valid";
			}catch(Exception e){
				System.out.println("\n" + selection + " is an invalid selection!");
			}
		}

		//Add new product to DB
		DB.createProductNode(DB, name, category, subCategory, price, quantity, farm.name());
		
		Utils.clearConsole();

	}

	public void restockProduct(Farm farm){
		Product product = null;
		int poductId = 0;
        int quantity = 0;
		String selection = ""; 
		boolean validSelection = false;

		String title = "Select a product to restock";
		Utils.surroundString(title);

		farm.viewInventory();
		

		while(!validSelection){

			System.out.print("\nEnter Product ID: ");
			selection = input.nextLine();

			try{
				poductId = Integer.parseInt(selection);
				if(!farm.carriesProduct(poductId)) throw new Exception("Product Not Found!");
				product = farm.getProduct(poductId);
				validSelection = true;

			}catch(NumberFormatException e){
				System.out.println("Enter an integer!");

			}catch(Exception e){
				System.out.println(e.getMessage());

			}
		}

		Utils.clearConsole();
		validSelection = false;

		quantity = product.quantity();

		String productInfo = "Product: " + product.name() + " | Current Inventory: " + quantity;
        System.out.println(productInfo);
		Utils.underlineString(productInfo);

		while(!validSelection){
			System.out.print("\nEnter Restock Amount: ");
			selection = input.nextLine();
			try{
				//Increase quantity
				quantity += Integer.parseInt(selection);
				validSelection = true;

			}catch(Exception e){
				System.out.println("Enter an integer!");
			}
		}

		//Push to database
		DB.updateProductQuantity(poductId, quantity);

		Utils.clearConsole();

	}

	public ArrayList<Farm> farms() {
		return farms;
	}

	public Farm selectFarm(){

		Utils.clearConsole();

		String selection = "";
		boolean validSelectionl = false;
		Farm farm = null;

		String title = "Head Quarters Inventory";

		System.out.println(title);
		Utils.underlineString(title);
		viewFarms();

		while(!validSelectionl){
			System.out.print("\nChoose a farm: ");
			selection = input.nextLine();

			int selctionInt = Integer.parseInt(selection);
			int numFarms = farms.size();

			if( selctionInt < 0 || selctionInt > numFarms){
				System.out.println(selection + " is an invalid selection!");
			} else {
				farm = farms.get(selctionInt-1);
				validSelectionl = true;
			}
	
		}

		return farm;

	}

	public int viewFarms(){
		
		if(pullFarms() < 1){
			System.out.println("\nNo farms in this HQ!");

		} else {

			for(int i = 1; i <= farms.size(); i++){

				String username = farms.get(i-1).name();
				String displayName = farms.get(i-1).displayName();

				System.out.println((i + " - " + username + " - " + displayName));
			}
	
		}


		return farms.size();
	}

	private void liveOrderOptions(ArrayList<Cart> orders){

		System.out.println();

		String optioins = "1 - Select Order | 2 - Refresh | 3 - Back";
		Utils.surroundString(optioins);

		boolean validSelection = false;
		String selection = "";

		while(!validSelection){

			System.out.print("Your Selection: ");
			selection = input.nextLine();

			switch(selection){

				case"1":
					validSelection = true;
						if(orders.isEmpty()) viewLiveOrders();
						else selectLiveOrder(Utils.cartArrayToMap(orders));
					break;

				case"2":
					validSelection = true;
					viewLiveOrders();
					break;

				case"3":
					validSelection = true;
					Utils.clearConsole();
					break;

				default:
					System.out.println(selection + " is an invalid selection!");
					break;

			}
		}
	}
	
	private void selectLiveOrder(Map<Integer,Cart> orders){

		boolean validSelection = false;
		String selection = "";

		while(!validSelection){

			System.out.print("Enter Order ID: ");
			selection = input.nextLine();

			try{

				int id = Integer.parseInt(selection);
				if(!orders.containsKey(id)) throw new Exception("That is not a live order!");
				validSelection = true;
				selectLiveOrderStatus(orders.get(id));

			} catch (NumberFormatException e){
				System.out.println("Enter an integer!");

			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	private void selectLiveOrderStatus(Cart cart){

		Utils.clearConsole();
		cart.pullCartItems();
		cart.viewCart(false);
		boolean validSelection = false;
		String selection = "";

		boolean isShipped = cart.status() != CartStatus.ORDERED;

		String optioins = (!isShipped)
							? "1 - Shipped | 2 - Back"
							: "1 - Delivered | 2 - Back";

		Utils.surroundString(optioins);


		while(!validSelection){

			System.out.print("Your Selection: ");
			selection = input.nextLine();

			switch(selection){

				case"1":
					validSelection = true;
					Utils.clearConsole();
					if (isShipped)
						DB.updateCartStatus(cart.identity(), CartStatus.DELIVERED);
					else
						DB.updateCartStatus(cart.identity(), CartStatus.SHIPPED);
					
					viewLiveOrders();
					break;

				case"2":
					validSelection = true;
					viewLiveOrders();
					break;

				default:
					System.out.println(selection + " is an invalid selection!");
					break;

			}
		
		}
	}

	public void viewLiveOrders(){
		
		ArrayList<Cart> liveOrders = DB.getLiveOrders(DB, categories);

		Utils.clearConsole();
		String title = "Live Orders";
		System.out.println(title);
		Utils.underlineString(title);
		System.out.println();

		if(liveOrders.isEmpty()) System.out.println("No orders to fulfill!");
		Utils.printCartsByStatus(liveOrders);
		liveOrderOptions(liveOrders);
}

}
