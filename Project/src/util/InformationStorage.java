package util;

public class InformationStorage {
	private static String typeOfPayment;
	private static String deliveryTime;
	
	public static void setPaymentType(String typeOfPayment){
		InformationStorage.typeOfPayment = typeOfPayment;
	}
	
	public static void setDelivery(String deliveryTime){
		InformationStorage.deliveryTime = deliveryTime;
	}
	
	public static String getPaymentType(){
		return typeOfPayment;
	}
	
	public static String getDelivery(){
		return deliveryTime;
	}
}
