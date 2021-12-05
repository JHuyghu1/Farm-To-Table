package cs476.mavenproject;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;


public class Cart {

	public static enum CartStatus{
		NEW,
		ORDERED,
        SHIPPED,
        DELIVERED
	}

	private Map<Integer, Entry<Product,Integer>> products = new HashMap<Integer, Entry<Product,Integer>>();
	private String owner;
	private int identity = -1;
	private double payloadWeight = 0;
	private double totalCost = 0;
	private CartStatus status = CartStatus.NEW;
	Database DB;
	Categories categories;


	//Used for a new cart
	public Cart(Database DB, Categories categories, String owner) {
		this.DB = DB;
		this.owner = owner;
		this.categories = categories;
	}

	public Cart(Database DB, Categories categories, int identity, String owner, double payloadWeight, double totalCost, CartStatus status) {
		this.DB = DB;
		this.categories = categories;
		this.owner = owner;
		this.identity = identity;
		this.totalCost = totalCost;
		this.payloadWeight = payloadWeight;
		this.status = status;
		pullCartItems();
	}

	public void pullCartItems(){
		products = DB.getCartItems(DB, categories, identity);
		
	}

	public Double currentWeight() {
		return payloadWeight;
	}

	public Double currentCost() {
		return totalCost;
	}


	public int identity(){
		return identity;
	}

	public String statusString(){
		return Utils.stringFromStatus(status);
	}


	public int maxQuantity(Product p){
		int maxQuantity = 0;
		Double productWeight = p.weight();
		Double capcityLeft = Constants.WEIGHT_LIMIT - payloadWeight;

		for(int i = 1; (i*productWeight) < capcityLeft;  i++){
			maxQuantity = i;
		}
		return maxQuantity;

	}

	public int checkout(Scanner input, boolean override) {

		boolean updated = false;
		int retID = -1;

		if(!override) updated = updateCartToCurrentSupply();
		
		if(updated && !override){

			userInputForCartChange(input);
 
		} else {
			Cart newCart = DB.createCartNode(DB, categories, owner, payloadWeight, totalCost, Utils.stringFromStatus(status));
		
			retID = newCart.identity;

			products.forEach((k,v) -> {
	
				int cartId = newCart.identity;
				int productId = v.getKey().identity();
				int amount = v.getValue();

				int newProductQuantity = v.getKey().quantity() - amount;
	
				DB.addProductToCart(cartId, productId, amount);
				DB.updateProductQuantity(productId, newProductQuantity);
	
	
			} );
			
			DB.updateCartStatus(retID, CartStatus.ORDERED);
		}

		return retID;
	}

	public int contains(int productId){
		//If the product isn't in the cart
		if(!products.containsKey(productId)){
			return 0;
		} else {

			//Get current cart item
			Entry<Product, Integer> cartItem = products.get(productId);

			//return total number of {product} in cart
			return cartItem.getValue();
		}
		


	}
	
	// Used to remove product from cart
	public void remove(Product product, int quantity) {
		
		int productId = product.identity();

		//Get current cart item
		Entry<Product, Integer> cartItem = products.get(productId);

		//Completely rmove product
		if(cartItem.getValue() == quantity){
			products.remove(productId);
		} else {
			//Overwrite cart item with difference in quantity
			products.put(productId, new SimpleEntry<Product, Integer>(product, cartItem.getValue() - quantity));
		}

		upateCartMetrics();

	}

	// Used to add procut to cart
	public void add(Product product, int quantity) {

		int productId = product.identity();

		//If the product isn't in the cart
		if(!products.containsKey(productId)){
			products.put(productId, new SimpleEntry<Product, Integer>(product, quantity));

		} else {
			//Get current cart item
			Entry<Product, Integer> cartItem = products.get(productId);

			//Overwrite cart item with summed quantity
			products.put(productId, new SimpleEntry<Product, Integer>(product, quantity + cartItem.getValue()));

		}

		upateCartMetrics();
	}

	//Go through cart and update the payload weight
	private void upateCartMetrics(){

		final double [] weightSum = {0};
		final double [] cost = {0};

		products.forEach((k,v) -> {
			weightSum[0] = weightSum[0] + (v.getKey().weight()*v.getValue());
			cost[0] = cost[0] + (v.getKey().price()*v.getValue());

		} );

		totalCost = cost[0];

		payloadWeight = weightSum[0];
	}

	private boolean updateCartToCurrentSupply(){

		final boolean [] changed = {false};

		products.forEach((k,v) -> {
			
			//Cart Product
			int productId = v.getKey().identity();
			int cartQuantity = v.getValue();

			//Pull product quantity to get most uptodate quantity
			Product updatedProduct = DB.findProduct(DB, categories, productId);
			int updatedProductQuanity = updatedProduct.quantity();

			//Check if you have enough room
			int quanityDifference = updatedProductQuanity - cartQuantity;

			//Update the product in cart
			products.put(productId, new SimpleEntry<Product, Integer>(updatedProduct, cartQuantity));


			if(quanityDifference < 0){
				remove(updatedProduct, quanityDifference);
				System.out.println("Removed " + quanityDifference + " Product ID: " + productId + " from cart due to low supply!\n");
				changed[0] = true;
			}

		});

		return changed[0];
	}

	//Print all cart items (Not categorized by farm)
	public void viewCart(boolean newCart) {


		if(newCart){

			if(products.size() == 0){
				System.out.println("You have no products in your cart!");
	
			} else {
	
				updateCartToCurrentSupply();
				products.forEach((k,v) -> {
					System.out.println(v.getKey().toString(true, v.getValue()));
				} );
		
			}
		} else {
			
			String statusString = Utils.stringFromStatus(status);
			System.out.println("\nStatus: " + statusString);
			System.out.print("--------");
			Utils.underlineString(statusString);


			products.forEach((k,v) -> {
				System.out.println(v.getKey().toString(true, v.getValue()));
			} );

		}

		/*
		int index = 0;

		if (products.size() > 0) {
			for (Product product : products) {
				System.out.println(++index + ". " + product.toString(true) + "\n");
			}

			System.out.println(
					"--> Cost: $" + cost + "\n--> Weight: " + payloadWeight + "/" + Constants.WEIGHT_LIMIT + " grams");

		} else {
			System.out.println("Cart is empty, nothing to view.");

		}
		*/
	}

	//Print a cart proudct to the console
	public void printProduct(int productId){
		if(products.containsKey(productId)){
			Entry<Product, Integer> cartItem = products.get(productId);
			System.out.println(cartItem.getKey().toString(true, cartItem.getValue()));
		} else {
			System.out.println("Product isn't in cart!");
		}
	}

	public boolean isEmpty(){
		return products.size() < 1;
	}

	private void userInputForCartChange(Scanner input){
		System.out.println("Items were removed due to suppluy, would you still like to checkout?");
		System.out.println("----------------");
		System.out.println("1 - Yes | 2 - No");
		System.out.println("----------------");
		
		String selection = "";
		boolean valid = false;

		while(!valid){
			System.out.print("\nYour selection: ");
			selection = input.nextLine();

			switch(selection){
				case "1":
					valid = true;
					checkout(input, true);
					break;
				case"2":
					valid = true;
					break;
				default:
					System.out.println(selection + " is an invalid selection!");
			}

		}
	}
}
