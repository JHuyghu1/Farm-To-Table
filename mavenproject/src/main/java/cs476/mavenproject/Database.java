package cs476.mavenproject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class Database implements AutoCloseable{
	private final Driver driver;
	//Map<String, String> params;
	

	public Database(String uri, String username, String password){
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
	}
	
	@Override
	public void close() throws Exception {
		driver.close();
	}

	public Buyer createBuyerNode(final Database DB, final String username, final String password, final String address ){
		  final String querry = "CREATE (n: Buyer {username: $username, password: $password, address: $address} ) RETURN n.identity";
		  try(Session session = driver.session())
		  {
			 Buyer output = session.writeTransaction(new TransactionWork<Buyer>()
			 {
				  public Buyer execute(Transaction tx)
				  {
					  Result result = tx.run(querry, parameters("username", username, "password", password, "address", address));
					  Buyer temp = new Buyer(DB, username, password, address);
					  tx.commit();

					  return temp;
				  }
			  });

			 return output;
		  } 
	  }

	public Boolean buyerUsernameExists(final String username){
		final String querry = "MATCH (n:Buyer) WHERE n.username = $username RETURN count(n) > 0 as n";
		try(Session session = driver.session()){
			Boolean output = session.readTransaction(new TransactionWork<Boolean>(){
				@Override
				public Boolean execute( Transaction tx ){
					Result result = tx.run(querry, parameters("username", username));
					return result.single().get(0).asBoolean();
				}
			});
			return output;
		} 
	}

	public String verifyBuyerPassword(final String username){
		final String query = "MATCH (n:Buyer) WHERE n.username = $username RETURN n.password";
		try(Session session = driver.session()){
			String output = session.readTransaction(new TransactionWork<String>(){
				public String execute(Transaction tx){
					Result result = tx.run(query, parameters("username", username));
					return result.single().get("password").asString();
				}
			});
			return output;
		}

	}
	// Find buyer using username. Return a new buyer
	
	public Buyer findBuyer(final Database DB, final String username){
		  final String customer = "MATCH n:Buyer WHERE n.username = $username RETURN n";
		  try(Session session = driver.session()){
			  Buyer buyer = session.readTransaction(new TransactionWork<Buyer>(){
				  public Buyer execute(Transaction tx){
					  Result result = tx.run(customer, parameters("username", username));
					  Buyer temp = new Buyer(DB, username, result.single().get("password").asString(), esult.single().get("address").asString());
					  return temp;
				  }
			  });
			  return buyer;
		  } 
	  }
	
	public void followUser(final String usernameA, final String usernameB ) {
		  final String follower = "MATCH (a: Buyer) , (b:Buyer)  "
		  		+ "WHERE  a.username = $usernameA AND b.username = $usernameB "
		  		+ "CREATE (a)-[r:FOLLOWS]->(b)";
		  try(Session session = driver.session()){
			  String x = session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tx) {
					  Result result = tx.run(follower, parameters("usernameA", usernameA, "usernameB", usernameB));
					  tx.commit();
					  return result.single().get("id").asString();  
				  }
			  });
		  }														
	  }
	  
	public void unfollowUser(final String usernameA, final String usernameB ) {
		  final String follower = "MATCH (a: Buyer {username: $usernameA})-[r:FOLLOWS]->(b:Buyer {username: $usernameB}) "
		  		+ "DELETE r";
		  try(Session session = driver.session()){
			  String x = session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tx) {
					  Result result = tx.run(follower, parameters("usernameA", usernameA, "usernameB", usernameB));
					  tx.commit();
					  return result.single().get("id").asString();  
				  }
			  });
		  }														
	  }
	
	public void addCartToDatabase(final Buyer buyer, final String cart_id, final ArrayList<Product> products, final double weight, final double cost) {
		  final String cart = "MATCH (n:Buyer) WHERE n.username = $username, n.password = $password, n.address = $address, AND n.id = $id"
		  					+ "CREATE (c:CART {owner: n, cartID: $c_id, weight: $weight, cost: $cost, products:[$products]}) "
		  					+ "CREATE (n)-[r:PURCHASED]->(c)";
		  try(Session session = driver.session()){
			  String x = session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tx) {
					  Result result = tx.run(cart, parameters("username", buyer.username(), "password", buyer.password(), "address", buyer.address(), "id", buyer.id(),
							  "c_id", cart_id,  "weight", weight, "cost", cost, "products", products));
					  tx.commit();
					  return result.single().get("id").asString();
				  }
			  });
		  }
	  }
	  
	/*public Cart findCart(final String id) {
		  final String query = "MATCH (c:Cart) WHERE id(c) = $id RETURN c, collect(c.products) AS products";
		  try (Session session = driver.session()){
			  Cart output = session.readTransaction(new TransactionWork<Cart>() {
				  public Cart execute(Transaction tx) {
					Result result = tx.run(query, parameters("id", id));
					  return result;
				  }  
			  });
					  
			  Buyer temp = new Buyer(id, output.single().get("username").asString(), output.single().get("password").asString(), output.single().get("address").asString());
			  
			  //trying to convert product result from database to an ArrayList
			  Cart cart = new Cart( temp, id, output.single().get("products").as, output.single().get("weight").asDouble(), output.single().get("cost").asDouble() );
					 
		  }
	  }*/
	  
	
	public String createFarm(final String username, final String  password) {
		  final String farmer = "CREATE (farm: Farm {farm.username: $username, farm.password: $password}) RETURN id(farm)";
		  try(Session session = driver.session()){
			 String id = session.writeTransaction(new TransactionWork<String>(){
				  public String execute(Transaction tx){
					  Result result = tx.run(farmer, parameters("username", username, "password", password));
					  tx.commit();
					  return result.single().get("id").asString();
				  }
			  });
			 return id;
		  }
	  }
	  
	public Farm findFarm(final String username){
		  final String farmer = "MATCH farm:Farm WHERE farm.username = $username RETURN farm";
		  try(Session session = driver.session()){
			  Farm f = session.readTransaction(new TransactionWork<Farm>(){
				  public Farm execute(Transaction tx){
					  Result result = tx.run(farmer, parameters("id", id));
					  return result;
				  }
			  });
			  //Farm user = new Farm(id, f.single().get("username").asString(), f.single().get("password").asString());
			  return f;
		  } 
	  }
	  
	
	public String verifyFarmPassword(final String username){
		final String query = "MATCH (m:Farm) WHERE m.username = $username RETURN m.password";
		try(Session session = driver.session()){
			String password = session.readTransaction(new TransactionWork<String>(){
				public String execute(Transaction tx){
					Result result = tx.run(query, parameters("username", username));
					return result.single().get("password").asString();
				}
			});
			return password;
		}

	}

	public Boolean farmUsernameExists(final String username){
		final String querry = "MATCH (f:Farm) WHERE m.username = $username RETURN count(m) > 0 as m";
		try(Session session = driver.session()){
			Boolean output = session.readTransaction(new TransactionWork<Boolean>(){
				@Override
				public Boolean execute( Transaction tx ){
					Result result = tx.run(querry, parameters("username", username));
					return result.single().get(0).asBoolean();
				}
			});
			return output;
		} 
	}
	public void addProductToFarm(final String username, final String prodId, final int quantityLeft) {
		  final String farmer = "MATCH f:Farm {username: $username}"
				+  "MATCH p:Product WHERE p.id = $prod_id"
		  		+ "CREATE (f)-[r:SELLS {quantityLeft: $quantityLeft}]->(p)";
		  try(Session session = driver.session()){
			  String temp = session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tc) {
					  Result result = tc.run(farmer, parameters("username", username, "prod_id", prodId, "quantityLeft", quantityLeft));
					  tc.commit();
					  return result.single().get("id").asString();
				  }
			  });
		  } 		
	}
	
	public void updateProductQuantity(final String username, final String id, final int amount) {
		final String product = "MATCH (f:Farm {username: $username})-[r:SELLS]->(prod:Product {prod.id: $id})"
				+ "SET r.quantityLeft = $amount"
				+ "RETURN r.quantityLeft";
		try(Session session = driver.session()){
			String temp = session.writeTransaction(new TransactionWork<String>() {
				public String execute(Transaction tx) {
					Result result = tx.run(product, parameters("username", username, "id", id, "amount", amount));
					tx.commit();
					return result.single().get("quantityLeft").asString();
				}
			});
		}	
	}

	public void addProductToDatabase(final String prodID, final Farm farm, final String name, final Category category, final SubCategory subCategory, final double price, final int quantityLeft, final int quantityWanted) {
		final String product = "CREATE p;Product {p.id: $prodID, p.Farm: $farm, p.name: $name, p.category: $category, p.subCategory: $subCat , p.price: $price, p.quantityLeft: $left, p.quantityWanted: $wanted";
		try(Session session = driver.session()){
			String temp = session.writeTransaction(new TransactionWork<String>() {
				public String execute(Transaction tx) {
					Result result = tx.run(product, parameters("prodId", prodID, "farm", farm, "name", name, "category", category, "subCat", subCategory, "price", price, "left", quantityLeft, "wanted", quantityWanted));
					tx.commit();
					return result.single().get("id").asString();
				}
			});
		}
	}

	/*public Product findProduct(final String id) {
		final String prod = "MATCH (p:Product) WHERE p.id = $id RETURN p, p.Farm, p.category, p.subcategory";
		try(Session session = driver.session()){
			Product temp = session.readTransaction(new TransactionWork<Product>() {
				public Product execute(Transaction tx) {
					Result result = tx.run(prod, parameters("id", id));
					return result;
				}
			});
			//need a new product constructor to take Farm, Category, and SubCategory as nonObject variables
			Product product = new Product(id, temp.single().get("farm").asObject(), temp.single().get("name").asString(), temp.single().get("category").asObject(),temp.single().get("subCategory").asObject(), temp.single().get("price").asDouble(), temp.single().get("quantityLeft").asInt(), temp.single().get("quantityWanted").asInt() );
		}
	}*/


	public Buyer recommendNewFollowers(final Database DB, final String username){
		final String query = "MATCH (n:Buyer {n.username: $username})->[:FOLLOWS]->(m)-[:FOLLOWS]->(s)"
							+ "WHERE not (n)-[:FOLLOWS]-(s)"
							+ "RETURN s";
		try (Session session = driver.session()){
			Buyer recommend = session.readTransaction(new TransactionWork<Buyer>(){
				public Buyer execute(Transaction tx){
					Result result = tx.run(query, parameters("username", username));
					Buyer temp = new Buyer( DB, username, result.single().get("password").asString(),result.single().get("address").asString());
					return temp;
				}
			});
			return recommend;
		}
	}

	/*public Product recommendNewProducts(final Cart cart, final String unsername){
		final String query = "MATCH (n:Buyer {n.username: $username})"
							+ "MATCH (curr:Cart {c.owner: n, c.products:[$products]}"
							+ "MATCH (prev:Cart {prev.owner: n})"
							+ "FOREACH (item IN node(prev)"
	}*/
	
	
}
