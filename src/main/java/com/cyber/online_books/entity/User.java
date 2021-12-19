package com.cyber.online_books.entity;

import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "displayName", unique = true)
    private String displayName;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "notification")
    private String notification;

    @Min(value = 0)
    @Column(name = "gold", precision = 22, scale = 0)
    private Double gold;

    @Column(name = "avatar")
    private String avatar;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;

    @Column(name = "status")
    private Integer status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "userId", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "roleId", nullable = false, updatable = false)})
    private Set< Role > roleList;

    @Transient
    private String passwordRegister;

    @Transient
    private String passwordRegisterConfirm;

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (status == null) {
            status = ConstantsStatusUtils.USER_ACTIVED;
        }
        if (gold == null) {
            gold = (double) 0;
        }
    }
}
