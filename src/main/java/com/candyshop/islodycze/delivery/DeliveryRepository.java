package com.candyshop.islodycze.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Integer> {

    DeliveryEntity findByOrderOrderId(final Long orderId);

}
