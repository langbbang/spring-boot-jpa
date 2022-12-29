package me.songha.tutorial.delivery.exception;

import me.songha.tutorial.delivery.domain.DeliveryStatus;

public class DeliveryStatusEqaulsException extends RuntimeException {
    private DeliveryStatus status;

    public DeliveryStatusEqaulsException(DeliveryStatus status) {
        super(status.name() + " It can not be changed to the same state.");
        this.status = status;
    }
}
