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
		this.farms = DB.getFramsFromDatabase(DB);
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

            if(!DB.usernameExists(tempName)){
                username = tempName;
            } else {
                System.out.println("That username is already taken!\n");
            }
        }

        System.out.print("Enter your pasword: ");
        password = input.nextLine();


        Farm tempFarm = DB.createFarmNode(DB, username, password);
        
		farms.add(tempFarm);

		Utils.clearConsole();

	}

	public void createProduct(){

        String name = "";
		Category category;
        SubCategory subCategory;
        double price = 0;
		int quantity;

        System.out.println("Add a product");
        System.out.println("--------------\n");

        System.out.print("Enter product name: ");
        name = input.nextLine();

		Utils.clearConsole();

		System.out.println("Choose a category");
		System.out.println("--------------\n");
		System.out.println("1. Vegtables");
		System.out.println("2. Fruits");
		System.out.println("3. Herbs");

		String selection = "";

		while(selection != "valid"){

		System.out.print("\n Your selection: ");
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
			System.out.println(selection + " is an invalid selection!");
			
		}

	}





	}
	public ArrayList<Farm> inventory() {
		return farms;
	}

	public void viewInventory() {
		if (farms.size() > 0) {
			for (Farm farm : farms) {
				farm.viewInventory();
				System.out.println("");
			}
		} else {

			System.out.println("Farm to Table has no iventory! \n");

		}

	}

}
