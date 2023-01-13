package com.candyshop.islodycze.loyalty_points;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Service
public class LoyaltyPointsService {

    public static BigDecimal resolveDiscount(final Double oldPrice, final boolean discount) {
        if (discount) {
            return BigDecimal.valueOf(oldPrice * 0.95);
        }
        return BigDecimal.valueOf(oldPrice);
    }

    public static BigDecimal resolveDiscount(final Double oldPrice) {

        return BigDecimal.valueOf(oldPrice * 0.95);
    }
}
