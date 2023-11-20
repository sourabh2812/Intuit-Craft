package craft.intuit.servicescheduler.controller;

import craft.intuit.servicescheduler.model.Customer;
import craft.intuit.servicescheduler.service.ServiceScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/scheduler")
public class ServiceSchedulerController {
    private final ServiceScheduler serviceScheduler;

    public ServiceSchedulerController(ServiceScheduler serviceScheduler) {
        this.serviceScheduler = serviceScheduler;
    }

    @PostMapping("/checkIn")
    @Operation(summary = "Check in a new customer",
            description = "Adds a new customer to the queue and assigns a service number",
            responses = {
                    @ApiResponse(description = "Customer checked in successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid customer details", content = @Content)
            })
    public ResponseEntity<String> checkInCustomer(@RequestBody Customer customer) {
        try {
            log.info("Checking in customer: {}", customer.getName());
            serviceScheduler.checkIn(customer);
            return ResponseEntity.ok("Customer checked in with service number: " + customer.getServiceNumber());
        } catch (Exception e) {
            log.error("Error during customer check-in: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error checking in customer: " + e.getMessage());
        }
    }

    @GetMapping("/nextCustomer")
    @Operation(summary = "Get the next customer",
            description = "Retrieves the next customer to be served based on the scheduling rules",
            responses = {
                    @ApiResponse(description = "Next customer to be served", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No customers in the queue", content = @Content)
            })
    public ResponseEntity<Customer> getNextCustomer() {
        Customer customer = serviceScheduler.getNextCustomer();
        if (customer != null) {
            log.info("Next customer to be served: {}", customer.getName());
            return ResponseEntity.ok(customer);
        } else {
            log.info("No more customers in the queue");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findCustomer/{phoneNumber}")
    @Operation(summary = "Find a customer by phone number",
            description = "Finds a customer using their phone number",
            responses = {
                    @ApiResponse(description = "Customer found", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
            })
    public ResponseEntity<Customer> findCustomer(@PathVariable String phoneNumber) {
        try {
            log.info("Finding customer with phone number: {}", phoneNumber);
            Customer customer = serviceScheduler.findCustomer(phoneNumber);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            log.error("Error finding customer: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
