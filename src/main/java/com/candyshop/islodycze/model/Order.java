package com.candyshop.islodycze.model;

import com.candyshop.islodycze.exceptions.NotEnoughLoyaltyPointsException;
import com.candyshop.islodycze.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "orders")
@ToString
@DynamicInsert
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private BigDecimal totalCost;

    private LocalDateTime orderDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private boolean isDiscount;

    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private UserEntity userIdFk;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ProductOrder> productOrder;

    public void addLoyaltyPoints() {
        userIdFk.setLoyaltyPoints(userIdFk.getLoyaltyPoints() + (int) totalCost.doubleValue() / 10);
    }

    public void removeLoyaltyPoints(final int loyaltyPoints) {
        if (userIdFk.getLoyaltyPoints() >= 10) {
            userIdFk.setLoyaltyPoints(userIdFk.getLoyaltyPoints() - loyaltyPoints);
        } else {
            throw new NotEnoughLoyaltyPointsException("You have to have at least 10 loyalty points.");
        }
    }
}
