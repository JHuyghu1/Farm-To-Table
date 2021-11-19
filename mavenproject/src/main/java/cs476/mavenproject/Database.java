package cs476.mavenproject;
import java.util.ArrayList;
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
	public void close() throws Exception{
		driver.close();
	}

	public String addBuyertoDatabase(final String username, final String pass, final String address ){
	  
	  final String customer = "CREATE (n: Buyer {n.username: $username, n.password: $pass, n.address: $address} ) RETURN id(n)";
	  

	  
	  try(Session session = driver.session()){
		 String id = session.writeTransaction(new TransactionWork<String>(){
			  public String execute(Transaction tx){
				  Result result = tx.run(customer, parameters("username", username, "password", pass, "address", address));
				  
				  tx.commit();
				  return result.single().get("id").asString();
			  }
		  });
		 return id;
	  }

  }

}
