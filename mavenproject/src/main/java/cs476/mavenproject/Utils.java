package cs476.mavenproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cs476.mavenproject.Cart.CartStatus;

public class Utils {
    
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }

    public static CartStatus statusFromString(String status){
		switch(status){
			case"new":
				return CartStatus.NEW;

			case"ordered":
				return CartStatus.ORDERED;

			case"shipped":
				return CartStatus.SHIPPED;

			case"delivered":
				return CartStatus.DELIVERED;
			default:
				return null;
		}
	}

	public static String stringFromStatus( CartStatus status){
		if(status == CartStatus.NEW){
			return "new";

		} else if( status == CartStatus.ORDERED){
			return "ordered";

		} else if( status == CartStatus.SHIPPED){
			return "shipped";
		} else if( status == CartStatus.DELIVERED){
			return "delivered";
		} else {
			return null;
		}
	}

	public static void underlineString(String s){
		for(int i = 0; i < s.length(); i++){
            System.out.print('-');
        }
		System.out.println();
	}

	public static void surroundString(String s){
		underlineString(s);
		System.out.println(s);
		underlineString(s);
		System.out.println();
	}

	public static void printFoundUser(Buyer foundUser, int index){
		String username = foundUser.username();
		String isFollowing = foundUser.isFollowing()
								? " - F"
								: "";

		String userString = index++ + " - " + username + isFollowing;
		System.out.println(userString);
		System.out.println("--");

	}

	public static void printFoundUsersTitle(ArrayList<Buyer> foundUsers){
		String userTittle = foundUsers.size() == 1 ? " User" : " Users";

		String title = "Found: " + foundUsers.size() + userTittle + " | F = Following";

		System.out.println(title);
		underlineString(title);

	}

	//Return true if product is found
	public static boolean printProductsByFarm(Database DB, ArrayList<Product> products, boolean showAll, Buyer mainBuyer){

		final boolean anyProductFound[] = {false};
		Optional<Buyer> buyer = Optional.ofNullable(mainBuyer);

        Map<String,List<Product>> groupByFarm = new HashMap<>();

		groupByFarm =  products.stream()
		.collect(Collectors.groupingBy(Product::farm));

		groupByFarm.forEach((username, productList) -> {

			String farmInventory = "";
			boolean emptyFarm = true;
			
			for(Product p: productList){

				int quanity = buyer.isPresent()
								? mainBuyer.cart.contains(p.identity())
								: 0;

				if(showAll || p.quantity() > 0){
					farmInventory = farmInventory.concat(p.toString(false, quanity) + '\n');
					farmInventory = farmInventory.concat("--\n");
					emptyFarm = false;
					anyProductFound[0] = true;
				}
			}

			if(!emptyFarm){
				
				String caption = (showAll) 
									? "Farm: " + username
									: "Farm: " + DB.getFarmDisplayName(username);

				System.out.println(caption);
				underlineString(caption);
				System.out.println(farmInventory);	
			}

		});

		return anyProductFound[0];
	}	

	public static boolean printCartsByStatus(ArrayList<Cart> liveOrders){

		final boolean liveOrdersHot[] = {false};

        Map<CartStatus,List<Cart>> groupByStatus = new HashMap<>();

		groupByStatus = liveOrders.stream()
		.collect(Collectors.groupingBy(Cart::status));

		groupByStatus.forEach((status, cartList) -> {
			
			
			String caption = "Status: " + stringFromStatus(status);
			System.out.println(caption);
			underlineString(caption);

			int index = 1;
			for(Cart c: cartList){

				if(index++ %5 != 0 && cartList.size() > index){
					System.out.print("|~ ID: " + c.identity());
				} else {
					System.out.println("|~ ID: " + c.identity());
				}

				liveOrdersHot[0] = true;
	
			}

		

		});

		return liveOrdersHot[0];
	}	

	public static HashMap<Integer,Cart> cartArrayToMap (ArrayList<Cart> orders){

		HashMap<Integer, Cart> hashMap = new HashMap<>();

		for (Cart c : orders) {
			hashMap.put(c.identity(), c);
		}
  
		return hashMap;
		}

	public static boolean validUsername(String username){
		String regex = "^[a-z0-9_]\\w{3,19}$";
  
        Pattern p = Pattern.compile(regex);
  
        if (username == null) {
            return false;
        }
		
        Matcher m = p.matcher(username);
  
        return m.matches();

	}

	public static void invalidSelection(String selection){
		String sel = selection + " is an invalid selection!";
		System.out.println(sel);
		underlineString(sel);

		System.out.println();

	}

	public static void printError(String e){
		System.out.println(e);
		underlineString(e);

		System.out.println();

	}
}
