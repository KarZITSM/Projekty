package com.aproteam.flowerpost.model;

import java.util.Objects;

/**
 * This class represents flowers and its identifiers
 */
public class FlowerType extends StoreItem {
	/**
	 * @param id Flowers ID
	 */
	private final int id;
	/**
	 * @param name Flowers name
	 */
	private final String name;
	/**
	 * @param wholesalePrice The price of a single unit sold by the warehouse
	 */
	private final int wholesalePrice;
	/**
	 * @param retailPrice The price of a single unit sold by the flower shops
	 */
	private final int retailPrice;

	/**
	 * A constructor in FlowerType class
	 *
	 * @param id             Flowers ID
	 * @param name           Flowers name
	 * @param wholesalePrice The price of a single unit sold by the warehouse
	 * @param retailPrice    The price of a single unit sold by the flower shops
	 */
	public FlowerType(int id, String name, int wholesalePrice, int retailPrice) {
		this.id = id;
		this.name = name;
		this.wholesalePrice = wholesalePrice;
		this.retailPrice = retailPrice;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getWholesalePrice() {
		return wholesalePrice;
	}

	public int getRetailPrice() {
		return retailPrice;
	}

	public Object[] getFieldValues() {
		return new Object[]{id, name, wholesalePrice + " $", retailPrice + " $"};
	}

	public static String[] getFieldNames() {
		return new String[]{"Id", "Name", "Wholesale Price", "Retail price"};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FlowerType that = (FlowerType) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return name;
	}

}