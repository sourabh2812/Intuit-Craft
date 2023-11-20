package craft.intuit.servicescheduler.service;

import craft.intuit.servicescheduler.model.Customer;

public interface ServiceScheduler {
    void checkIn(Customer customer);
    Customer getNextCustomer();
    Customer findCustomer(String phoneNumber);
}
