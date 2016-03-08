package util;

import java.util.ArrayList;
import java.util.List;

import interfaces.PaymentMethod;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PaymentProperty implements ObservableValue<PaymentMethod> {

	private PaymentMethod paymentMethod;
	private List<ChangeListener<? super PaymentMethod>> listeners = new ArrayList<ChangeListener<? super PaymentMethod>>();
	
	public PaymentProperty(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public void setValue(PaymentMethod paymentMethod) {
		PaymentMethod oldValue = this.paymentMethod;
		PaymentMethod newValue = paymentMethod;
		this.paymentMethod = paymentMethod;
		listeners.forEach(consumer -> consumer.changed(this, oldValue, newValue));
	}
	
	/** Do not use, no functionality */
	@Override
	public void addListener(InvalidationListener listener) {
	}
	
	/** Do not use, no functionality */
	@Override
	public void removeListener(InvalidationListener listener) {
	}

	@Override
	public void addListener(ChangeListener<? super PaymentMethod> listener) {
		listeners.add(listener);
	}

	@Override
	public PaymentMethod getValue() {
		return this.paymentMethod;
	}
	
	public PaymentMethod get() {
		return this.paymentMethod;
	}

	@Override
	public void removeListener(ChangeListener<? super PaymentMethod> listener) {
		listeners.remove(listener);
	}

}
