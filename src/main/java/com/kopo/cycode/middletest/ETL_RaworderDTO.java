package com.kopo.cycode.middletest;

public class ETL_RaworderDTO {
	private String orderId;
	private String productId;
	private int qty;
	private String status; // 주문상태
	private String createdAt; // 주문발생일시

	public ETL_RaworderDTO() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	// Alias methods for binary compatibility with old ETLDataService.class
	public String getOrderDate() {
		return this.status;
	}

	public void setOrderDate(String orderDate) {
		this.status = orderDate;
	}
}
