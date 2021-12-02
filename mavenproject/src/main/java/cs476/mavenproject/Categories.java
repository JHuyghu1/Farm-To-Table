package cs476.mavenproject;
import java.util.Hashtable;

//import SubCategory;

public class Categories {

	Hashtable<Integer, SubCategory> vegetableTable = new Hashtable<Integer, SubCategory>();
	Hashtable<Integer, SubCategory> fruitTable = new Hashtable<Integer, SubCategory>();
	Hashtable<Integer, SubCategory> herbsTable = new Hashtable<Integer, SubCategory>();

	Category vegetables, fruits, herbs;

	public Categories() {

		// Add all vegetables
		vegetableTable.put(1, new SubCategory("Lettuce", 89));
		vegetableTable.put(2, new SubCategory("Spinach", 30));
		vegetableTable.put(3, new SubCategory("Broccoli", 226));
		vegetableTable.put(4, new SubCategory("Celery", 192));
		vegetableTable.put(5, new SubCategory("Cucumber", 104));
		vegetableTable.put(6, new SubCategory("Avocado", 150));
		vegetableTable.put(7, new SubCategory("Sprouts", 93));
		vegetableTable.put(8, new SubCategory("Bell Pepper", 119));

		// Add all fruits
		fruitTable.put(1, new SubCategory("Strawberries", 100));
		fruitTable.put(2, new SubCategory("Blackberries", 100));
		fruitTable.put(3, new SubCategory("Blueberries", 100));
		fruitTable.put(4, new SubCategory("Melon", 161));
		fruitTable.put(5, new SubCategory("Mango", 265));
		fruitTable.put(6, new SubCategory("Pomogranite", 282));
		fruitTable.put(7, new SubCategory("Red Grapes", 151));
		fruitTable.put(8, new SubCategory("Green Grapes", 151));
		fruitTable.put(9, new SubCategory("Red Apple", 169));
		fruitTable.put(10, new SubCategory("Green Apple", 169));
		fruitTable.put(11, new SubCategory("Pineapple", 165));

		// Add all herbs
		herbsTable.put(1, new SubCategory("Mint", 2));
		herbsTable.put(2, new SubCategory("Cilantro", 17));
		herbsTable.put(3, new SubCategory("Thyme", 41));
		herbsTable.put(4, new SubCategory("Parsley", 63));
		herbsTable.put(5, new SubCategory("Lemongrass", 71));
		herbsTable.put(6, new SubCategory("Oregano", 91));
		herbsTable.put(7, new SubCategory("Dill", 151));

		vegetables = new Category(vegetableTable, "Vegetables");
		fruits = new Category(fruitTable, "Fruits");
		herbs = new Category(herbsTable, "Herbs");

}



class Category {

	private Hashtable<Integer, SubCategory> tabel = new Hashtable<Integer, SubCategory>();
	private String name;

	public Category(Hashtable<Integer, SubCategory> tabel, String name) {
		this.tabel = tabel;
		this.name = name;
	}

	public void viewSubCategory(){
		System.out.println(tabel);
	}

	public String name() {
		return name;
	}

	public SubCategory get(int i) {
		return tabel.get(i);
	}

}

}
