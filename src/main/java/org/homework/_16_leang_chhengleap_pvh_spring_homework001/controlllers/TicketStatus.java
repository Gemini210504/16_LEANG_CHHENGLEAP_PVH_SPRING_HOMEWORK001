package org.homework._16_leang_chhengleap_pvh_spring_homework001.controlllers;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum TicketStatus {
    BOOKED,
    CANCELED,
    COMPLETED
}
