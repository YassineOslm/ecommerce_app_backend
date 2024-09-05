package eafc.uccle.be.entity;

import eafc.uccle.be.enums.AddressType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "user_address")
@Data
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_address")
    private Long idUserAddress;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "box")
    private String box;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType; // ENUM (billing, shipping, both)

    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;

   }