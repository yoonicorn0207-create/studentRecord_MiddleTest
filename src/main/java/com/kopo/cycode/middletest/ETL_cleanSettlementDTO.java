package com.kopo.cycode.middletest;

public class ETL_cleanSettlementDTO {
	private String seq; // 정산 데이터 고유 일련번호
	private String storeName; // 정제된 가맹점명
	private int salesAmount; // 실제 매출 금액
	private int commissionFee; // 플랫폼 중개 수수료
	private int finalAmount; // 매장 최종 정산 금액
	private String orderDate; // 정산 기준일 (주문일시)
	private String transferDate; // ETL 데이터 이관 일시

	public ETL_cleanSettlementDTO() {
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(int salesAmount) {
		this.salesAmount = salesAmount;
	}

	public int getCommissionFee() {
		return commissionFee;
	}

	public void setCommissionFee(int commissionFee) {
		this.commissionFee = commissionFee;
	}

	public int getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(int finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
}
