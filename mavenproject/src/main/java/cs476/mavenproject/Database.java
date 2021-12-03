package cs476.mavenproject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Sides;

import java.util.HashMap;

import org.neo4j.driver.*;

import cs476.mavenproject.Categories.Category;

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
		  final String query = "CREATE (n: Buyer {username: $username, password: $password, address: $address} ) RETURN n.identity";
		  try(Session session = driver.session())
		  {
			 Buyer output = session.writeTransaction(new TransactionWork<Buyer>()
			 {
				  public Buyer execute(Transaction tx)
				  {
					  Result result = tx.run(query, parameters("username", username, "password", password, "address", address));
					  Buyer temp = new Buyer(DB, username, password, address);
					  tx.commit();

					  return temp;
				  }
			  });

			 return output;
		  } 
	  }

	public Boolean buyerUsernameExists(final String username){
		final String query = "MATCH (n:Buyer) WHERE n.username = $username RETURN count(n) > 0 as n";
		try(Session session = driver.session()){
			Boolean output = session.readTransaction(new TransactionWork<Boolean>(){
				@Override
				public Boolean execute( Transaction tx ){
					Result result = tx.run(query, parameters("username", username));
					return result.single().get(0).asBoolean();
				}
			});
			return output;
		} 
	}

	public Boolean verifyBuyerPassword(final String username, final String password){
		final String query = "MATCH (n:Buyer) WHERE n.username = $username RETURN n.password";
		try(Session session = driver.session()){
			Boolean output = session.readTransaction(new TransactionWork<Boolean>(){
				public Boolean execute(Transaction tx){
					Result result = tx.run(query, parameters("username", username));
					if(result.hasNext()){
						return (result.single().get("n.password").asString().equals(password));
					} else {
						return false; 
					}
				}
			});
			return output;
		}

	}
	// Find buyer using username. Return a new buyer
	
	public Buyer findBuyer(final Database DB, final String username){
		  final String query = "MATCH (n:Buyer) WHERE n.username = $username RETURN n.address, n.password";
		  try(Session session = driver.session()){
			  Buyer buyer = session.readTransaction(new TransactionWork<Buyer>(){
				  public Buyer execute(Transaction tx){
					  Result result = tx.run(query, parameters("username", username));
					  Record record = result.single();

					  Buyer temp = new Buyer(DB, username, record.get("n.address").asString(), record.get("n.password").asString());
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
		  final String cart = "MATCH (n:Buyer) WHERE n.username = $username, n.password = $password, AND n.address = $address"
		  					+ "CREATE (c:CART {owner: n, cartID: $c_id, weight: $weight, cost: $cost, products:[$products]}) "
		  					+ "CREATE (n)-[r:PURCHASED]->(c)";
		  try(Session session = driver.session()){
			  String x = session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tx) {
					  Result result = tx.run(cart, parameters("username", buyer.username(), "password", buyer.password(), "address", buyer.address(), 
							  "c_id", cart_id,  "weight", weight, "cost", cost, "products", products));
					  tx.commit();
					  return result.single().get("id").asString();
				  }
			  });
		  }
	  }
	  
	/*public void addProductToCart(final Cart cart, final String product_id){
		final String query = "MATCH (c:Cart) WHERE c.id = $id"
							
	}
	public Cart findCart(final String id) {
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
	  
	
	public Farm createFarmNode(final Database DB, final Categories categories, final String username, final String  password) {
		  final String query = "CREATE (f: Farm {username: $username, password: $password}) RETURN f";
		  try(Session session = driver.session()){
			 Farm farmer = session.writeTransaction(new TransactionWork<Farm>(){
				  public Farm execute(Transaction tx){
					  tx.run(query, parameters("username", username, "password", password));
					  tx.commit();
					  Farm temp = new Farm(DB, categories, username, password);
					  return temp;
				  }
			  });
			 return farmer;
		  }
	  }
	  
	public Farm findFarm(final Database DB, final Categories categories, final String username){
		  final String farmer1 = "MATCH (farm:Farm) WHERE farm.username = $username RETURN farm.password";
		  try(Session session = driver.session()){
			  Farm f = session.readTransaction(new TransactionWork<Farm>(){
				  public Farm execute(Transaction tx){
					  Result result = tx.run(farmer1, parameters("username", username));  
					  Farm temp = new Farm(DB, categories, username, result.single().get(0).asString());
					  return temp;
				  }
			  });
			  return f;
		  } 
	  }
	  
	public ArrayList<Farm> getAllFrams(final Database DB, final Categories categories){
		final String query = "MATCH (n:Farm) RETURN n.username";
		try(Session session = driver.session()){
			ArrayList<Farm> farms = session.readTransaction(new TransactionWork<ArrayList<Farm>>(){
				public ArrayList<Farm> execute(Transaction tx){

					ArrayList<Farm> tempFarms = new ArrayList<Farm>();
					Result result = tx.run(query);

					while(result.hasNext()){
						tempFarms.add(findFarm(DB, categories, result.next().get("n.username").asString()));
					}

					return tempFarms;

				}
			});
			return farms;
		} 

	}

	public Boolean verifyFarmPassword(final String username, final String password){
		final String query = "MATCH (n:Farm) WHERE n.username = $username RETURN n.password";
		try(Session session = driver.session()){
			Boolean output = session.readTransaction(new TransactionWork<Boolean>(){
				public Boolean execute(Transaction tx){
					Result result = tx.run(query, parameters("username", username));
					if(result.hasNext()){
						return (result.single().get("n.password").asString().equals(password));
					} else {
						return false; 
					}
				}
			});
			return output;
		}

	}

	public Boolean farmUsernameExists(final String username){
		final String query = "MATCH (f:Farm) WHERE f.username = $username RETURN count(f) > 0 as f";
		try(Session session = driver.session()){
			Boolean output = session.readTransaction(new TransactionWork<Boolean>(){
				@Override
				public Boolean execute( Transaction tx ){
					Result result = tx.run(query, parameters("username", username));
					return result.single().get(0).asBoolean();
				}
			});
			return output;
		} 
	}
	
	public void addProductToFarm(final String username, final int prodId) {
		  final String farmer = "MATCH (f:Farm), (p:Product) " +
		    					"WHERE f.username = $username AND id(p) = $prodId "+
		  		          		"CREATE (f)-[:SELLS]->(p) " +
								"RETURN f,p";
		  try(Session session = driver.session()){
			  session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tx) {
					  tx.run(farmer, parameters("username", username, "prodId", prodId));
					  tx.commit();

					  return null;
				  }
			  });
		  } 		
	}
	
	public void restockProduct(final int id, final int amount) {
		final String product = "MATCH (p:Product) WHERE id(p) = $id "
				+ "SET p.quantity = $amount "
				+ "RETURN p";
		try(Session session = driver.session()){
			session.writeTransaction(new TransactionWork<String>() {
				public String execute(Transaction tx) {
					tx.run(product, parameters("id", id, "amount", amount));
					tx.commit();

					return null;
				}
			});
		}	
	}

	public Product createProductNode(final Database DB, final String name, final Category category, final SubCategory subCategory, final double price, final int quantity) {
		final String query = "CREATE (p:Product {name: $name, category: $category, subCategory: $subCategory, price: $price, quantity: $quantity})"
							+ " RETURN id(p)";
		try(Session session = driver.session()){
			Product product = session.writeTransaction(new TransactionWork<Product>() {
				public Product execute(Transaction tx) {
					Result result = tx.run(query, parameters("name", name, "category", category.name(), "subCategory", subCategory.name(), "price", price, "quantity", quantity));
					Record record = result.single();
					String tempId = record.get(0).toString();

					tx.commit();
					
					Product temp = new Product(DB, tempId, name, category, subCategory, price, quantity);
					return temp;
				}
			});
		return product;
		}
	}

	public ArrayList<Product> getFarmInventory(final Database DB, final Categories categories, final String username){
		final String query = "MATCH (n:Farm)-[:SELLS]-(p:Product) WHERE n.username = $username RETURN id(p)";
		try(Session session = driver.session()){
			ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
				public ArrayList<Product> execute(Transaction tx){

					ArrayList<Product> tempInventory = new ArrayList<Product>();
					Result result = tx.run(query, parameters("username", username));

					while(result.hasNext()){
						tempInventory.add(findProduct(categories, DB, result.next().get(0).asInt()));
					}

					return tempInventory;

				}
			});
			return farms;
		} 

	}
	
	public Product findProduct(final Categories categories, final Database DB, final int id) {
		final String query = "MATCH (p:Product) WHERE id(p) = $id RETURN id(p) as id, p.name, p.category, p.subCategory, p.price, p.quantity";
		
		try(Session session = driver.session()){
			Product output = session.readTransaction(new TransactionWork<Product>() {
				public Product execute(Transaction tx) {
					Result result = tx.run(query, parameters("id", id));
					Record record = result.single();

					Category tempCategory = categories.getCategoryByName(record.get("p.category").asString());
					SubCategory tempSubCategory = tempCategory.get(record.get("p.subCategory").asString()); 

					Product tempProduct = new Product(DB, record.get("id").toString(), record.get("p.name").asString(), tempCategory, tempSubCategory, record.get("p.price").asDouble(), record.get("p.quantity").asInt());
					
					return tempProduct;
				}
			});

			return output;
		}
	}

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
