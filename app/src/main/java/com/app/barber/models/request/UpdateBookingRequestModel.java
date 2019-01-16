package com.app.barber.models.request;

/**
 * Created by harish on 2/1/19.
 */

public class UpdateBookingRequestModel {
    int bookingId;
    boolean Status;
    int BookingType;

    public UpdateBookingRequestModel(int bookingId, boolean Status, int BookingType) {
        this.bookingId = bookingId;
        this.Status = Status;
        this.BookingType = BookingType;

    }
}
