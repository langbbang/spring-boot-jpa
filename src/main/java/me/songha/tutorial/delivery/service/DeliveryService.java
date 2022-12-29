package me.songha.tutorial.delivery.service;

import lombok.AllArgsConstructor;
import me.songha.tutorial.delivery.domain.Delivery;
import me.songha.tutorial.delivery.dto.DeliveryDto;
import me.songha.tutorial.delivery.domain.DeliveryStatus;
import me.songha.tutorial.delivery.exception.DeliveryNotFoundException;
import me.songha.tutorial.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class DeliveryService {
    private DeliveryRepository deliveryRepository;

    public Delivery create(DeliveryDto.CreationReq dto) {
        final Delivery delivery = dto.toEntity();
        delivery.addLog(DeliveryStatus.PENDING);
        return deliveryRepository.save(delivery);
    }

    public Delivery updateStatus(long id, DeliveryDto.UpdateReq dto) {
        final Delivery delivery = findById(id);
        delivery.addLog(dto.getStatus());
        return delivery;
    }

    public Delivery findById(long id) {
        final Optional<Delivery> delivery = deliveryRepository.findById(id);
        delivery.orElseThrow(() -> new DeliveryNotFoundException(id));
        return delivery.get();
    }

    public Delivery removeLogs(long id) {
        final Delivery delivery = findById(id);
        delivery.getLogs().clear();
        return delivery;
    }

    public void remove(long id) {
        deliveryRepository.deleteById(id);
    }

}
