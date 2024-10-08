package com.technico.web.technico.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 20, message = "E9 must contain 20 characters.")
    @NotNull
    @Column(unique = true)
    private String e9;

    @Column(name = "property_address", length = 50)
    private String propertyAddress;

    @Digits(integer = 4, fraction = 0)
    @Column(name = "construction_year")
    private int constructionYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;

    @NotNull
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "owner_vat", referencedColumnName = "vat", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Repair> repairs;
}
