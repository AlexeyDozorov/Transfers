package com.example.cotransfers.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "arrival_date")
    private String arrivalDate;
    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "flight_number")
    private  String flightNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "passport")
    private String passport;

    @Column(name = "telegram_login")
    private String telegramLogin;

    @Column(name = "trip_comment")
    private String tripComment;

    @Column(name = "identification_number")
    private Long identificationNumber;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinTable(
            name = "transfer_user",
            joinColumns = @JoinColumn(name = "transfer_id_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id_id")
    )
    private List<Transfer> transfer;
}
