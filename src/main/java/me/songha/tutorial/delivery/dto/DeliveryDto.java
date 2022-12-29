package me.songha.tutorial.delivery.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.songha.tutorial.account.domain.Address;
import me.songha.tutorial.delivery.domain.Delivery;
import me.songha.tutorial.delivery.domain.DeliveryLog;
import me.songha.tutorial.delivery.domain.DeliveryStatus;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryDto {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class CreationReq {
        @Valid
        private Address address;

        @Builder
        public CreationReq(Address address) {
            this.address = address;
        }

        public Delivery toEntity() {
            return Delivery.builder()
                    .address(address)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateReq {
        private DeliveryStatus status;

        @Builder
        public UpdateReq(DeliveryStatus status) {
            this.status = status;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private Address address;
        private List<LogRes> logs;

        public Res(final Delivery delivery) {
            this.address = delivery.getAddress();
            this.logs = delivery.getLogs()
                    .parallelStream().map(LogRes::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class LogRes {
        private DeliveryStatus status;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public LogRes(DeliveryLog log) {
            this.status = log.getStatus();
            this.createdAt = log.getCreatedAt();
            this.updatedAt = log.getUpdatedAt();
        }
    }
}
