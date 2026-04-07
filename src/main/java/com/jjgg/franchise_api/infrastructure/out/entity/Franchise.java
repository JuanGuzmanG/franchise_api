package com.jjgg.franchise_api.infrastructure.out.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "franchises")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {

    @Id
    @Column("franchise_id")
    private Long id;

    @Column("name")
    private String name;


}

