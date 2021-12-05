package cs476.mavenproject;

import java.io.IOException;

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
			case"New":
				return CartStatus.NEW;

			case"Ordered":
				return CartStatus.ORDERED;

			case"Shipped":
				return CartStatus.SHIPPED;

			case"Delivered":
				return CartStatus.DELIVERED;
			default:
				return null;
		}
	}

	public static String stringFromStatus( CartStatus status){
		if(status == CartStatus.NEW){
			return "New";

		} else if( status == CartStatus.ORDERED){
			return "Ordered";

		} else if( status == CartStatus.SHIPPED){
			return "Shipped";
		} else if( status == CartStatus.DELIVERED){
			return "Delivered";
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


}
