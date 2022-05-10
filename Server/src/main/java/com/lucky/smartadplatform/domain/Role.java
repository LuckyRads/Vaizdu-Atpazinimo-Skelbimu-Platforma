package com.lucky.smartadplatform.domain;

import com.lucky.smartadplatform.domain.type.RoleType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;

    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }

}
