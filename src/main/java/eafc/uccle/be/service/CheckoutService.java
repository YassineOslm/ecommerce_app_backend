package eafc.uccle.be.service;

import eafc.uccle.be.dto.Purchase;
import eafc.uccle.be.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

}
