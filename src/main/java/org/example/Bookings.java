package org.example;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bookings {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingTimeline bookingdates;
    private String additionalneeds;

    public Bookings(String firstname, String lastname, int totalprice, boolean depositpaid, String checkin, String checkout, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = new BookingTimeline(checkin, checkout);
        this.additionalneeds = additionalneeds;
    }

    @Data
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingTimeline {
        private String checkin;
        private String checkout;
    }
}