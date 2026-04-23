package com.akillisehir.gezirehberi.entity;

import com.akillisehir.gezirehberi.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating extends BaseEntity {

    private Integer score;

    @ManyToOne
    private User user;

    @ManyToOne
    private Place place;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}