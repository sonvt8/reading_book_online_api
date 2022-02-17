package com.cyber.online_books.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class UserRatingPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "userId", nullable = false)
    private long userId;

    @Column(name = "storyId", nullable = false)
    private long storyId;

}
