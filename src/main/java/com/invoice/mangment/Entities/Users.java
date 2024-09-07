package com.invoice.mangment.Entities;


import com.invoice.mangment.enums.Privilege;
import com.invoice.mangment.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles;

    @ElementCollection(targetClass = Privilege.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_privileges", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "privilege")
    private Set<Privilege> privileges;
}
