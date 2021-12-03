package cs476.mavenproject;
import java.util.ArrayList;
import java.util.Scanner;

import cs476.mavenproject.Categories.Category;

public class HeadQ {

	private ArrayList<Farm> farms = new ArrayList<Farm>();
	Categories categories;
	Scanner input;
	Database DB;

	public HeadQ(Database DB, Scanner input, Categories categories) {
		this.DB = DB;
		this.input = input;
		this.farms = DB.getAllFrams(DB, categories);
		this.categories = categories;
	}

	public void createNewFarm() {

		String username = "";
        String password = "";

        System.out.println("Add a farm");
        System.out.println("--------------\n");

        while(username == ""){
            System.out.print("Enter their new username: ");
            String tempName = input.nextLine();

            if(!DB.farmUsernameExists(tempName)){
                username = tempName;
            } else {
                System.out.println("That username is already taken!\n");
            }
        }

        System.out.print("Enter your pasword: ");
        password = input.nextLine();


        Farm tempFarm = DB.createFarmNode(DB, categories, username, password);
        
		farms.add(tempFarm);

		Utils.clearConsole();

	}

	public void createProduct(){

        String name = "";
		Category category = null;
        SubCategory subCategory = null;
        double price = 0;
		int quantity = 0;
		Farm farm = null;

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


		Utils.clearConsole();

		selection = "";
		System.out.println("What farm is selling it?");
		System.out.println("------------------------");
		viewFarms();

		while(selection != "valid"){
			System.out.print("\nYour selection: ");
			selection = input.nextLine();

			int selctionInt = Integer.parseInt(selection);
			int numFarms = farms.size();

			if( selctionInt < 0 || selctionInt > numFarms){
				System.out.println("\n" + selection + " is an invalid selection!");
			} else {
				farm = farms.get(selctionInt-1);
				selection = "valid";
			}
	
		}

		//Add new product to DB
		Product newProduct = DB.createProductNode(DB, name, category, subCategory, price, quantity);
		
		//Connect the product to the farm
		int idAsInt = Integer.parseInt(newProduct.identity());
		DB.addProductToFarm(farm.name(), idAsInt);

		Utils.clearConsole();

	}

	public void restockProduct(){
		Product product = null;
		int poductId = 0;
        int quantity = 0;
		String selection = ""; 

		System.out.println("\n-----------------");
        System.out.println("Restock a product");
        System.out.println("-----------------\n");

		while(selection != "valid"){
			System.out.print("Enter Product ID: ");
			selection = input.nextLine();
			try{
				poductId = Integer.parseInt(selection);
				product = DB.findProduct(categories, DB, poductId);
				if(product == null) throw new Exception("Product doesn't exsits");
				selection = "valid";
			}catch(Exception e){
				System.out.println("\n" + selection + " is an invalid ID!");
			}
		}

		Utils.clearConsole();
		selection = "";

		//pull current quantity
		quantity = product.quantity();

        System.out.println("Restock " + product.name());
        System.out.println("-----------------");
		System.out.println("Current Quantity: " + quantity);

		while(selection != "valid"){
			System.out.print("\nEnter Restock Amount: ");
			selection = input.nextLine();
			try{
				quantity += Integer.parseInt(selection);
				selection = "valid";

			}catch(Exception e){
				System.out.println("\n" + selection + " is an invalid quanity!");
			}
		}

		DB.restockProduct(poductId, quantity);

		Utils.clearConsole();

	}




	public ArrayList<Farm> inventory() {
		return farms;
	}

	public void viewFarms(){
		for(int i = 1; i <= farms.size(); i++){
			System.out.println((i + " - " + farms.get(i-1).name()));
		}
	}

	public void viewInventory() {

		ArrayList<String> emptyFarms = new ArrayList<String>();

		if (farms.size() > 0) {
			for (Farm farm : farms) {
				farm.viewInventory();

				if(farm.inventory().size() < 1)
					emptyFarms.add(farm.name());
			}

			if(emptyFarms.size() > 0){
				System.out.println("\nFarms with no products: " + emptyFarms.toString());
			}

		} else {

			System.out.println("Farm to Table has no iventory! \n");

		}

	}

}
