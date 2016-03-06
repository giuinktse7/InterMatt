package util;

public class InformationStorage {
	private static String typeOfPayment;
	private static String deliveryTime;
	private static String purchaser_first_name; // Store first name here since user may have chosen to not save credentials

	public static void setPaymentType(String typeOfPayment) {
		InformationStorage.typeOfPayment = typeOfPayment;
	}

	public static void setDelivery(String deliveryTime) {
		InformationStorage.deliveryTime = deliveryTime;
	}

	public static void setFirtsName(String name) {
		purchaser_first_name = name;
	}

	public static String getPaymentType() {
		return typeOfPayment;
	}

	public static String getDelivery() {
		return deliveryTime;
	}

	public static String getFirstName() {
		return purchaser_first_name;
	}

}