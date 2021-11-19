package cs476.mavenproject;
import java.util.ArrayList;

public class Buyer {
	private String id;
	private String username;
	private String password;
	private String address;
	private ArrayList<Cart> purchaseHistory = new ArrayList<Cart>();
	private ArrayList<Buyer> following = new ArrayList<Buyer>();
	Cart cart;
	Database DB;
	public Buyer(String id, String username, String password, String address) {

		this.id = id;
		this.username = username;
		this.password = password;
		this.address = address;
		this.cart = new Cart(this);
		//this.id = DB.addBuyertoDatabase(username, pass, address);

	}

	// TODO: Load buyer from database
	public Buyer(String id) {

	}

	public String id() {
		return id;

	}

	public String username() {
		return username;

	}

	public String password() {
		return password;

	}

	public String address() {
		return address;

	}

	public ArrayList<Buyer> following() {
		return following;

	}

	public ArrayList<Cart> purchaseHistory() {
		return purchaseHistory;
	}

	public boolean isFollowing(Buyer buyer) {
		return following.contains(buyer);

	}

	public void follow(Buyer buyer) {

		if (!isFollowing(buyer)) {
			following.add(buyer);
			// TODO: Add relationship to database

		} else {
			// TODO: Pull username from database
			System.out.printf("Already following " + id);
		}

	}

	public void unFollow(Buyer buyer) {

		if (isFollowing(buyer)) {
			following.remove(buyer);
			// TODO: Remove relationship from database

		} else {
			// TODO: Pull username from database
			System.out.printf("You are not following " + id);
		}

	}

	public void viewPurchaseHistory() {

		for (Cart cart : purchaseHistory) {
			cart.view();
			System.out.println();

		}
	}

	public void checkout() {

		if (cart.contents().size() > 0) {
			this.purchaseHistory().add(cart.copy());
			cart.checkout();
			this.cart = new Cart(this);

		} else {
			System.out.println("Your cart is empty! \n");

		}
	}
}
