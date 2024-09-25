package com.technico.web.technico.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.io.Serializable;
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
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 9, max = 9)
    @Column(nullable = false, unique = true)
    private String vat;

    @Size(min = 1, max = 50)
    @NotNull
    private String name;

    @Size(min = 1, max = 50)
    @NotNull
    private String surname;

    @Size(max = 50)
    private String address;

    @Column(name = "phone_number", length = 14)
    private String phoneNumber;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;
    
    @Size(min = 8, max = 50)
    @NotNull
    private String password;

    @NotNull
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Property> propertyList;
}
