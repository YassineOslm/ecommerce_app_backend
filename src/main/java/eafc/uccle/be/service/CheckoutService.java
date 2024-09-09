package eafc.uccle.be.service;

import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.dao.ShoppingDetailsRepository;
import eafc.uccle.be.dao.ShoppingOrderRepository;
import eafc.uccle.be.dao.UserRepository;
import eafc.uccle.be.dto.Purchase;
import eafc.uccle.be.dto.ShoppingDetailsDto;
import eafc.uccle.be.entity.Product;
import eafc.uccle.be.entity.ShoppingDetails;
import eafc.uccle.be.entity.ShoppingOrder;
import eafc.uccle.be.entity.UserAddress;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class CheckoutService {

    private final ShoppingOrderRepository shoppingOrderRepository;
    private final ShoppingDetailsRepository shoppingDetailsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CheckoutService(ShoppingOrderRepository shoppingOrderRepository, ShoppingDetailsRepository shoppingDetailsRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.shoppingOrderRepository = shoppingOrderRepository;
        this.shoppingDetailsRepository = shoppingDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @Transactional
    public String placeOrder(Purchase purchase) {

        ShoppingOrder order = new ShoppingOrder();
        order.setTotalPrice(purchase.getOrder().getTotalPrice());
        order.setTotalQuantity(purchase.getOrder().getTotalQuantity());
        order.setBillingAddress(purchase.getOrder().getBillingAddress());
        order.setShippingAddress(purchase.getOrder().getShippingAddress());
        order.setPaymentMethod(purchase.getOrder().getPaymentMethod());
        order.setPaymentStatus(purchase.getOrder().getPaymentStatus());
        order.setDeliveryMethod(purchase.getOrder().getDeliveryMethod());
        order.setDeliveryStatus(purchase.getOrder().getDeliveryStatus());
        order.setOrderDate(new Date());
        order.setUser(userRepository.findById(purchase.getUser().getId()).orElse(null));

        // log.info(order.toString());

        ShoppingOrder savedOrder = shoppingOrderRepository.save(order);

        for (ShoppingDetailsDto item : purchase.getOrderItems()) {
            ShoppingDetails newShoppingDetails = new ShoppingDetails();
            newShoppingDetails.setQuantity(item.getQuantity());
            newShoppingDetails.setUnitPrice(item.getUnitPrice());
            newShoppingDetails.setSubTotalPrice(item.getSubTotalPrice());
            newShoppingDetails.setShoppingOrder(savedOrder);
            newShoppingDetails.setProduct(productRepository.findById((long) item.getIdProduct()).orElse(null));
            shoppingDetailsRepository.save(newShoppingDetails);
        }

        return savedOrder.getId();
    }

}
