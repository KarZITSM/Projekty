package com.aproteam.flowerpost.model;

import com.aproteam.flowerpost.Utils;

import java.util.Date;

/**
 * This class represents placing and order and initiates needed variables
 */
public class Order {

	/**
	 * Number identifying order
	 */
	private final int orderId;
	/**
	 * Date which represents time the order were made
	 */
	private final Date orderDate;
	/**
	 * User that placed the order
	 */
	private final String userEmail;
	/**
	 * Id of flower shop where the order is placed
	 */
	private final int flowerShopId;
	/**
	 * Number of items in this order
	 */
	private final int itemCount;
	/**
	 * Sum of order items' prices
	 */
	private final int orderPriceSum;
	/**
	 * Status of this order
	 */
	private final OrderStatus orderStatus;

	/**
	 * A constructor in Order class
	 *
	 * @param orderId       Number identifying order
	 * @param orderDate     Date which represents time the order were made
	 * @param userEmail     Email of the user that placed the order
	 * @param flowerShopId  Products that are being sold/ordered
	 * @param itemCount     Number of items in this order
	 * @param orderPriceSum Sum of order items' prices
	 * @param orderStatus   Status this order
	 */
	public Order(int orderId, Date orderDate, String userEmail, int flowerShopId, int itemCount, int orderPriceSum, OrderStatus orderStatus) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.userEmail = userEmail;
		this.flowerShopId = flowerShopId;
		this.itemCount = itemCount;
		this.orderPriceSum = orderPriceSum;
		this.orderStatus = orderStatus;
	}

	/**
	 * @return Returns order ID
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 * @return Returns order date
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @return Returns user email
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @return Returns flower shop id
	 */
	public int getFlowerShopId() {
		return flowerShopId;
	}

	/**
	 * @return Number of items in this order
	 */
	public int getItemCount() {
		return itemCount;
	}

	public Object[] getFieldValues() {
		return new Object[]{orderId, Utils.formatDate(orderDate), userEmail, flowerShopId};
	}

	public static String[] getFieldNames() {
		return new String[]{"orderId", "orderDate", "userEmail", "flowerShopId"};
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public int getOrderPriceSum() {
		return orderPriceSum;
	}

	public String getOrderPriceSumAsText() {
		return orderPriceSum + " $";
	}
}