package me.songha.tutorial.delivery.domain;

import me.songha.tutorial.delivery.exception.DeliveryAlreadyDeliveringException;
import me.songha.tutorial.delivery.exception.DeliveryStatusEqaulsException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "delivery_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * EnumType.ORDINAL : enum 순서 값을 DB에 저장
     * EnumType.STRING : enum 이름을 DB에 저장
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private DeliveryStatus status;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_at", insertable = false)
    private ZonedDateTime updatedAt;


    @Transient
    private DeliveryStatus lastStatus;

    /**
     * JsonProperty.Access.WRITE_ONLY : 역직렬화, JSON -> Java Object 할 때만 접근 허용, @JsonIgnore를 사용할 수 없는 상황에서 쓰임
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    /**
     * Delivery 와는 N:1 관계로 양방향 매핑을 적용하였다. 연관관계의 주인은 Delivery.
     * DeliveryLog 는 Delivery 에 종속되어 있다.
     */
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false, updatable = false)
    private Delivery delivery;

    /**
     * 객체를 바꿀 수 있는것은 자기 자신이라는 원칙 아래 DeliveryLog 생성 시 값 설정을 해당 도메인 안에서 주도한다.
     */
    @Builder
    public DeliveryLog(final DeliveryStatus status, final Delivery delivery) {
        verifyStatus(status, delivery);
        setStatus(status);
        this.delivery = delivery;
    }

    /**
     * 최신 배송 상태가 현재 상태와 같거나 이미 완료되었을 경우 예외 처리
     */
    private void verifyStatus(DeliveryStatus status, Delivery delivery) {
        if (!delivery.getLogs().isEmpty()) {
            lastStatus = getLastStatus(delivery);
            verifyLastStatusEquals(status);
            verifyAlreadyCompleted();
        }
    }

    /**
     * 기록되어 있는 최신 배송 상태 조회
     */
    private DeliveryStatus getLastStatus(Delivery delivery) {
        final int lastIndex = delivery.getLogs().size() - 1;
        return delivery.getLogs().get(lastIndex).getStatus();
    }

    private void setStatus(final DeliveryStatus status) {
        switch (status) {
            case DELIVERING:
                delivering();
                break;
            case COMPLETED:
                completed();
                break;
            case CANCELED:
                cancel();
                break;
            case PENDING:
                pending();
                break;
            default:
                throw new IllegalArgumentException(status.name() + " is not found");
        }
    }


    private void pending() {
        this.status = DeliveryStatus.PENDING;
    }

    private void cancel() {
        verifyNotYetDelivering();
        this.status = DeliveryStatus.CANCELED;
    }

    private void completed() {
        this.status = DeliveryStatus.COMPLETED;
    }

    private void delivering() {
        this.status = DeliveryStatus.DELIVERING;
    }

    private void verifyNotYetDelivering() {
        if (isNotYetDelivering()) throw new DeliveryAlreadyDeliveringException();
    }

    private boolean isNotYetDelivering() {
        return this.lastStatus != DeliveryStatus.PENDING ;
    }

    private void verifyAlreadyCompleted() {
        if (isCompleted())
            throw new IllegalArgumentException("It has already been completed and can not be changed.");
    }

    /**
     * 현재 기록하고자 하는 배송 상태와 기록되어있는 최신 배송상태가 같을 경우 예외 처리
     */
    private void verifyLastStatusEquals(DeliveryStatus status) {
        if (lastStatus == status) throw new DeliveryStatusEqaulsException(lastStatus);
    }

    private boolean isCompleted() {
        return lastStatus == DeliveryStatus.COMPLETED;
    }

}
