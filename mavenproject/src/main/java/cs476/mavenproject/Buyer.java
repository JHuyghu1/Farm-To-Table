package cs476.mavenproject;
import java.util.ArrayList;

public class Buyer {
	
	private String id;
	private String username;
	private String password;
	private String address;
	private ArrayList<Cart> purchaseHistory = new ArrayList<Cart>();
	private ArrayList<Buyer> following = new ArrayList<Buyer>();
	private ArrayList<Buyer> followers = new ArrayList<Buyer>();

	//Used for buyer search results
	private Boolean isFollowing = false;
	private int numFriendsFollowing = 0;


	//Dependencies
	Cart cart;
	Database DB;
	Categories categories;

	public Buyer(Database DB, Categories categories, final String username, final String password, final String address) {

		this.DB = DB;
		this.categories = categories;
		this.username = username;
		this.password = password;
		this.address = address;
		this.cart = new Cart(DB, categories, this.username);

	}

	//Used for search querry
	public Buyer(final String username, final int numFriendsFollowing){
		this.username = username;
		this.numFriendsFollowing = numFriendsFollowing;
	}

	//Used for recomendations querry
	public Buyer(final String username, final boolean isFollowing){
		this.username = username;
		this.isFollowing = isFollowing;
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

	public boolean isFollowing(){
		return isFollowing;
	}

	public void isFollowing(boolean status){
		isFollowing = status;
	}

	public int numFriendsFollowing(){
		return numFriendsFollowing;
	}

	public void numFriendsFollowing(int setNum){
		 numFriendsFollowing = setNum;
	}


	public ArrayList<Buyer> following() {
		return following;
	}

	public ArrayList<Buyer> followers() {
		return followers;
	}


	public ArrayList<Cart> purchaseHistory() {
		return purchaseHistory;
	}

	public boolean isFollowing(Buyer buyer) {
		return following.contains(buyer);

	}

	public void updatePurchaseHistory(){
		purchaseHistory = DB.getBuyerPurchaseHistory(DB, categories, username);
	}

	public void viewPurchaseHistory() {
		for (Cart cart : purchaseHistory) {
			cart.viewCart(false);
			System.out.println();

		}
	}
	
}
