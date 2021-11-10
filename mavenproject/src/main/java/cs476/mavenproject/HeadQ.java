package cs476.mavenproject;
import java.util.ArrayList;

public class HeadQ {

	private ArrayList<Farm> inventory = new ArrayList<Farm>();

	public HeadQ() {

	}

	public void addNewFarm(Farm farm) {

		if (!inventory.contains(farm)) {
			inventory.add(farm);
		} else {
			System.out.println("Farm already exists! \n");
		}
	}

	public ArrayList<Farm> inventory() {
		return inventory;
	}

	public void viewInventory() {
		if (inventory.size() > 0) {
			for (Farm farm : inventory) {
				farm.view();
				System.out.println("");
			}
		} else {

			System.out.println("Farm to Table has no iventory! \n");

		}

	}

}
