package com.lucky.smartadplatform.infrastructure.model.jpa;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class JpaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String contactNumber;

    @OneToMany(cascade = { CascadeType.MERGE }, mappedBy = "item", orphanRemoval = true)
    private List<JpaImage> images;

    @NotNull
    @ManyToOne
    @JoinColumn
    private JpaUser owner;

    @NotNull
    @ManyToOne
    private JpaCategory category;

}
