package cs476.mavenproject;
import java.util.ArrayList;


public class Farm {
	private String username;
	private String password;
	private ArrayList<Product> inventory = new ArrayList<Product>();
	private ArrayList<Product> soldHistory = new ArrayList<Product>();
	Database DB;
	Categories categories;


	//Initaliize empty farm
	public Farm(){};

	// Constructor from client
	public Farm(Database DB, Categories categories, String username, String password) {
		this.DB = DB;
		this.categories = categories;
		this.username = username;
		this.password = password;
		pullInventory();
	}

	// Constructor from DB
	public Farm(String username) {
		//TODO: Pull farm from db
		
	}

	public String name() {
		return username;
	}

	public String password() {
		return password;
	}

	//TODO: Pull sold history from db
	public void pullSoldHistory(){

	}

	public void pullInventory(){
		inventory = DB.getFarmInventory(DB, categories, username);
	}

	public ArrayList<Product> inventory() {
		return inventory;
	}

	public ArrayList<Product> soldHistory() {
		return soldHistory;
	}

	public void sold(Product product) {

	}

	public void viewSoldHistory() {

		pullSoldHistory();

		for (Product product : soldHistory) {
			System.out.println(product.toString(true));
			System.out.println();

		}
	}

	public void viewInventory() {

		pullInventory();

		
		if (inventory.size() > 0) {
			System.out.println("\nFarm: " + username );
			System.out.println("-----" );
			for (Product product : inventory) {
				System.out.println(product.toString(false));
				System.out.println("-");

			}
			
		}

	}

}
