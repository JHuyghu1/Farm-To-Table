package cs476.mavenproject;
import java.util.ArrayList;


public class Farm {

	private String id;
	private String username;
	private String password;
	private ArrayList<Product> inventory = new ArrayList<Product>();
	private ArrayList<Product> soldHistory = new ArrayList<Product>();
	Database DB;

	// Constructor from client
	public Farm(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
		//this.id = DB.createFarm(id, username, password);

	}

	// Constructor from DB
	public Farm(String id) {
		Farm temp = DB.findFarm(id);
	}

	public String id() {
		return id;
	}

	public String name() {
		return username;
	}

	public String password() {
		return password;
	}

	public ArrayList<Product> inventory() {
		return inventory;
	}

	public ArrayList<Product> soldHistory() {
		return soldHistory;
	}

	public void addNewProduct(Product product) {
		// TODO: Add farm sells product relationship
		if (!inventory.contains(product)) {
			inventory.add(product);
			//DB.addProductToFarm(username, product.id(), product.quantityLeft());
		} else {
			System.out.println(product.name() + " already exists. Use addExistingProduct()\n");

		}

	}

	public void addExistingProduct(Product product, int amount) {
		// TODO: Edit farm sells product relationship
		if (inventory.contains(product)) {
			inventory.get(inventory.indexOf(product)).increaseQuantity(amount);
			//DB.updateProductQuantity(username, product.id(), amount);

		} else {
			System.out.println(product.name() + " wasn't found.\n");
		}

	}

	public void sold(Product product) {

	}

	public void viewSoldHistory() {

		for (Product product : soldHistory) {
			System.out.println(product.toString(true));
			System.out.println();

		}
	}

	public void view() {
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
