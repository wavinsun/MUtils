package cn.o.app.core.sort;

/**
 * Order item who has {@link Order}
 */
public class OrderItem {

	protected Order mOrder = Order.ASC;

	public Order getOrder() {
		return mOrder;
	}

	public void setOrder(Order order) {
		mOrder = order;
	}

}
