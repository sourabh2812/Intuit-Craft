package craft.intuit.servicescheduler.controller;

import craft.intuit.servicescheduler.model.Customer;
import craft.intuit.servicescheduler.service.ServiceScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
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
    public String checkInCustomer(@RequestBody Customer customer) {
        log.info("Checking in customer: {}", customer.getName());
        serviceScheduler.checkIn(customer);
        return "Customer checked in with service number: " + customer.getServiceNumber();
    }

    @GetMapping("/nextCustomer")
    @Operation(summary = "Get the next customer",
            description = "Retrieves the next customer to be served based on the scheduling rules",
            responses = {
                    @ApiResponse(description = "Next customer to be served", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No customers in the queue", content = @Content)
            })
    public Customer getNextCustomer() {
        Customer customer = serviceScheduler.getNextCustomer();
        if (customer != null) {
            log.info("Next customer to be served: {}", customer.getName());
        } else {
            log.info("No more customers in the queue");
        }
        return customer;
    }

    @GetMapping("/findCustomer/{phoneNumber}")
    @Operation(summary = "Find a customer by phone number",
            description = "Finds a customer using their phone number",
            responses = {
                    @ApiResponse(description = "Customer found", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
            })
    public Customer findCustomer(@PathVariable String phoneNumber) {
        log.info("Finding customer with phone number: {}", phoneNumber);
        return serviceScheduler.findCustomer(phoneNumber);
    }
}
