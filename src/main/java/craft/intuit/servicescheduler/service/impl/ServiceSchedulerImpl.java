package craft.intuit.servicescheduler.service.impl;

import craft.intuit.servicescheduler.exceptions.CustomerNotFoundException;
import craft.intuit.servicescheduler.model.Customer;
import craft.intuit.servicescheduler.model.CustomerType;
import craft.intuit.servicescheduler.service.ServiceScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class ServiceSchedulerImpl implements ServiceScheduler {
    private final Queue<Customer> regularQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Customer> vipQueue = new ConcurrentLinkedQueue<>();
    private int serviceCounter = 0;
    private int vipToRegularRatio = 0;

    @Override
    public synchronized void checkIn(Customer customer) {
        customer.setServiceNumber(++serviceCounter);
        if (customer.getCustomerType() == CustomerType.VIP) {
            vipQueue.add(customer);
        } else {
            regularQueue.add(customer);
        }
    }

    @Override
    public synchronized Customer getNextCustomer() {
        Customer customer = null;

        if (!vipQueue.isEmpty() && (vipToRegularRatio < 2 || regularQueue.isEmpty())) {
            customer = vipQueue.poll();
            vipToRegularRatio++;
        } else if (!regularQueue.isEmpty()) {
            customer = regularQueue.poll();
            vipToRegularRatio = 0;
        }

        log.info(customer != null ? "Serving customer: " + customer : "No customers to serve");
        return customer;
    }

    @Override
    public Customer findCustomer(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            log.error("Invalid phone number provided for customer search");
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }

        Customer customer = searchQueueForCustomer(regularQueue, phoneNumber);
        if (customer == null) {
            customer = searchQueueForCustomer(vipQueue, phoneNumber);
        }

        if (customer == null) {
            log.error("Customer not found with phone number: {}", phoneNumber);
            throw new CustomerNotFoundException("Customer with phone number " + phoneNumber + " not found.");
        }

        return customer;
    }

    private Customer searchQueueForCustomer(Queue<Customer> queue, String phoneNumber) {
        return queue.stream()
                .filter(customer -> phoneNumber.equals(customer.getPhoneNumber()))
                .findFirst()
                .orElse(null);
    }
}
