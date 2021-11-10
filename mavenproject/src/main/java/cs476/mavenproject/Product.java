package cs476.mavenproject;
//import SubCategory;
//import cs476.farm_to_table.Categories;
//import cs476.farm_to_table.Farm;

public class Product {

	private String id;
	private Farm farm;
	private Category category;
	private SubCategory subCategory;
	private String name;
	private double price;
	private double weight;
	private int quantityLeft;
	private int quantityWanted = 0;

	// Constructor from Client
	public Product(String id, Farm farm, String name, Category category, SubCategory subCategory, double price,
			int quantityLeft) {
		this.id = id;
		this.farm = farm;
		this.name = name;
		this.category = category;
		this.subCategory = subCategory;
		this.price = price;
		this.weight = subCategory.weight();
		this.quantityLeft = quantityLeft;
	}

	public Product(String id, Farm farm, String name, Category category, SubCategory subCategory, double price,
			int quantityLeft, int quantityWanted) {
		this.id = id;
		this.farm = farm;
		this.name = name;
		this.category = category;
		this.subCategory = subCategory;
		this.price = price;
		this.weight = subCategory.weight();
		this.quantityLeft = quantityLeft;
		this.quantityWanted = quantityWanted;

	}

	// Constructor from DB
	public Product(String id) {

	}

	// Copy constructor
	public Product copy() {

		return new Product(id, farm, name, category, subCategory, price, quantityLeft, quantityWanted);

	}

	public String id() {
		return id;
	}

	public Category category() {
		return category;
	}

	public SubCategory subCategory() {
		return subCategory;
	}

	public double price() {
		return price;
	}

	public double weight() {
		return weight;
	}

	public String name() {
		return name;
	}

	public int quantityLeft() {
		return quantityLeft;
	}

	public int quantityWanted() {

		return quantityWanted;
	}

	public void resetQuantityWanted() {
		quantityWanted = 0;
	}

	public void increaseQuantityWanted(int amount) {

		if (quantityWanted + amount <= quantityLeft) {
			quantityWanted += amount;
		} else {
			System.out.println("Can't set quantity wanted, not enough supply");

		}
	}

	public void decreaseQuantityWanted(int amount) {

		if (quantityWanted - amount > 0) {
			quantityWanted -= amount;
		} else {
			this.resetQuantityWanted();

		}
	}

	public void increaseQuantity(int amount) {
		quantityLeft += amount;
	}

	public void decreaseQuantity(int amount) {

		if (quantityLeft - amount >= 0) {

			quantityLeft -= amount;

		} else {

			System.out.println("Can't remove low supply: " + quantityLeft + " left");
		}

	}

	public String toString(boolean cartView) {

		return cartView

				? name + " | " + category.name() + " - " + subCategory.name() + " | Price: $" + price
						+ " | Serving Size: " + weight + " grams | In Cart: " + quantityWanted

				: name + " | " + category.name() + " - " + subCategory.name() + " | Price: $" + price
						+ " | Serving Size: " + weight + " grams | Quiantity Left: " + quantityLeft;

	}

}
