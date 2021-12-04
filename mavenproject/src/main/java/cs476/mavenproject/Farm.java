package cs476.mavenproject;
import java.util.ArrayList;


public class Farm {
	private String username;
	private String password;
	private ArrayList<Product> inventory = new ArrayList<Product>();
	private ArrayList<Product> soldHistory = new ArrayList<Product>();
	Database DB;
	Categories categories;

	// Constructor from client
	public Farm(Database DB, Categories categories, String username, String password) {
		this.DB = DB;
		this.categories = categories;
		this.username = username;
		this.password = password;
		pullInventory();
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

	}

	public void viewInventory(Boolean withFarmName) {

		pullInventory();

		
		if (inventory.size() > 0) {
			if(withFarmName){
				System.out.println("\nFarm: " + username );
				System.out.println("-----" );
			}
			for (Product product : inventory) {
				System.out.println(product.toString(false, 0));
				System.out.println("-");

			}
			
		}

	}

}
