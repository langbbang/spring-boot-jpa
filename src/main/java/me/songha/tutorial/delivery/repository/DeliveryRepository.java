package me.songha.tutorial.delivery.repository;

import me.songha.tutorial.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
