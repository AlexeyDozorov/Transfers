package com.example.cotransfers.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "transfers")
@NoArgsConstructor
@Getter
@Setter
public class Transfer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "transfer_time")
    private String transferTime;
    @Column(name = "transfer_date")
    private String transferDate;


    @Column(name = "adults_amount")
    private Integer adultsAmount;

    @Column(name = "children_under5")
    private int childrenUnder5;
    @Column(name = "children_above5")
    private int childrenAbove5;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "end_location")
    private String endLocation;

    @Column(name = "is_ended")
    private Boolean isEnded;
    @Column(name = "auto_type")
    private String carType;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_updated")
    private Boolean isUpdated;

    @Column(name = "is_pick_up_from_airport")
    private Boolean isPickUpFromAirport;

    @Column(name = "is_shared")
    private Boolean isShared;

    @JsonManagedReference
    @ManyToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<User> users;

}
