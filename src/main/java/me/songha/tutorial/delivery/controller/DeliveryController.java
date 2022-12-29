package me.songha.tutorial.delivery.controller;

import lombok.AllArgsConstructor;
import me.songha.tutorial.delivery.dto.DeliveryDto;
import me.songha.tutorial.delivery.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("deliveries")
@AllArgsConstructor
public class DeliveryController {
    private DeliveryService deliveryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public DeliveryDto.Res create(@RequestBody @Valid final DeliveryDto.CreationReq dto) {
        return new DeliveryDto.Res(deliveryService.create(dto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public DeliveryDto.Res getDelivery(@PathVariable final long id) {
        return new DeliveryDto.Res(deliveryService.findById(id));
    }

    @PostMapping("/{id}/logs")
    @ResponseStatus(value = HttpStatus.OK)
    public DeliveryDto.Res updateDeliveryStatus(@PathVariable final long id, @RequestBody DeliveryDto.UpdateReq dto) {
        return new DeliveryDto.Res(deliveryService.updateStatus(id, dto));
    }

}
