package cs476.mavenproject;
import java.util.ArrayList;

public class Order {

	private String id;
	private ArrayList<Product> products = new ArrayList<Product>();

	public Order(String id) {
		this.id = id;
	}

}
