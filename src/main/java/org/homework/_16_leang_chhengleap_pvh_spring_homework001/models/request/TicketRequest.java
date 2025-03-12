package org.homework._16_leang_chhengleap_pvh_spring_homework001.models.request;

import lombok.Data;

@Data
public class TicketRequest {
    private String passengerName;
    private String travelDate;
    private String sourceStation;
    private String destinationStation;
    private double price;
    private boolean paymentStatus;
    private String ticketStatus;
    private int seatNumber;


    public boolean getPaymentStatus() {
        return paymentStatus;
    }
}
