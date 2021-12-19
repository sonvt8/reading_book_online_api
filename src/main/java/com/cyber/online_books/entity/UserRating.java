package com.cyber.online_books.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_rating")
@Data
@NoArgsConstructor
public class UserRating implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false)),
            @AttributeOverride(name = "storyId", column = @Column(name = "storyId", nullable = false))})
    private UserRatingPK userRatingPK;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;

    @JoinColumn(name = "storyId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Story story;

    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "locationIP", length = 50, nullable = false)
    private String locationIP;

}
