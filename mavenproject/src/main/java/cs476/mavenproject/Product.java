package cs476.mavenproject;

import cs476.mavenproject.Categories.Category;

public class Product {
	private int identity;
	private Category category;
	private SubCategory subCategory;
	private String name;
	private double price;
	private double weight;
	private int quantity;
	private String farm;
	Database DB;

	public Product(Database DB, int identity, String name, Category category, SubCategory subCategory, double price, int quantity, String farm) {
		this.identity = identity;
		this.name = name;
		this.category = category;
		this.subCategory = subCategory;
		this.price = price;
		this.weight = subCategory.weight();
		this.quantity = quantity;
		this.farm = farm;
	}

	public int identity() {
		return identity;
	}

	public String farm() {
		return farm;
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

	public int quantity() {
		return quantity;
	}

	public String toString(boolean cartView, int totalInCart) {

		return cartView

				? "|~ ID: " + identity + " | " + name + " | " + subCategory.name() + " | Price: $" + price
						+ " | Serving Size: " + weight + " grams | In Cart: " + totalInCart

				: "|~ ID: " + identity + " | " +name + " | " + subCategory.name() + " | Price: $" + price
						+ " | Serving Size: " + weight + " grams | Quiantity Left: " + (quantity - totalInCart);

	}

}

