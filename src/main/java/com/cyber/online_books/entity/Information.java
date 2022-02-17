package com.cyber.online_books.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "information")
@Data
@NoArgsConstructor
public class Information implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "introduce", columnDefinition = "TEXT")
    private String introduce;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "phone", length = 13)
    private String phone;

    @Column(name = "skype", length = 50)
    private String skype;

    @Column(name = "logo", nullable = false, length = 150)
    private String logo;

    @Column(name = "favicon", nullable = false, length = 150)
    private String favicon;
}
