package com.app.barber.models.request;

import java.util.List;

/**
 * Created by harish on 31/12/18.
 */

public class RatingRequestModel {
    public int getBarberId() {
        return BarberId;
    }

    public void setBarberId(int barberId) {
        BarberId = barberId;
    }

    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        BookingId = bookingId;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }


    int BarberId;
    int BookingId;
    String Review;

    public List<Ratings> getRatingList() {
        return RatingList;
    }

    public void setRatingList(List<Ratings> ratingList) {
        RatingList = ratingList;
    }

    List<Ratings> RatingList;
    public int getBookingType() {
        return BookingType;
    }

    public void setBookingType(int bookingType) {
        BookingType = bookingType;
    }

    int BookingType;

    public static class Ratings {
        public int getRatingType() {
            return RatingType;
        }

        public void setRatingType(int ratingType) {
            RatingType = ratingType;
        }

        public int getRating() {
            return Rating;
        }

        public void setRating(int rating) {
            Rating = rating;
        }

        int RatingType;//  Expertise = 1,Hygiene = 2, Value = 3,Punctuality = 4

        int Rating;


    }
}
