package craft.intuit.servicescheduler.service;

import craft.intuit.servicescheduler.exceptions.CustomerNotFoundException;
import craft.intuit.servicescheduler.exceptions.InvalidCustomerTypeException;
import craft.intuit.servicescheduler.model.Customer;
import craft.intuit.servicescheduler.model.CustomerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Slf4j
public class ServiceScheduler {
    private final Queue<Customer> regularQueue = new LinkedList<>();
    private final Queue<Customer> vipQueue = new LinkedList<>();
    private int serviceCounter = 0;
    private int vipToRegularRatio = 0;

    public void checkIn(Customer customer) {
        if (customer.getCustomerType() == null) {
            throw new InvalidCustomerTypeException("Customer type is invalid or null.");
        }

        customer.setServiceNumber(++serviceCounter);
        if (customer.getCustomerType() == CustomerType.VIP) {
            vipQueue.add(customer);
            log.debug("VIP customer added to the queue: {}", customer.getName());
        } else if (customer.getCustomerType() == CustomerType.REGULAR) {
            regularQueue.add(customer);
            log.debug("Regular customer added to the queue: {}", customer.getName());
        } else {
            log.error("Customer type is not recognized for customer: {}", customer.getName());
            throw new InvalidCustomerTypeException("Customer type is not recognized.");
        }
    }

    public Customer getNextCustomer() {
        if (!vipQueue.isEmpty()) {
            if (vipToRegularRatio < 2) {
                vipToRegularRatio++;
                return vipQueue.poll();
            }
        }

        if (!regularQueue.isEmpty()) {
            vipToRegularRatio = 0; // Reset ratio after serving a regular customer
            return regularQueue.poll();
        }

        return null; // Return null if no customers are waiting
    }

    public Customer findCustomer(String phoneNumber) {
        // In a real scenario, this might involve searching in a database
        for (Customer customer : regularQueue) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        for (Customer customer : vipQueue) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }

        log.error("Customer not found with phone number: {}", phoneNumber);
        throw new CustomerNotFoundException("Customer with phone number " + phoneNumber + " not found.");
    }
}
