package com.aproteam.flowerpost;

import com.aproteam.flowerpost.database.OrdersTable;
import com.aproteam.flowerpost.exception.OrderNotFoundException;
import com.aproteam.flowerpost.model.Order;
import com.aproteam.flowerpost.model.OrderStatus;
import com.aproteam.flowerpost.model.User;
import com.aproteam.flowerpost.ui.UserInterfaceOfFlowerShopSystem;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PaymentSystemSimulator {

	/**
	 * Simulates payment with BLIK code.
	 *
	 * @param blikCode The BLIK code needed for payment
	 * @return true if the payment was successful, false otherwise
	 */
	public static boolean payWithBLIK(String blikCode) {
		System.out.println("Waiting for Bank system response...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return !blikCode.equals("997997");
	}

	/**
	 * Simulates payment with Bank Transfer.
	 *
	 * @return true if the payment was successful, false otherwise
	 */
	public static boolean payWithBankTransfer() {
		System.out.println("""
				Please make a Bank Transfer for this Bank Account:
				PL 34 1090 2402 6918 9158 1752 5615
				""");
		UserInterfaceOfFlowerShopSystem.printAndWaitForEnter("Press Enter when you have completed the Bank Transfer.");
		System.out.println("Waiting for Bank system response...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Simulates the order status update
	 *
	 * @param order The order needs to be updated
	 */
	public static void simulateOrderStatusChange(Order order) {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(() -> {
			try {
				OrdersTable.updateOrderStatus(order.getUserEmail(), order.getOrderId(), OrderStatus.DELIVERY);
			} catch (OrderNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}, Constants.SIMULATE_ORDER_STATUS_CHANGE_DELAY_IN_SECONDS, TimeUnit.SECONDS);
		//Simulating order status change after 30 seconds:
		service.schedule(() -> {
			try {
				OrdersTable.updateOrderStatus(order.getUserEmail(), order.getOrderId(), OrderStatus.DELIVERY);
			} catch (OrderNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}, 2 * Constants.SIMULATE_ORDER_STATUS_CHANGE_DELAY_IN_SECONDS, TimeUnit.SECONDS);
	}

}