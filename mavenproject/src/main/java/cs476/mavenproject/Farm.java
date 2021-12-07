package cs476.mavenproject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Farm {
	private String username;
	private String displayName;
	private String password;
	private Map<Integer,Product> inventory = new HashMap<Integer,Product>();
	private ArrayList<Product> soldHistory = new ArrayList<Product>();
	Database DB;
	Categories categories;

	// Constructor from client
	public Farm(Database DB, Categories categories, String username, String displayName, String password) {
		this.DB = DB;
		this.categories = categories;
		this.username = username;
		this.displayName = displayName;
		this.password = password;
	}

	//Searching farms
	public Farm(Database DB, Categories categories, String username, String displayName) {
		this.DB = DB;
		this.categories = categories;
		this.username = username;
		this.displayName = displayName;
	}


	public String name() {
		return username;
	}

	public String displayName() {
		return displayName;
	}


	public String password() {
		return password;
	}

	public void pullSoldHistory(){
		soldHistory = DB.getSales(DB, categories, username);
	}

	public int pullInventory(){
		inventory = DB.getFarmInventory(DB, categories, username);
		return inventory.size();
	}

	public ArrayList<Product> soldHistory() {
		pullSoldHistory();
		return soldHistory;
	}

	public void sold(Product product) {

	}

	public void viewSoldHistory() {
		pullSoldHistory();


	}

	public void viewInventory() {

		pullInventory();

		if(inventory.isEmpty()){
			System.out.println("\nNo Products Found!");

		} else {

			inventory.forEach((id, product) -> {
				System.out.println(product.toString(false, 0));
				System.out.println("--");
			} );
		}

	}

	public boolean carriesProduct(int id){
		return inventory.containsKey(id);
	}

	public Product getProduct(int id){
		return inventory.get(id);
	}

}

