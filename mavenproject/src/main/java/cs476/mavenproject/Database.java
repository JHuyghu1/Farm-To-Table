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
		  final String querry = "CREATE (n: Buyer {username: $username, password: $password, address: $address} ) RETURN n";
		  try(Session session = driver.session())
		  {
			 Buyer output = session.writeTransaction(new TransactionWork<Buyer>()
			 {
				  public Buyer execute(Transaction tx)
				  {
					  tx.run(querry, parameters("username", username, "password", password, "address", address));
					  Buyer temp = new Buyer(DB, username, password, address);
					  tx.commit();

					  return temp;
				  }
			  });

			 return output;
		  } 
	  }

	public Farm createFarmNode(final Database DB, final String username, final String password ){
		final String querry = "CREATE (n: Farm {username: $username, password: $password} ) RETURN n";
		try(Session session = driver.session())
		{
		   Farm output = session.writeTransaction(new TransactionWork<Farm>()
		   {
				public Farm execute(Transaction tx)
				{
					tx.run(querry, parameters("username", username, "password", password));
					Farm temp = new Farm(DB, username, password);
					tx.commit();

					return temp;
				}
			});

		   return output;
		} 
	}

	public Boolean usernameExists(final String username){
		
		final String querry = "MATCH (n:Buyer) WHERE n.username = $username RETURN count(n) > 0 as n";

		try(Session session = driver.session())
		{
			Boolean output = session.readTransaction(new TransactionWork<Boolean>()
			{
				@Override
				public Boolean execute( Transaction tx )
				{
					Result result = tx.run(querry, parameters("username", username));
					return result.single().get(0).asBoolean();
				}
			});
			return output;
		} 
	}

	public Farm getFarmFromDataBase(final Database DB, final String username){

		final String querry = "MATCH (n:Farm) WHERE n.username = $username RETURN n.username, n.password";


		try(Session session = driver.session())
		{
			Farm output = session.readTransaction(new TransactionWork<Farm>()
			{
				@Override
				public Farm execute( Transaction tx )
				{
					Result result = tx.run(querry, parameters("username", username));
					Record record = result.single();

					String username = record.get("n.username").asString();
					String password = record.get("n.password").asString();


					Farm tempFarm = new Farm(DB, username, password);


					return tempFarm;
				}
			});


			return output;
		} 

	}
	
	public ArrayList<Farm> getFramsFromDatabase(final Database DB){
		final String querry = "MATCH (n:Farm) RETURN n.username";

		try(Session session = driver.session())
		{
			ArrayList<Farm> output = session.readTransaction(new TransactionWork<ArrayList<Farm>>()
			{
				@Override
				public ArrayList<Farm> execute( Transaction tx )
				{
					Result result = tx.run(querry);

					ArrayList<Farm> tempFarmList = new ArrayList<Farm>();

					while(result.hasNext()){
						tempFarmList.add(getFarmFromDataBase(DB, result.next().get(0).asString()));
					}
					return tempFarmList; 
				}
			});
			return output;
		} 
	}



	// Find buyer using username. Return a new buyer
	/*
	public Buyer findBuyer(final String username){
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
	  }*/
	
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
	  }
	  */
	
	  /*
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
	  */
	
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
		final String prod = "MATCH (p:Product) WHERE p.id = $id RETURN p";
		try(Session session = driver.session()){
			Result temp = session.readTransaction(new TransactionWork<Result>() {
				public Result execute(Transaction tx) {
					Result result = tx.run(prod, parameters("id", id));
					return result;
				}
			});
			//need a new product constructor to take Farm, Category, and SubCategory as nonObject variables
			Product product = new Product(id, temp.single().get("farm").asObject(), temp.single().get("name").asString(), temp.single().get("category").asObject(),temp.single().get("subCategory").asObject(), temp.single().get("price").asDouble(), temp.single().get("quantityLeft").asInt(), temp.single().get("quantityWanted").asInt() );
		}
	}*/
	
	
}
