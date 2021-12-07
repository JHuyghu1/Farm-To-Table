package cs476.mavenproject;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.HashMap;
import org.neo4j.driver.*;

import cs476.mavenproject.Cart.CartStatus;
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

	// -------- CREATE NODE -------------

	public Buyer createBuyerNode(final Database DB, final Categories categories, final String username, final String password, final String address ){
		  final String query = "CREATE (n: Buyer {username: $username, password: $password, address: $address} ) RETURN n.identity";
		  try(Session session = driver.session())
		  {
			 Buyer output = session.writeTransaction(new TransactionWork<Buyer>()
			 {
				  public Buyer execute(Transaction tx)
				  {
					  tx.run(query, parameters("username", username, "password", password, "address", address));
					  Buyer temp = new Buyer(DB, categories, username, password, address);
					  tx.commit();

					  return temp;
				  }
			  });

			 return output;
		  } 
	  }

	public Product createProductNode(final Database DB, final String name, final Category category, final SubCategory subCategory, final double price, final int quantity, final String farm) {
	final String query = "MATCH (f:Farm) WHERE f.username = $farm " +
						"CREATE (p:Product {name: $name, category: $category, subCategory: $subCategory, price: $price, quantity: $quantity, farm: $farm}) " +
						"CREATE (f)-[:SELLS]->(p) " +
						"RETURN id(p) ";
	try(Session session = driver.session()){
		Product product = session.writeTransaction(new TransactionWork<Product>() {
			public Product execute(Transaction tx) {
				Result result = tx.run(query, parameters("name", name, "category", category.name(), "subCategory", subCategory.name(), "price", price, "quantity", quantity, "farm", farm));
				Record record = result.single();
				int id = record.get(0).asInt();
				Product product = new Product(DB, id, name, category, subCategory, price, quantity, farm);

				tx.commit();
				
				return product;
			}
		});
	return product;
	}
}

	public Farm createFarmNode(final Database DB, final Categories categories, final String username, final String displayName, final String  password) {
		final String query = "CREATE (f: Farm {username: $username, password: $password, displayName: $displayName}) RETURN f";
		try(Session session = driver.session()){
		   Farm farmer = session.writeTransaction(new TransactionWork<Farm>(){
				public Farm execute(Transaction tx){
					tx.run(query, parameters("username", username, "displayName", displayName, "password", password));
					tx.commit();
					Farm temp = new Farm(DB, categories, username, displayName, password);
					return temp;
				}
			});
		   return farmer;
		}
	}

	public Cart createCartNode(final Database DB, final Categories categories, final String username, final double weight, final double cost, final String status) {

		final String query = "MATCH (b:Buyer) WHERE b.username = $username " +
							 "CREATE (c:Cart {owner: $username, cost: $cost, weight: $weight, status: $status}) " +
							 "CREATE (b)-[:PURCHASED]->(c) " +
							 "RETURN id(c)";
		try(Session session = driver.session()){
			Cart cart = session.writeTransaction(new TransactionWork<Cart>() {
				public Cart execute(Transaction tx) {
					Result result = tx.run(query, parameters("username", username, "weight", weight, "cost", cost, "status", status));
					Record record = result.single();
					int tempId = record.get(0).asInt();
					tx.commit();
					Cart temp = new Cart(DB, categories, tempId, username, weight, cost, Utils.statusFromString(status));
					return temp;
				}
			});
		return cart;
		}
	}

	// if BUYER username used RETURN false
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

	// if FARM username used RETURN false
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
		
	// -------- LOGIN VERIFICATION -------------

	// Buyer
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
	
	// Farm
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

	// -------- FIND SINGLE NODE -------------

	// By username
	public Buyer findBuyer(final Database DB, final Categories categories, final String username){
		  final String query = "MATCH (n:Buyer) WHERE n.username = $username RETURN n.address, n.password";
		  
		  try(Session session = driver.session()){
			  Buyer buyer = session.readTransaction(new TransactionWork<Buyer>(){
				  public Buyer execute(Transaction tx){
					  Result result = tx.run(query, parameters("username", username));

					  if(result.hasNext()){
						Record record = result.single();
						String address = record.get("n.address").asString();
						String password = record.get("n.password").asString();
						Buyer buyer = new Buyer(DB, categories, username, address, password);

						return buyer;

					  } else {
						return null;

					  }
				  }
			  });
			  return buyer;
		  } 
	  }
	
	public String getBuyerAddress(final String username){
		final String query = "MATCH (n:Buyer) WHERE n.username = $username RETURN n.address ";
		try(Session session = driver.session()){
			String output = session.readTransaction(new TransactionWork<String>(){
				public String execute(Transaction tx){

					Result result = tx.run(query, parameters("username", username));

					if(result.hasNext()){

						Record record = result.single();
						String a = record.get("n.address").asString();

						return a;
					}

					return "test";
				}
			});
			return output;
		} 
	}

	// By username
	public Farm findFarm(final Database DB, final Categories categories, final String username){
		final String farmer1 = "MATCH (farm:Farm) WHERE farm.username = $username RETURN farm.displayName, farm.password";
		try(Session session = driver.session()){
			Farm f = session.readTransaction(new TransactionWork<Farm>(){
				public Farm execute(Transaction tx){
					Result result = tx.run(farmer1, parameters("username", username));

					if(result.hasNext()){
						Record record = result.single();
						String displayName = record.get("farm.displayName").asString();
						String password = record.get("farm.password").asString();

						Farm farm = new Farm(DB, categories, username, displayName, password);
						return farm;
					}

					return null;
				}
			});
			return f;
		} 
	}
	
	public String getFarmDisplayName(final String username){

		final String query = "MATCH (farm:Farm{username:$username}) RETURN farm.displayName";
		try(Session session = driver.session()){
			String output = session.readTransaction(new TransactionWork<String>(){
				public String execute(Transaction tx){
					Result result = tx.run(query, parameters("username", username));

					if(result.hasNext()){
						Record record = result.single();
						String displayName = record.get("farm.displayName").asString();

						return displayName;
					}

					return null;
				}
			});
			return output;
		} 
	}


	// By id
	public Product findProduct(final Database DB, final Categories categories, final int id) {
		final String query = "MATCH (p:Product) WHERE id(p) = $id RETURN p.name, p.category, p.subCategory, p.price, p.quantity, p.farm";
		
		try(Session session = driver.session()){
			Product output = session.readTransaction(new TransactionWork<Product>() {
				public Product execute(Transaction tx) {
					Result result = tx.run(query, parameters("id", id));
					Record record = result.single();

					Category category = categories.getCategoryByName(record.get("p.category").asString());
					SubCategory subCategory = category.get(record.get("p.subCategory").asString());
					String name = record.get("p.name").asString();
					double price = record.get("p.price").asDouble();
					int quantity = record.get("p.quantity").asInt();
					String farm = record.get("p.farm").asString();


					Product product = new Product(DB, id, name, category, subCategory, price, quantity, farm);
					
					return product;
				}
			});

			return output;
		}
	}

	// By id
	public Cart findCart(final Database DB, final Categories categories, final int id) {
		final String query = "MATCH (c:Cart) WHERE id(c) = $id RETURN c.owner, c.weight, c.cost, c.status";
		
		try(Session session = driver.session()){
			Cart output = session.readTransaction(new TransactionWork<Cart>() {
				public Cart execute(Transaction tx) {
					Result result = tx.run(query, parameters("id", id));

					if(result.hasNext()){

						Record record = result.single();

						String owner = record.get("c.owner").asString();
						double payloadWeight = record.get("c.weight").asDouble();
						double totalCost = record.get("c.cost").asDouble();
						String stringStatus = record.get("c.status").asString();
						CartStatus status = Utils.statusFromString(stringStatus);



						Cart tempCart = new Cart(DB, categories, id, owner, payloadWeight, totalCost, status);

						return tempCart;

					} else {

						return null;
					}
					
				}
			});

			return output;
		}
	}

	// -------- BUYER ACTIONS -------------

	// user & target = buyer.username (return target)
	public String followUser(final String user, final String target) {
		  final String follower = "MATCH (u:Buyer), (t:Buyer) " +
		  						  "WHERE  u.username = $user AND t.username = $target " +
		  						  "CREATE (u)-[r:FOLLOWS]->(t) " +
								  "RETURN t.username";
		  try(Session session = driver.session()){
			  String output = session.writeTransaction(new TransactionWork<String>() {
				  public String execute(Transaction tx) {
					  Result result = tx.run(follower, parameters("user", user, "target", target));
					  Record record = result.single();
					  tx.commit();

					  return record.get("t.username").asString();  
				  }
			  });
			  return output;
		  }														
	  }
	
	public String unfollowUser(final String user, final String target) {
		final String follower = "MATCH (u:Buyer)-[r:FOLLOWS]->(t:Buyer) " +
								"WHERE  u.username = $user AND t.username = $target " +
								"DELETE r " +
								"RETURN t.username";
		try(Session session = driver.session()){
			String output = session.writeTransaction(new TransactionWork<String>() {
				public String execute(Transaction tx) {
					Result result = tx.run(follower, parameters("user", user, "target", target));
					Record record = result.single();
					String username = record.get("t.username").asString();

					tx.commit();

					return username;  
				}
			});
			return output;
		}														
	}
	
	// Buyer -[r:FOLLOWS]-> Buyer(Current user)
	public ArrayList<Buyer> getFollowers(final Database DB, final Categories categories, final String username){
		final String query = "MATCH (t:Buyer)-[:FOLLOWS]->(u:Buyer) WHERE u.username = $username " + 
							 "RETURN t.username as follower, exists((u)-[:FOLLOWS]->(t)) as isFollowing";
		try(Session session = driver.session()){
			ArrayList<Buyer> farms = session.readTransaction(new TransactionWork<ArrayList<Buyer>>(){
				public ArrayList<Buyer> execute(Transaction tx){

					ArrayList<Buyer> tempFollowers = new ArrayList<Buyer>();
					Result result = tx.run(query, parameters("username", username));

					while(result.hasNext()){
						Record record = result.next();
						String followerUsername = record.get("follower").asString();
						boolean isFollowing = record.get("isFollowing").asBoolean();

						Buyer follower = new Buyer(followerUsername, isFollowing);

						tempFollowers.add(follower);
					}

					return tempFollowers;

				}
			});
			return farms;
		} 
	}
	
    // Buyer(Current user) -[r:FOLLOWS]-> Buyer
	public ArrayList<Buyer> getFollowing(final Database DB, final Categories categories, final String username){
		final String query = "MATCH (u:Buyer)-[:FOLLOWS]->(t:Buyer) WHERE u.username = $username RETURN t.username as following";
		try(Session session = driver.session()){
			ArrayList<Buyer> farms = session.readTransaction(new TransactionWork<ArrayList<Buyer>>(){
				public ArrayList<Buyer> execute(Transaction tx){

					ArrayList<Buyer> tempFollowers = new ArrayList<Buyer>();
					Result result = tx.run(query, parameters("username", username));

					while(result.hasNext()){
						String followerUsername = result.next().get("following").asString();
						Buyer follower = new Buyer(followerUsername, true);
						
						tempFollowers.add(follower);
					}

					return tempFollowers;

				}
			});
			return farms;
		} 
	}

	// Buyer -[r:PURCHASED]-> Cart
	public ArrayList<Cart> getBuyerPurchaseHistory(final Database DB, final Categories categories, final String username ){

		final String query = "MATCH (b:Buyer)-[:PURCHASED]->(c:Cart) WHERE b.username = $username "+
							 "RETURN id(c) as id, c.owner, c.weight, c.cost, c.status";
		try(Session session = driver.session()){
			ArrayList<Cart> carts = session.readTransaction(new TransactionWork<ArrayList<Cart>>(){
				public ArrayList<Cart> execute(Transaction tx){

					ArrayList<Cart> tempCarts = new ArrayList<Cart>();
					Result result = tx.run(query, parameters("username", username));

					while(result.hasNext()){
						Record record = result.next();
						int cartId = record.get("id").asInt();
						String owner = record.get("c.owner").asString();
						double payloadWeight = record.get("c.weight").asDouble();
						double totalCost = record.get("c.cost").asDouble();
						String stringStatus = record.get("c.status").asString();
						CartStatus status = Utils.statusFromString(stringStatus);

						Cart foundCart = new Cart(DB, categories, cartId, owner, payloadWeight, totalCost, status);
						tempCarts.add(foundCart);
					}



					return tempCarts;

				}
			});
			return carts;
		} 

	}
	
	// Show people you are following first
	public ArrayList<Buyer> findBuyersByUsername(final Database DB, final Categories categories, final String user, final String target){
		final String query = "MATCH (t:Buyer) WHERE t.username CONTAINS $target AND t.username <> $user " +
							 "RETURN t.username, exists((:Buyer{username: $user})-[:FOLLOWS]->(t)) as isFollowing " +
							 "ORDER BY isFollowing DESC";
		try(Session session = driver.session()){
			ArrayList<Buyer> farms = session.readTransaction(new TransactionWork<ArrayList<Buyer>>(){
				public ArrayList<Buyer> execute(Transaction tx){

					ArrayList<Buyer> foundBuyers = new ArrayList<Buyer>();
					Result result = tx.run(query, parameters("user", user, "target", target));

					while(result.hasNext()){
						Record record = result.next();
						String foundUsername = record.get("t.username").asString();
						Boolean isfollowing = record.get("isFollowing").asBoolean();
						Buyer buyer = new Buyer(foundUsername, isfollowing);
						foundBuyers.add(buyer);
					}

					return foundBuyers;
					
					
				}
			});
			return farms;
		} 

	}

	public ArrayList<Product> favoriteItems(final Database DB, final Categories categories, final String username){
		final String query = "MATCH (f:Farm)<-[r:EXCHANGED]-(b:Buyer{username: $username}), (p:Product) " +
							"WHERE id(p) = r.product " + 
							"RETURN id(p) as id, p.name, p.price, p.category, p.subCategory, sum(r.amount) as total " +
							"ORDER BY total DESC LIMIT 5";

		try(Session session = driver.session()){
			ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
				public ArrayList<Product> execute(Transaction tx){

					ArrayList<Product> sales = new ArrayList<Product>();
					Result result = tx.run(query, parameters("username", username));

					while(result.hasNext()){
						Record record = result.next();

						int identity = record.get("id").asInt();
						String name = record.get("p.name").asString();
						Double price = record.get("p.price").asDouble();
						int totalSold = record.get("total").asInt();
						Category category = categories.getCategoryByName(record.get("p.category").asString());
						SubCategory subCategory = category.get(record.get("p.subCategory").asString());

						Product product = new Product(identity, name, price, totalSold, category, subCategory);

						sales.add(product);
					}

					return sales;

				}
			});
			return farms;
		} 
	}

	public ArrayList<Product> bProductRecommendations(final Database DB, final Categories categories, final Product product){

		final String query = "MATCH (c:Cart)-[r:CONTAINS]->(p:Product), (sameCartProduct:Product) " +
							 "WHERE id(p) = $productId "+ 
							 "AND sameCartProduct <> p AND sameCartProduct.subCategory != $subCategory " +
							 "AND (c)-[rr:CONTAINS]->(sameCartProduct)" + 
							 "RETURN id(sameCartProduct) as id, sameCartProduct.name as name, sameCartProduct.price as price, "+ 
							 "sameCartProduct.category as category, p.subCategory as subCategory, count(rr) as frequency " +
							 "ORDER BY frequency DESC LIMIT 5";

		try(Session session = driver.session()){
			ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
				public ArrayList<Product> execute(Transaction tx){

					ArrayList<Product> sales = new ArrayList<Product>();
					Result result = tx.run(query, parameters("productId", product.identity(), "subCategory", product.subCategory().name()));

					while(result.hasNext()){
						Record record = result.next();

						int identity = record.get("id").asInt();
						String name = record.get("name").asString();
						Double price = record.get("price").asDouble();
						int totalSold = record.get("frequency").asInt();
						Category category = categories.getCategoryByName(record.get("p.category").asString());
						SubCategory subCategory = category.get(record.get("p.subCategory").asString());

						Product product = new Product(identity, name, price, totalSold, category, subCategory);

						sales.add(product);
					}

					return sales;

				}
			});
			return farms;
		} 
	}



	
	
	// -------- FARM ACTIONS -------------

	// Return inventory for farm (username)
	public HashMap<Integer,Product> getFarmInventory(final Database DB, final Categories categories, final String farm){
		final String query = "MATCH (f:Farm{username: $farm})-[:SELLS]->(p:Product) RETURN id(p) as id, p.name, p.category, p.subCategory, p.price, p.quantity, p.farm";
		try(Session session = driver.session()){
			HashMap<Integer,Product> farms = session.readTransaction(new TransactionWork<HashMap<Integer,Product>>(){
				public HashMap<Integer,Product> execute(Transaction tx){

					HashMap<Integer,Product> inventory = new HashMap<Integer,Product>();
					Result result = tx.run(query,parameters("farm", farm));

					while(result.hasNext()){
						Record record = result.next();

						int id = record.get("id").asInt();
						Category category = categories.getCategoryByName(record.get("p.category").asString());
						SubCategory subCategory = category.get(record.get("p.subCategory").asString());
						String name = record.get("p.name").asString();
						double price = record.get("p.price").asDouble();
						int quantity = record.get("p.quantity").asInt();
						String farm = record.get("p.farm").asString();

						Product product = new Product(DB, id, name, category, subCategory, price, quantity, farm);
						inventory.put(id, product);
					}

					return inventory;

				}
			});
			return farms;
		} 

	}
	
	// Return all farms
	public ArrayList<Farm> getAllFrams(final Database DB, final Categories categories){
		final String query = "MATCH (n:Farm) RETURN n.username, n.displayName";
		try(Session session = driver.session()){
			ArrayList<Farm> farms = session.readTransaction(new TransactionWork<ArrayList<Farm>>(){
				public ArrayList<Farm> execute(Transaction tx){

					ArrayList<Farm> farmsList = new ArrayList<Farm>();
					Result result = tx.run(query);

					while(result.hasNext()){

						Record record = result.next();
						String displayName = record.get("n.displayName").asString();
						String username = record.get("n.username").asString();

						Farm farm = new Farm(DB, categories, username, displayName);
						farmsList.add(farm);
					}

					return farmsList;

				}
			});
			return farms;
		} 

	}

	// -------- PRODUCT ACTIONS -------------
	public ArrayList<Product> customProductSearch(final Database DB, final Categories categories, final SubCategory subCategory){
		final String query = "MATCH (p:Product) WHERE p.subCategory = $subCategory " + 
							 "RETURN id(p) as id, p.name, p.category, p.subCategory, p.price, p.quantity, p.farm";
		try(Session session = driver.session()){
			ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
				public ArrayList<Product> execute(Transaction tx){

					ArrayList<Product> tempInventory = new ArrayList<Product>();
					Result result = tx.run(query, parameters("subCategory", subCategory.name()));

					while(result.hasNext()){
						Record record = result.next();

						Category category = categories.getCategoryByName(record.get("p.category").asString());
						String subCatString = record.get("p.subCategory").asString();
						SubCategory subCategory = category.get(subCatString);
						int id = record.get("id").asInt();
						String name = record.get("p.name").asString();
						double price = record.get("p.price").asDouble();
						int quantity = record.get("p.quantity").asInt();
						String farm = record.get("p.farm").asString();
	
	
						Product product = new Product(DB, id, name, category, subCategory, price, quantity, farm);
		
						tempInventory.add(product);
					}

					return tempInventory;

				}
			});
			return farms;
		} 

	}

	// Change product quantity property
	public void updateProductQuantity(final int id, final int amount) {
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


	// Create Product -[r:CONTAINS]-> Cart (r.amount)
	public void addProductToCart(final int cartId, final int prodId, final int amount) {
		System.out.println(cartId + " " + amount + " " + prodId);

		final String query = "MATCH (c:Cart), (p:Product), (b:Buyer), (f:Farm) " +
							 "WHERE id(c) = $cartId AND id(p) = $prodId AND b.username = c.owner AND f.username = p.farm "+
							 "CREATE (c)-[r:CONTAINS{amount: $amount}]->(p) " +
							 "CREATE (b)-[r2:EXCHANGED{product: $prodId, amount: $amount}]->(f) " +
							 "RETURN c.owner";
		try(Session session = driver.session()){
			session.writeTransaction(new TransactionWork<String>() {
				public String execute(Transaction tx) {
					Result result = tx.run(query, parameters("cartId", cartId, "prodId", prodId, "amount", amount ));
					Record record = result.single();
					
					tx.commit();
					return null;
				}
			});
		} 		
  }

   	public ArrayList<Product> getSales(final Database DB, final Categories categories, final String username){
	final String query = "MATCH (f:Farm{username: $username})<-[r:EXCHANGED]-(b:Buyer), (p:Product) " +
						 "WHERE id(p) = r.product " + 
						 "RETURN id(p) as id, p.name, p.price, p.category, p.subCategory, sum(r.amount) as total " +
						 "ORDER BY total DESC";

	try(Session session = driver.session()){
		ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
			public ArrayList<Product> execute(Transaction tx){

				ArrayList<Product> sales = new ArrayList<Product>();
				Result result = tx.run(query, parameters("username", username));

				while(result.hasNext()){
					Record record = result.next();

					int identity = record.get("id").asInt();
					String name = record.get("p.name").asString();
					Double price = record.get("p.price").asDouble();
					int totalSold = record.get("total").asInt();
					Category category = categories.getCategoryByName(record.get("p.category").asString());
					SubCategory subCategory = category.get(record.get("p.subCategory").asString());

					Product product = new Product(identity, name, price, totalSold, category, subCategory);

					sales.add(product);
				}

				return sales;

			}
		});
		return farms;
	} 
}

	public ArrayList<Product> topFiveSellers(final Database DB, final Categories categories, final String username){
		final String query = "MATCH (f:Farm{username: $username})<-[r:EXCHANGED]-(b:Buyer), (p:Product) " +
							"WHERE id(p) = r.product " + 
							"RETURN id(p) as id, p.name, p.price, p.category, p.subCategory, sum(r.amount) as total " +
							"ORDER BY total DESC LIMIT 5";

		try(Session session = driver.session()){
			ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
				public ArrayList<Product> execute(Transaction tx){

					ArrayList<Product> sales = new ArrayList<Product>();
					Result result = tx.run(query, parameters("username", username));

					while(result.hasNext()){
						Record record = result.next();

						int identity = record.get("id").asInt();
						String name = record.get("p.name").asString();
						Double price = record.get("p.price").asDouble();
						int totalSold = record.get("total").asInt();
						Category category = categories.getCategoryByName(record.get("p.category").asString());
						SubCategory subCategory = category.get(record.get("p.subCategory").asString());

						Product product = new Product(identity, name, price, totalSold, category, subCategory);

						sales.add(product);
					}

					return sales;

				}
			});
			return farms;
		} 
	}

	public ArrayList<Product> productRecommendations(final Database DB, final Categories categories, final Product product){

		final String query = "MATCH (c:Cart)-[r:CONTAINS]->(p:Product), (sameCartProduct:Product) " +
							 "WHERE id(p) = $productId "+ 
							 "AND sameCartProduct <> p AND sameCartProduct.subCategory <> $subCategory " +
							 "AND (c)-[:CONTAINS]->(sameCartProduct)" + 
							 "RETURN id(sameCartProduct) as id, sameCartProduct.name as name, sameCartProduct.price as price, "+ 
							 "sameCartProduct.category as car, p.subCategory as sub, count(sameCartProduct) as frequency " +
							 "ORDER BY frequency DESC LIMIT 5";

		try(Session session = driver.session()){
			ArrayList<Product> farms = session.readTransaction(new TransactionWork<ArrayList<Product>>(){
				public ArrayList<Product> execute(Transaction tx){

					ArrayList<Product> sales = new ArrayList<Product>();
					Result result = tx.run(query, parameters("productId", product.identity(), "subCategory", product.subCategory().name()));

					while(result.hasNext()){
						Record record = result.next();

						int identity = record.get("id").asInt();
						String name = record.get("name").asString();
						Double price = record.get("price").asDouble();
						int totalSold = record.get("frequency").asInt();
						Category category = categories.getCategoryByName(record.get("cat").asString());
						SubCategory subCategory = category.get(record.get("sub").asString());

						Product product = new Product(identity, name, price, totalSold, category, subCategory);

						sales.add(product);
					}

					return sales;

				}
			});
			return farms;
		} 
	}

	// -------- CART ACTIONS -------------

	// [Cart ID <Product, Quantity in Cart>]
  	public HashMap<Integer, Entry<Product,Integer>> getCartItems(final Database DB, final Categories categories, final int cartId){

		final String query = "MATCH (c:Cart)-[r:CONTAINS]->(p:Product) WHERE id(c) = $cartId "+
							 "RETURN id(p) as id, p.name, p.category, p.subCategory, p.price, p.quantity, r.amount";
		try(Session session = driver.session()){
			HashMap<Integer, Entry<Product,Integer>> output = session.readTransaction(new TransactionWork<HashMap<Integer, Entry<Product,Integer>>>(){
				public HashMap<Integer, Entry<Product,Integer>> execute(Transaction tx){

					HashMap<Integer, Entry<Product,Integer>> tempInventory = new HashMap<Integer, Entry<Product,Integer>>();

					Result result = tx.run(query, parameters("cartId", cartId));

					while(result.hasNext()){
						Record record = result.next();

						int cartQuantity = record.get("r.amount").asInt();
						Category category = categories.getCategoryByName(record.get("p.category").asString());
						SubCategory subCategory = category.get(record.get("p.subCategory").asString());
						int id = record.get("id").asInt();
						String name = record.get("p.name").asString();
						double price = record.get("p.price").asDouble();
						int quantity = record.get("p.quantity").asInt();
						String farm = record.get("p.farm").asString();
	
						Product product = new Product(DB, id, name, category, subCategory, price, quantity, farm);
	
						tempInventory.put(id, new SimpleEntry<Product, Integer>(product, cartQuantity));

					} 
											

					return tempInventory;

				}
			});
			return output;
		} 

	}
	
	public void updateCartStatus(final int cartId, final CartStatus status){
		final String product = "MATCH (c:Cart) WHERE id(c) = $cartId "
							 + "SET c.status = $status "
							 + "RETURN c";
		try(Session session = driver.session()){
			session.writeTransaction(new TransactionWork<String>() {
				public String execute(Transaction tx) {
					tx.run(product, parameters("cartId", cartId, "status", Utils.stringFromStatus(status)));
					tx.commit();

					return null;
				}
			});
		}	

	}

	public ArrayList<Buyer> recommendNewFollowers(final String username){
		final String query = "MATCH (main:Buyer {username: $username})-[:FOLLOWS]->(f:Buyer)-[:FOLLOWS]->(ff:Buyer) "
							+ "WHERE main <> ff AND NOT (main)-[:FOLLOWS]->(ff) "
							+ "RETURN ff.username, count(ff) as frequency "
							+ "ORDER BY frequency DESC LIMIT 10 ";

		try (Session session = driver.session()){
			ArrayList<Buyer> users = session.readTransaction(new TransactionWork<ArrayList<Buyer>>(){
				public ArrayList<Buyer> execute(Transaction tx){					
					Result result = tx.run(query, parameters("username", username));

					ArrayList<Buyer> recommendations = new ArrayList<Buyer>();

					while(result.hasNext()){
						Record record = result.next();
						String username = record.get("ff.username").asString();
						int frequency = record.get("frequency").asInt();

						Buyer buyer = new Buyer(username, frequency);
						recommendations.add(buyer);

					}					
					return recommendations;
				}
			});
			return users;
		}
	}

	public ArrayList<Cart> getLiveOrders(final Database DB, final Categories categories){

		final String ordered = Utils.stringFromStatus(CartStatus.ORDERED);
		final String shipped = Utils.stringFromStatus(CartStatus.SHIPPED);;

		final String query = "MATCH (c:Cart) WHERE c.status = $ordered OR c.status = $shipped "+
							 "RETURN id(c) as id, c.status, c.owner";

		try(Session session = driver.session()){
			ArrayList<Cart> output = session.readTransaction(new TransactionWork<ArrayList<Cart>>(){
				public ArrayList<Cart> execute(Transaction tx){

					ArrayList<Cart> liveOrders = new ArrayList<Cart>();
					Result result = tx.run(query, parameters("ordered", ordered, "shipped", shipped ));

					while(result.hasNext()){
						Record record = result.next();

						int id = record.get("id").asInt();
						String owner = record.get("c.owner").asString();
						String stringStatus = record.get("c.status").asString();
						CartStatus status = Utils.statusFromString(stringStatus);

						Cart cart = new Cart(DB, categories, id, status, owner);
						
						liveOrders.add(cart);
					}

					return liveOrders;

				}
			});
			return output;
		} 

	}
	
	
}
