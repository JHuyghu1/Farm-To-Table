package cs476.mavenproject;
import java.util.ArrayList;
import java.util.Map;

import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class Database{
	private final Driver driver;

  public Database(String uri, String username, String password){
    driver = new Database(uri, AuthTokens.basic(username, password));
  }

  public void addBuyertoDatabase(Buyer user){
	  String customer = "CREATE (n: Buyer {n.id: $user.id, n.username: $user.username, n.password: $user.password, n.address: $user.address} )";
	  Map<String, Buyer> params = new HashMap<>();
	  params.put("id", user.id());
	  params.put("username", user.username());
	  params.put("password", user.password());
	  params.put("address", user.address());
	  

	  try(Session session = driver.session()){
		  session.writeTransaction(new TransactionWork<Buyer>(){
			  public void execute(	Transaction tx){
				  Result result = tx.run(customer, params);
				  tx.commit();
				  Buy
				  return result;
			  }
		  }
		  )
	  }*/

  }

  	/*public Node createBuyerCollection(){
  		try( Transaction tx = graphDG.beginTx() ) {
  			user =
  		}
  	}*/
}
