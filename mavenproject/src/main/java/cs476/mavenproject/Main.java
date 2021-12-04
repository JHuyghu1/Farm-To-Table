package cs476.mavenproject;
import java.util.Scanner;



public class Main {

	public static void main(String[] args) {

		//Setup Database 
		Database DB = new Database("neo4j+s://c612ba03.databases.neo4j.io", "neo4j", "zWYkD4TNiLSyg2kbQpJneBESCwOnNnwVVVL79M32sFw");

		//Start Scanner
		Scanner input = new Scanner(System.in);

		//Initialize App Driver
		AppDriver driver = new AppDriver(DB, input);

		Utils.clearConsole();

		driver.login();
		
	}

}
