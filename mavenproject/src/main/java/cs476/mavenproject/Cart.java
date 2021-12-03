package cs476.mavenproject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class Cart {

	public static enum CartStatus{
		BUILD,
		ORDERED,
        SHIPPED,
        DELIVERED
	}

	private Map<Integer, Entry<Product,Integer>> products = new HashMap<Integer, Entry<Product,Integer>>();
	private Buyer owner;
	private double payloadWeight = 0;
	private double cost = 0;
	private CartStatus status = CartStatus.BUILD;
	Database DB;


	public Cart(Buyer buyer) {
		this.owner = buyer;

	}

	public Cart(Buyer buyer, Map<Integer, Entry<Product,Integer>> products, double payloadWeight, double cost) {
		this.owner = buyer;
		this.cost = cost;
		this.products = products;
		this.payloadWeight = payloadWeight;

	}

	// TODO: Get order from database
	public Cart(String id) {

		//Cart temp = DB.findCart(id);
		//need to find a way to return cart object with ArrayList variable in it, within neo4j
	}

	public int maxQuantity(Product p){
		int maxQuantity = 0;
		Double productWeight = p.weight();
		Double capcityLeft = Constants.WEIGHT_LIMIT - payloadWeight;

		for(int i = 0; (maxQuantity*productWeight) < capcityLeft;  i++){
			maxQuantity = i;
		}
		return maxQuantity;

	}

	public Double currentWeight() {
		return payloadWeight;
	}

	public void checkout() {


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

		updatePayloadWeight();

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

		updatePayloadWeight();
	}

	//Go through cart and update the payload weight
	private void updatePayloadWeight(){

		final double [] weightSum = {0};

		products.forEach((k,v) -> {
			weightSum[0] = weightSum[0] + (v.getKey().weight()*v.getValue());
		} );

		payloadWeight = weightSum[0];
	}

	public void viewCart() {

		if(products.size() == 0){
			System.out.println("You have no products in your cart!");

		} else {
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

	public void printProduct(int productId){
		if(products.containsKey(productId)){
			Entry<Product, Integer> cartItem = products.get(productId);
			System.out.println(cartItem.getKey().toString(true, cartItem.getValue()));
		} else {
			System.out.println("Product isn't in cart!");
		}
	}
}
