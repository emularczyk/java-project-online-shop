package com.candyshop.islodycze.delivery;

enum DeliveryMode {
    COURIER(10.00),
    INPOST(5.00),
    PERSONAL(0.00);

    public final double price;

    DeliveryMode(double price) {
        this.price = price;
    }
}
