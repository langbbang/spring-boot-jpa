package me.songha.tutorial.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.songha.tutorial.account.dto.AccountDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class) /* @CreatedDate, @LastModifiedDate 사용을 위한 리스너 등록 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    /**
     * description :: @GeneratedValue
     * strategy 속성을 지정해주지 않을 경우 GenerationType.AUTO 가 default 이며,
     * AUTO 는 dialect 값에 따라 기본키 자동 생성 전략이 지정된다.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * description :: @Embedded - @Embeddable
     * address1, address2 각각 주소를 의미하는 컬럼이다.
     * 이를 하나의 객체로 묶는다면 주소라는 의미를 더 명확하게 표현할 수 있을 것이다.
     * Address 클래스에서 @Embeddable 는 생략 가능하다.
     */
    @Embedded
    private Address address;

    /**
     * description :: @AttributeOverride
     * 객체를 이용해 컬럼을 표현하였다면 다음과 같은 예시의 상황에서 컬럼명 중복의 상황이 발생할 수 있다. (e.g. 집주소, 회사주소를 Address 로 표현)
     * 동일한 객체를 사용하면서 컬럼명의 구분이 필요할 때 사용할 수 있다.
     */
//    @Embedded
//    @AttributeOverride(name = "address1", column = @Column(name = "company_address1"))
//    @AttributeOverride(name = "address2", column = @Column(name = "company_address2"))
//    @AttributeOverride(name = "zip", column = @Column(name = "company_zip"))
//    private Address companyAddress;

    @Embedded
    private Email email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Embedded
    private Password password;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_at", insertable = false)
    private ZonedDateTime updatedAt;

    /**
     * entity 클래스 자체에 @Builder 어노테이션을 붙이지 않은 것은,
     * 생성일 및 수정일 등의 날짜 데이터는 데이터베이스에서 자동으로 입력하게 설정하는 편이 좋기 때문이다.
     * 매번 생성할 때 create 시간을 넣어 주고, update 할 때 넣어 주는 등의 반복적인 작업을 줄일 수 있다.
     */
    @Builder
    public Account(Email email, String firstName, String lastName, Password password, Address address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.address = address;
    }

    /**
     * entity 클래스에서 setter 사용은 지양하도록 한다.
     * JPA에서는 영속성이 있는 객체에서 Setter 메서드를 통해서 데이터베이스 DML이 가능하게 된다.
     * 만약 무분별하게 모든 필드에 대한 Setter 메서드를 작성했을 경우 email 변경 기능이 없는 기획 의도가 있더라도
     * 영속성이 있는 상태에서 Setter 메서드를 사용해서 얼마든지 변경이 가능해지는 구조를 갖게 된다.
     * 또 굳이 변경 기능이 없는 속성뿐만이 아니라 영속성만 있으면 언제든지 DML이 가능한 구조는 안전하지 않다.
     * 또 데이터 변경이 발생했을 시 추적할 포인트들도 많아진다.
     * DTO 클래스를 기준으로 데이터 변경이 이루어진다면 명확한 요구사항에 의해서 변경된다.
     */
    public void updateMyAccount(AccountDto.MyAccountReq dto) {
        this.address = dto.getAddress();
    }
}
