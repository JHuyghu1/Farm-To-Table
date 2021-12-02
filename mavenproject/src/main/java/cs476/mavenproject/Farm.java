package cs476.mavenproject;
import java.util.ArrayList;


public class Farm {
	private String username;
	private String password;
	private ArrayList<Product> inventory = new ArrayList<Product>();
	private ArrayList<Product> soldHistory = new ArrayList<Product>();
	Database DB;

	//Initaliize empty farm
	public Farm(){};

	// Constructor from client
	public Farm(Database DB, String username, String password) {
		this.DB = DB;
		this.username = username;
		this.password = password;
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

	//TODO: Pull current invortory from db
	public void pullInventory(){
	
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

		int index = 0;
		if (inventory.size() > 0) {
			System.out.println("Farm: " + username + "\n");
			for (Product product : inventory) {
				System.out.println(++index + ". " + product.toString(false) + "\n");
			}
		} else {

			System.out.println(username + " has no iventory\n");

		}
	}

}
