package com.aproteam.flowerpost.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represent flower shops and its identifiers
 */
public class FlowerShop {
	/**
	 * An ID that represents the flower shop
	 */
	private final int id;
	/**
	 * A name of a flower shop
	 */
	private final String name;
	/**
	 * The address of a flower shop
	 */
	private final String address;
	/**
	 * Flowers sold in a flower shop
	 */
	private final Map<FlowerType, Integer> flowers;

	/**
	 * A constructor in a FlowerShop class
	 *
	 * @param id
	 * @param name
	 * @param address
	 */
	public FlowerShop(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.flowers = new HashMap<>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public Map<FlowerType, Integer> getFlowers() {
		return flowers;
	}

	/**
	 * A function that add flowers to the stock of a flower shop
	 *
	 * @param flowerType      The type of flower that is added
	 * @param amountofflowers The amount of a specific flower that is to be added
	 */
	public void addFlowers(FlowerType flowerType, int amountofflowers) {
		if (flowers.containsKey(flowerType)) {
			flowers.put(flowerType, flowers.get(flowerType) + amountofflowers);
		} else {
			flowers.put(flowerType, amountofflowers);
		}
	}

	/**
	 * A function that subtracts flowers from the stock of a flower shop
	 *
	 * @param flowerType      The type of flower that is subtracted
	 * @param amountofflowers The amount of a specific flower that is to be subtracted
	 */
	public void takeFlowers(FlowerType flowerType, int amountofflowers) {
		if (!flowers.containsKey(flowerType)) {
			throw new IllegalArgumentException("Flower type not found in flower post");
		}

		if (flowers.get(flowerType) < amountofflowers) {
			throw new IllegalArgumentException("Not enough flowers of this type in flower post");
		}

		flowers.put(flowerType, flowers.get(flowerType) - amountofflowers);
	}

	public Object[] getFieldValues() {
		return new Object[]{id, name, address};
	}

	public static String[] getFieldNames() {
		return new String[]{"id", "name", "address"};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FlowerShop that = (FlowerShop) o;
		return id == that.id && Objects.equals(name, that.name) && Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address);
	}

}