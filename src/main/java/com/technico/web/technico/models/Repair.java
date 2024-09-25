package com.technico.web.technico.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "repair_type", nullable = false)
    private RepairType repairType;

    @Column(name = "short_description", length = 100)
    private String shortDescription;

    @PastOrPresent
//    @NotNull
    @Column(name = "submission_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date submissionDate;

    @Size(max = 400)
    private String description;

    
    @Column(name = "scheduled_start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date scheduledStartDate;

    
    @Column(name = "scheduled_end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date scheduledEndDate;

    @DecimalMin(value = "0.0")
    @Column(name = "proposed_cost")
    private BigDecimal proposedCost;

    @Column(name = "acceptance_status")
    private Boolean acceptanceStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "repair_status")
    private RepairStatus repairStatus;
    
    @Column(name = "repair_address", length = 50)
    private String repairAddress;

    @FutureOrPresent
    @Column(name = "actual_start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date actualStartDate;

    @FutureOrPresent
    @Column(name = "actual_end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date actualEndDate;
    
    @NotNull
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "property_e9", referencedColumnName = "e9", nullable = false)
    private Property property;
}
