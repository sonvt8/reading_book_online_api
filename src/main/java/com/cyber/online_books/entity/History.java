package com.cyber.online_books.entity;

import com.cyber.online_books.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "history")
@Data
@NoArgsConstructor
public class History implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "locationIP", nullable = false, length = 50)
    private String locationIP;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "dateView", length = 19)
    private Date dateView;

    @Column(name = "status")
    private Integer status;

    @JoinColumn(name = "chapterId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chapter chapter;

    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void prePersist() {
        if (dateView == null) {
            dateView = DateUtils.getCurrentDate();
        }
    }

}
