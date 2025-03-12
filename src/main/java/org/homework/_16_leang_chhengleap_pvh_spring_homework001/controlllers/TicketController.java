package org.homework._16_leang_chhengleap_pvh_spring_homework001.controlllers;

import io.swagger.v3.oas.annotations.Operation;
import org.homework._16_leang_chhengleap_pvh_spring_homework001.models.entity.Ticket;
import org.homework._16_leang_chhengleap_pvh_spring_homework001.models.request.TicketRequest;
import org.homework._16_leang_chhengleap_pvh_spring_homework001.models.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/tickets/")
public class TicketController {
    private List<Ticket> tickets = new ArrayList<>();
    private int count =5;
    AtomicInteger atomicInteger = new AtomicInteger(6);

    public TicketController(){
        tickets.add(new Ticket(1,"Jisoo", "21-01-25", "SR", "BTB", 15, false, "BOOKED", 1));
        tickets.add(new Ticket(2,"Jennie", "11-02-25", "PVH", "KP", 20, false, "BOOKED", 1));
        tickets.add(new Ticket(3,"Lisa", "15-02-25", "KP", "SR", 8, true, "BOOKED", 1));
        tickets.add(new Ticket(4,"Rosé", "02-03-25", "BMC", "KPC", 12, true, "BOOKED", 1));
        tickets.add(new Ticket(5,"Iren", "11-03-25", "BMC", "KPT", 15, true, "BOOKED", 1));

    }

    //[i]. Create a Ticket
    @PostMapping
    @Operation(summary = "Create a new ticket")
    public List<Ticket> CreateTicket(@RequestBody TicketRequest ticketRequest){
        tickets.add(new Ticket(atomicInteger.getAndIncrement(), ticketRequest.getPassengerName(), ticketRequest.getTravelDate(), ticketRequest.getSourceStation(), ticketRequest.getDestinationStation(), ticketRequest.getPrice(), ticketRequest.getPaymentStatus(), ticketRequest.getTicketStatus(), ticketRequest.getSeatNumber()));
        return tickets;
    }

    //[ii]. Retrieve All Tickets with pagination
    @GetMapping
    @Operation(summary = "Get all tickets")
    public List<Ticket> getTickets(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "3") int size) {
        int start = page * size;
        int end = Math.min(start + size, tickets.size());

        if (start >= tickets.size()) {
            return new ArrayList<>();
        }

        return tickets.subList(start, end);
    }


    //[iii]. Retrieve a Ticket by ID (using @PathVariable)
    @GetMapping("/{id}")
    @Operation(summary = "Get a ticket by ID")
    public ResponseEntity<ApiResponse<List<Ticket>>> getTicket(@PathVariable("id") int id) {
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





    //[iv]. Search for a Ticket by Passenger Name (using @RequestParam)
    @GetMapping("/search")
    @Operation(summary = "Search tickets by passenger name")
    public List<Ticket> searchByName( @RequestParam String name){
        ArrayList<Ticket> ticketList = new ArrayList<>();
        for(Ticket t: tickets){
            if(t.getPassengerName().equalsIgnoreCase(name)){
                ticketList.add(t);
            }
        }
        return ticketList;
    }

    //[v]. Filter Tickets by Ticket Status and Travel Date (using @RequestParam)
    @GetMapping("/filter")
    @Operation(summary = "Filter tickets by status and travel date")
    public List<Ticket> filter(@RequestParam String date, @RequestParam TicketStatus status) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getTicketStatus().equals(status) && t.getTravelDate().equals(date)) {
                ticketList.add(t);
            }
        }

        return ticketList;
    }


    //[vi]. Update a Ticket by ID
    @PutMapping ("/{ticket-id}")
    @Operation(summary = "Update an existing ticket by ID")
    public Ticket updateTicket(@PathVariable int id, @RequestBody TicketRequest ticketRequest){
        for(Ticket t: tickets){
            if(t.getTicketId() == id){
                t.setPassengerName(ticketRequest.getPassengerName());
                t.setPrice(ticketRequest.getPrice());
                t.setDestinationStation(ticketRequest.getDestinationStation());
                t.setSourceStation(ticketRequest.getSourceStation());
                t.setSeatNumber(ticketRequest.getSeatNumber());
                t.setTravelDate(ticketRequest.getTravelDate());
                t.setPaymentStatus(ticketRequest.getPaymentStatus());
                return t;

            }
        }
        return null;
    }

    //[vii]. Delete a Ticket by ID
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


    //{Bonus}
    //[i]. Create Ticket as ArrayList (Done)
    //[ii]. Bulk update payment status for multiple tickets
    @PutMapping
    @Operation(summary = "Bulk update payment status for multiple tickets")
    public List<Ticket> updateMultiTicket(@PathVariable int id) {
        List<Ticket> updatedTickets = new ArrayList<>();

        for (Ticket t : tickets) {
            t.setTicketId(t.getTicketId());
            if (t.getTicketId() == id) {
                t.setPaymentStatus(true);
                updatedTickets.add(t);
            }
        }

        if (updatedTickets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching tickets found");
        }

        return updatedTickets;
    }





}