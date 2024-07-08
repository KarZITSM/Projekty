package com.aproteam.flowerpost.model;

import java.util.List;

/**
 * This class represents flower bouquet and its identifiers
 */
public class FlowerBouquet extends StoreItem {
	/**
	 * Flowers that the bouquet can be made out of
	 */
	private final List<FlowerType> flowers;

	/**
	 * The constructor of the FlowerBouquet class
	 *
	 * @param flowers
	 */
	public FlowerBouquet(List<FlowerType> flowers) {
		this.flowers = flowers;
	}

	public List<FlowerType> getFlowers() {
		return flowers;
	}

	/**
	 * A function that calculates the price based on the value of retailPrice variable
	 *
	 * @return This function returns the price of a specific flower type
	 */
	public double calculatePrice() {
		double price = 0;
		for (FlowerType flower : flowers)
			price += flower.getRetailPrice();
		return price;
	}

}