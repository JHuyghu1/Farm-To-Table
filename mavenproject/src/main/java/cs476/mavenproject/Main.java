package cs476.mavenproject;
import java.util.ArrayList;
//import org.neo4j.driver.*;



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Set up main

		Categories categories = new Categories();
		HeadQ HQ = new HeadQ();

		//set up database
		//Database DB = new Database("bolt://localhost:7687", "neo4j", "qzbOGbma98Zva4iBaaBDSRmioXMD0JT0IzNqzLrYp3k");

		Buyer buyer = new Buyer("098", "buyer_name", "1234", "Address");
		Farm farm1 = new Farm("028", "Farm1", "1234");
		Farm farm2 = new Farm("423", "Farm2", "1234");

		ArrayList<Product> productList = new ArrayList<Product>();

		for (int i = 0; i < 15; i++) {
			Product tempP = new Product(Integer.toString(i), farm1, "Name" + i, categories.fruits,
					categories.fruits.get(1), 5, i);
			productList.add(tempP);
		}

		for (int i = 15; i < 30; i++) {
			Product tempP = new Product(Integer.toString(i), farm2, "Name" + i, categories.fruits,
					categories.fruits.get(1), 5, i);
			productList.add(tempP);
		}

		farm1.addNewProduct(productList.get(3));
		farm1.addNewProduct(productList.get(4));
		farm1.addNewProduct(productList.get(5));

		farm2.addNewProduct(productList.get(0));
		farm2.addNewProduct(productList.get(23));
		farm2.addNewProduct(productList.get(9));

		HQ.addNewFarm(farm1);
		HQ.addNewFarm(farm2);

		buyer.cart.add(farm1.inventory().get(0), 2);
		buyer.cart.add(farm1.inventory().get(0), 1);

		buyer.cart.view();
		// buyer.checkout();
		// buyer.viewPurchaseHistory();

		farm1.viewSoldHistory();

		// HQ.viewInventory();

	}

}
