package cs476.mavenproject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Farm {
	private String username;
	private String password;
	private Map<Integer,Product> inventory = new HashMap<Integer,Product>();
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

	public ArrayList<Product> soldHistory() {
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
			System.out.println("No Inventory");

		} else {

			inventory.forEach((id, product) -> {
				System.out.println(product.toString(false, 0));
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

