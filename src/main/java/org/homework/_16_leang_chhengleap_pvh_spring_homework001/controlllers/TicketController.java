package org.homework._16_leang_chhengleap_pvh_spring_homework001.controlllers;

import io.swagger.v3.oas.annotations.Operation;
import org.homework._16_leang_chhengleap_pvh_spring_homework001.models.entity.Ticket;
import org.homework._16_leang_chhengleap_pvh_spring_homework001.models.request.TicketRequest;
import org.homework._16_leang_chhengleap_pvh_spring_homework001.models.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private List<Ticket> tickets = new ArrayList<>();
    private int count =5;
    AtomicInteger atomicInteger = new AtomicInteger(11);

    public TicketController(){
        tickets.add(new Ticket(1,"Jisoo", "21-01-25", "SR", "BTB", 15, false, "BOOKED", 5));
        tickets.add(new Ticket(2,"Jennie", "11-02-25", "PVH", "KP", 20, false,"BOOKED" , 9));
        tickets.add(new Ticket(3,"Lisa", "15-02-25", "KP", "SR", 8, true, "BOOKED", 2));
        tickets.add(new Ticket(4,"Rosé", "02-03-25", "BMC", "KPC", 12, true, "BOOKED", 6));
        tickets.add(new Ticket(5,"Iren", "11-03-25", "BMC", "KPT", 15, true, "BOOKED", 7));
        tickets.add(new Ticket(6,"Seugi", "21-01-25", "SR", "BTB", 15, false,"BOOKED" , 10));
        tickets.add(new Ticket(7,"Jiwon", "11-02-25", "PVH", "KP", 20, false, "BOOKED", 8));
        tickets.add(new Ticket(8,"Ruonan", "15-02-25", "KP", "SR", 8, true, "BOOKED", 3));
        tickets.add(new Ticket(9,"Alice", "02-03-25", "BMC", "KPC", 12, true, "BOOKED", 4));
        tickets.add(new Ticket(10,"Yuqi", "11-03-25", "BMC", "KPT", 15, true,"BOOKED" , 1));
    }


    @PostMapping
    @Operation(summary = "Create a new ticket")
    public List<Ticket> createTicket(@RequestBody TicketRequest ticketRequest){
        tickets.add(new Ticket(atomicInteger.getAndIncrement(), ticketRequest.getPassengerName(), ticketRequest.getTravelDate(), ticketRequest.getSourceStation(), ticketRequest.getDestinationStation(), ticketRequest.getPrice(), ticketRequest.getPaymentStatus(), ticketRequest.getTicketStatus(), ticketRequest.getSeatNumber()));
        return tickets;
    }

    @GetMapping
    @Operation(summary = "Get all tickets")
    public ResponseEntity<ApiResponse<List<Ticket>>> getTickets(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "3") int size) {
        int currentPage = page - 1;
        int start = currentPage * size;
        int end = Math.min(start + size, tickets.size());

        List<Ticket> paginateTickets = tickets.subList(start, end);

        ApiResponse<List<Ticket>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("All tickets retrieved successfully");
        response.setStatus(HttpStatus.OK);
        response.setPayload(paginateTickets);
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get a ticket by ID")
    public ResponseEntity<ApiResponse<List<Ticket>>> retrieveTicket(@PathVariable("id") int id) {
        List<Ticket> ticketList = new ArrayList<>();

        for (Ticket t : tickets) {
            if (t.getTicketId() == id) {
                ticketList.add(t);
            }
        }

        ApiResponse<List<Ticket>> response;

        if (!ticketList.isEmpty()) {
            response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Ticket(s) found");
            response.setStatus(HttpStatus.OK);
            response.setPayload(ticketList);
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.ok(response);
        } else {
            response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("No ticket found with ID: " + id);
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setPayload(null);
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping("/search")
    @Operation(summary = "Search tickets by passenger name")
    public ResponseEntity<ApiResponse<List<Ticket>>> searchByName(@RequestParam String name) {
        ArrayList<Ticket> ticketList = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getPassengerName().equalsIgnoreCase(name)) {
                ticketList.add(t);
            }
        }

        ApiResponse<List<Ticket>> response = new ApiResponse<>();
        response.setTimestamp(LocalDateTime.now());

        if (ticketList.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("No tickets found for passenger name: " + name);
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setSuccess(true);
        response.setMessage("Tickets found for passenger name: " + name);
        response.setStatus(HttpStatus.OK);
        response.setPayload(ticketList);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/filter")
    @Operation(summary = "Filter tickets by status and travel date")
    public ResponseEntity<ApiResponse<List<Ticket>>> filter(@RequestParam String date, @RequestParam TicketStatus status) {
        List<Ticket> ticketList = new ArrayList<>();

        for (Ticket t : tickets) {
            if (t.getTicketStatus().equalsIgnoreCase(String.valueOf(status)) && t.getTravelDate().equalsIgnoreCase(date)) {
                ticketList.add(t);
            }
        }

        ApiResponse<List<Ticket>> response = new ApiResponse<>();
        response.setTimestamp(LocalDateTime.now());

        if (ticketList.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("No tickets found with the given status and date");
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setSuccess(true);
        response.setMessage("Tickets filtered successfully");
        response.setStatus(HttpStatus.OK);
        response.setPayload(ticketList);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{ticket-id}")
    @Operation(summary = "Update an existing ticket by ID")
    public ResponseEntity<ApiResponse<Ticket>> updateTicket(@PathVariable("ticket-id") int id,
                                                            @RequestBody TicketRequest ticketRequest) {
        ApiResponse<Ticket> response = new ApiResponse<>();
        response.setTimestamp(LocalDateTime.now());

        if (tickets.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("No tickets found");
            response.setStatus(HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        for (Ticket t : tickets) {
            if (t.getTicketId() == id) {
                t.setPassengerName(ticketRequest.getPassengerName());
                t.setTravelDate(ticketRequest.getTravelDate());
                t.setSourceStation(ticketRequest.getSourceStation());
                t.setDestinationStation(ticketRequest.getDestinationStation());
                t.setPrice(ticketRequest.getPrice());
                t.setPaymentStatus(ticketRequest.getPaymentStatus());
                t.setTicketStatus(ticketRequest.getTicketStatus());
                t.setSeatNumber(ticketRequest.getSeatNumber());

                response.setSuccess(true);
                response.setMessage("Ticket updated successfully");
                response.setStatus(HttpStatus.OK);
                response.setPayload(t);

                return ResponseEntity.ok(response);
            }
        }

        response.setSuccess(false);
        response.setMessage("Ticket not found with ID: " + id);
        response.setStatus(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @DeleteMapping("/{ticket-id}")
    @Operation(summary = "Delete a ticket by ID")
    public List<Ticket> deleteEmployee(@RequestParam int id) {
        for (Ticket t : tickets) {
            if (t.getTicketId() == id) {
                tickets.remove(t);
                return tickets;
            }
        }
        return null;
    }



    @PutMapping
    @Operation(summary = "Bulk update payment status for multiple tickets")
    public ResponseEntity<ApiResponse<List<Ticket>>> bulkTicket(@RequestParam List<Integer> ids,
                                                                @RequestBody TicketRequest ticketRequest) {
        List<Ticket> updatedTickets = new ArrayList<>();
        ApiResponse<List<Ticket>> response = new ApiResponse<>();
        response.setTimestamp(LocalDateTime.now());

        for (Ticket t : tickets) {
            if (ids.contains(t.getTicketId())) {
                t.setPassengerName(ticketRequest.getPassengerName());
                t.setTravelDate(ticketRequest.getTravelDate());
                t.setSourceStation(ticketRequest.getSourceStation());
                t.setDestinationStation(ticketRequest.getDestinationStation());
                t.setPrice(ticketRequest.getPrice());
                t.setPaymentStatus(ticketRequest.getPaymentStatus());
                t.setTicketStatus(ticketRequest.getTicketStatus());
                t.setSeatNumber(ticketRequest.getSeatNumber());
                updatedTickets.add(t);
            }
        }

        if (updatedTickets.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("No matching tickets found to update.");
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setSuccess(true);
        response.setMessage("Payment status updated successfully for selected tickets.");
        response.setStatus(HttpStatus.OK);
        response.setPayload(updatedTickets);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create tickets")
    public ResponseEntity<ApiResponse<List<Ticket>>> bulkTicket(@RequestBody List<TicketRequest> ticketRequests) {
        ApiResponse<List<Ticket>> response = new ApiResponse<>();
        List<Ticket> createdTickets = new ArrayList<>();

        for (TicketRequest ticketRequest : ticketRequests) {
            Ticket newTicket = new Ticket(
                    atomicInteger.getAndIncrement(),
                    ticketRequest.getPassengerName(),
                    ticketRequest.getTravelDate(),
                    ticketRequest.getSourceStation(),
                    ticketRequest.getDestinationStation(),
                    ticketRequest.getPrice(),
                    ticketRequest.getPaymentStatus(),
                    ticketRequest.getTicketStatus(),
                    ticketRequest.getSeatNumber()
            );
            tickets.add(newTicket);
            createdTickets.add(newTicket);
        }

        response.setTimestamp(LocalDateTime.now());
        response.setSuccess(true);
        response.setMessage("Bulk ticket creation successful.");
        response.setStatus(HttpStatus.CREATED);
        response.setPayload(createdTickets);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}