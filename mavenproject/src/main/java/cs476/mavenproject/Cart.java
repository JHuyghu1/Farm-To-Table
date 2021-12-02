package cs476.mavenproject;
import java.util.ArrayList;


public class Cart {

	private ArrayList<Product> products = new ArrayList<Product>();
	private Buyer owner;
	private double payloadWeight = 0;
	private double cost = 0;
	Database DB;


	public Cart(Buyer buyer) {
		this.owner = buyer;

	}

	public Cart(Buyer buyer, ArrayList<Product> products, double payloadWeight, double cost) {
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

	// Copy constructor
	public Cart copy() {

		return new Cart(owner, copyProducts(), payloadWeight, cost);

	}

	public ArrayList<Product> copyProducts() {

		ArrayList<Product> copiedProducts = new ArrayList<Product>();

		for (Product product : products) {

			copiedProducts.add(product.copy());

		}

		return copiedProducts;

	}

	public ArrayList<Product> contents() {
		return products;
	}

	public void checkout() {

		for (Product product : products) {

			//Updates Product Client Side
			product.decreaseQuantity(product.quantityWanted());

			//TODO: Update Server Side
			//DB.addCartToDatabase(buyer, id, products, payloadWeight, cost);


			//Reset Products Wan
			product.resetQuantityWanted();

		}

	}


	// Used to remove product from cart
	public void remove(Product product, int quantity) {

		if (products.contains(product)) {

			Product foundProduct = products.get(products.indexOf(product));
			foundProduct.decreaseQuantityWanted(quantity);

		} else {
			System.out.println(product.name() + " is not in your cart!\n");
		}

	}

	// Used to add procut to cart
	public void add(Product product, int quantity) {

		if (product.quantityLeft() - (product.quantityWanted() + quantity) >= 0) {

			if ((product.weight() * product.quantityWanted() * quantity)
					+ this.payloadWeight <= Constants.WEIGHT_LIMIT) {

				product.increaseQuantityWanted(quantity);

				payloadWeight += product.weight() * quantity;
				cost += product.price();

				// Don't add product if it's already in the cart
				if (!products.contains(product)) {
					products.add(product);
				}

			} else {
				System.out.println("Could't add " + product.weight() + " grams of " + product.name()
						+ " max cart weight is " + Constants.WEIGHT_LIMIT + " grams \n");
			}

		} else {

			if (product.quantityLeft() == 0)
				System.out.println("Can't add " + product.name() + ", it's sold out!\n");
			else
				System.out.println("Can't add " + product.name() + ", not enough in stock!\n");

		}

	}

	public void view() {

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
	}

}
