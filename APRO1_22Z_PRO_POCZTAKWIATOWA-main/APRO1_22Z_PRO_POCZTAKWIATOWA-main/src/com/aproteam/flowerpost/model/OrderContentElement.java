package com.aproteam.flowerpost.model;

/**
 * This class represents a single order element
 */
public class OrderContentElement {
	/**
	 * An ID of the order element
	 */
	private final int id;
	private final StoreItem storeItem;
	/**
	 * The quantity of the order element that is currently in users order
	 */
	private final int quantity;
	/**
	 * The retail price of the order element
	 */
	private final int totalRetailPrice;

	/**
	 * A constructor of OrderContentElement class
	 *
	 * @param id
	 * @param storeItem
	 * @param quantity
	 * @param totalRetailPrice
	 */
	public OrderContentElement(int id, StoreItem storeItem, int quantity, int totalRetailPrice) {
		this.id = id;
		this.storeItem = storeItem;
		this.quantity = quantity;
		this.totalRetailPrice = totalRetailPrice;
	}

	public int getId() {
		return id;
	}

	public StoreItem getStoreItem() {
		return storeItem;
	}

	public Object[] getFieldValues() {
		return new Object[]{id, storeItem, quantity};
	}

	public static String[] getFieldNames() {
		return new String[]{"id", "storeItem", "quantity"};
	}

	public int getQuantity() {
		return quantity;
	}

	public int getTotalRetailPrice() {
		return totalRetailPrice;
	}
}