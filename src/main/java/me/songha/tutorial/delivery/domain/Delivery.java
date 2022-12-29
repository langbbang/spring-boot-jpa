package me.songha.tutorial.delivery.domain;

import lombok.*;
import me.songha.tutorial.account.domain.Address;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Delivery {

    /**
     * GenerationType.AUTO 설정은 dialect가 H2인 경우 경우 sequence 전략을 사용하는듯하다.
     * Hibernate:
     * call next value for hibernate_sequence
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private Address address;

    /**
     * CascadeType.PERSIST 설정을 주면 Delivery에서 DeliveryLog를 저장시킬 수 있다.
     * 이 때 ArrayList 형으로 지정돼 있다면 add 함수를 통해서 쉽게 저장할 수 있다.
     * 또한 FetchType.EAGER 통해서 모든 로그 정보를 가져오고 있다.
     * 로그 정보가 수십 개 이상일 경우는 Lazy 로딩을 통해서 가져오는 것이 좋지만,
     * 3~4개 정도로 가정했을 경우 FetchType.EAGER로 나쁘지 않다고 생각한다.
     */
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DeliveryLog> logs = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_at", insertable = false)
    private ZonedDateTime updatedAt;


    @Builder
    public Delivery(Address address) {
        this.address = address;
    }


    public void addLog(DeliveryStatus status) {
        this.logs.add(buildLog(status));
    }

    private DeliveryLog buildLog(DeliveryStatus status) {
        return DeliveryLog.builder()
                .status(status)
                .delivery(this)
                .build();
    }
}
