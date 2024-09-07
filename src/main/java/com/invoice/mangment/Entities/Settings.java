package com.invoice.mangment.Entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tax;

    private String settingKey;  // Add this field

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
