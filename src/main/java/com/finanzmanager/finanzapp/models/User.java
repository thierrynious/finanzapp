package com.finanzmanager.finanzapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username darf nicht leer sein.")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Ungültige E-Mail-Adresse.")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein.")
    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
