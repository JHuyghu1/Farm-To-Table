package cs476.mavenproject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class Database implements AutoCloseable{
	private final Driver driver;
	Map<String, String> params;
	

	public Database(String uri, String username, String password){
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
	}
	
	@Override
	public void close() throws Exception {
		driver.close();
	}

	public String addBuyertoDatabase(final String username, final String pass, final String address ){
		  final String customer = "CREATE (n: Buyer {n.username: $username, n.password: $pass, n.address: $address} ) RETURN id(n)";
		  try(Session session = driver.session()){
			 String id = session.writeTransaction(new TransactionWork<String>(){
				  public String execute(Transaction tx){
					  Result result = tx.run(customer, parameters("username", username, "pass", pass, "address", address));
					  tx.commit();
					  return result.single().get("id").asString();
				  }
			  });
			 return id;
		  }
		  
	  }

	  public Buyer findBuyer(final String id){
		  final String customer = "MATCH n:Buyer WHERE n.id = $id RETURN n";
		  try(Session session = driver.session()){
			  Result buyer = session.readTransaction(new TransactionWork<Result>(){
				  public Result execute(Transaction tx){
					  Result result = tx.run(customer, parameters("id", id));
					  return result;
				  }
			  });
			  Buyer user = new Buyer(id, buyer.single().get("username").asString(), buyer.single().get("password").asString(), buyer.single().get("address").asString());
			  return user;
		  } 
	  }
	  //need to pass in user object
	  /*public void followUser(final String username, final String pass, final String address ) {
		  final String customer = "MATCH (a: Buyer) , (b:Buyer)  WHERE  a.username = <place a username here > AND b.username = $username
		  									CREATE (a)-[r:FOLLOWS]->(b)"
		  					
	  }*/
	  
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
		  final String query = "MATCH c:Cart WHERE c.id = $id RETURN c, collect(c.products) AS products";
		  try (Session session = driver.session()){
			  Result output = session.readTransaction(new TransactionWork<Result>() {
				  public Result execute(Transaction tx) {
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
	  
	  public Farm findFarm(final String id){
		  final String farmer = "MATCH farm:Farm WHERE farm.id = $id RETURN farm";
		  try(Session session = driver.session()){
			  Result f = session.readTransaction(new TransactionWork<Result>(){
				  public Result execute(Transaction tx){
					  Result result = tx.run(farmer, parameters("id", id));
					  return result;
				  }
			  });
			  Farm user = new Farm(id, f.single().get("username").asString(), f.single().get("password").asString());
			  return user;
		  } 
	  }
	  
	  //need to pass in farm object
	  /*public void addProductToFarm(final Product prod) {
		  final String farmer = "MATCH "
	  }*/

}
