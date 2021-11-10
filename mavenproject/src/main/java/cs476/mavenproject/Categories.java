package cs476.mavenproject;
import java.util.Hashtable;

//import SubCategory;

public class Categories {

	Hashtable<Integer, SubCategory> vegetable = new Hashtable<Integer, SubCategory>();
	Hashtable<Integer, SubCategory> fruit = new Hashtable<Integer, SubCategory>();
	Hashtable<Integer, SubCategory> herb = new Hashtable<Integer, SubCategory>();

	Category vegetables, fruits, herbs;

	public Categories() {

		// Add all vegetables
		vegetable.put(1, new SubCategory("Lettuce", 100));
		vegetable.put(2, new SubCategory("Spinach", 100));
		vegetable.put(3, new SubCategory("Broccoli", 100));
		vegetable.put(4, new SubCategory("Celery", 100));
		vegetable.put(5, new SubCategory("Cucumber", 100));
		vegetable.put(6, new SubCategory("Avocado", 100));
		vegetable.put(7, new SubCategory("Sprouts", 100));
		vegetable.put(8, new SubCategory("Bell Pepper", 100));

		// Add all fruits
		fruit.put(1, new SubCategory("Strawberries", 100));
		fruit.put(2, new SubCategory("Blackberries", 100));
		fruit.put(3, new SubCategory("Blueberries", 100));
		fruit.put(4, new SubCategory("Melon", 100));
		fruit.put(5, new SubCategory("Mango", 100));
		fruit.put(6, new SubCategory("Pomogranite", 100));
		fruit.put(7, new SubCategory("Red Grapes", 100));
		fruit.put(8, new SubCategory("Green Grapes", 100));
		fruit.put(9, new SubCategory("Red Apple", 100));
		fruit.put(10, new SubCategory("Green Apple", 100));
		fruit.put(11, new SubCategory("Pineapple", 100));

		// Add all herbs
		herb.put(1, new SubCategory("Mint", 100));
		herb.put(2, new SubCategory("Cilantro", 100));
		herb.put(3, new SubCategory("Thyme", 100));
		herb.put(4, new SubCategory("Parsley", 100));
		herb.put(5, new SubCategory("Lemongrass", 100));
		herb.put(6, new SubCategory("Oregano", 100));
		herb.put(7, new SubCategory("Dill", 100));

		vegetables = new Category(vegetable, "Vegetables");
		fruits = new Category(fruit, "Fruits");
		herbs = new Category(herb, "Herbs");

	}

}

class Category {

	private Hashtable<Integer, SubCategory> tabel = new Hashtable<Integer, SubCategory>();
	private String name;

	public Category(Hashtable<Integer, SubCategory> tabel, String name) {
		this.tabel = tabel;
		this.name = name;
	}

	public String name() {
		return name;
	}

	public SubCategory get(int i) {
		return tabel.get(i);
	}

}
