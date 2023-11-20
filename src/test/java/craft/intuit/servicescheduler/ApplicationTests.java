package craft.intuit.servicescheduler;

import craft.intuit.servicescheduler.exceptions.CustomerNotFoundException;
import craft.intuit.servicescheduler.exceptions.InvalidCustomerTypeException;
import craft.intuit.servicescheduler.model.Customer;
import craft.intuit.servicescheduler.model.CustomerType;
import craft.intuit.servicescheduler.service.ServiceScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTests {
	private ServiceScheduler serviceScheduler;

	@BeforeEach
	void setUp() {
		serviceScheduler = new ServiceScheduler();
	}

	@Test
	void testNoCustomers() {
		assertNull(serviceScheduler.getNextCustomer(), "Expected null when no customers are checked in");
	}

	@Test
	void testOnlyRegularCustomers() {
		Customer regularCustomer1 = Customer.builder()
				.name("Charlie")
				.phoneNumber("3456789012")
				.customerType(CustomerType.REGULAR).build();

		Customer regularCustomer2 = Customer.builder()
				.name("Dave")
				.phoneNumber("4567890123")
				.customerType(CustomerType.REGULAR).build();

		serviceScheduler.checkIn(regularCustomer1);
		serviceScheduler.checkIn(regularCustomer2);

		assertEquals(regularCustomer1, serviceScheduler.getNextCustomer(), "First regular customer should be served first");
		assertEquals(regularCustomer2, serviceScheduler.getNextCustomer(), "Second regular customer should be served next");
		assertNull(serviceScheduler.getNextCustomer(), "Expected null when all regular customers are served");
	}

	@Test
	void testOnlyVIPCustomers() {
		Customer vipCustomer1 = Customer.builder()
				.name("Alice")
				.phoneNumber("1234567890")
				.customerType(CustomerType.VIP).build();

		Customer vipCustomer2 = Customer.builder()
				.name("Bob")
				.phoneNumber("2345678901")
				.customerType(CustomerType.VIP).build();

		serviceScheduler.checkIn(vipCustomer1);
		serviceScheduler.checkIn(vipCustomer2);

		assertEquals(vipCustomer1, serviceScheduler.getNextCustomer(), "First VIP customer should be served first");
		assertEquals(vipCustomer2, serviceScheduler.getNextCustomer(), "Second VIP customer should be served next");
		assertNull(serviceScheduler.getNextCustomer(), "Expected null when all VIP customers are served");
	}

	@Test
	void testAlternatingVIPAndRegularCustomers() {
		Customer vipCustomer = Customer.builder()
				.name("Alice")
				.phoneNumber("1234567890")
				.customerType(CustomerType.VIP).build();

		Customer regularCustomer = Customer.builder()
				.name("Charlie")
				.phoneNumber("3456789012")
				.customerType(CustomerType.REGULAR).build();

		serviceScheduler.checkIn(regularCustomer);
		serviceScheduler.checkIn(vipCustomer);

		assertEquals(vipCustomer, serviceScheduler.getNextCustomer(), "VIP customer should be served first even if checked in after a regular customer");
		assertEquals(regularCustomer, serviceScheduler.getNextCustomer(), "Regular customer should be served next");
		assertNull(serviceScheduler.getNextCustomer(), "Expected null when all customers are served");
	}

	@Test
	void testEmptyQueueAfterSomeServicing() {
		Customer vipCustomer = Customer.builder()
				.name("Alice")
				.phoneNumber("1234567890")
				.customerType(CustomerType.VIP).build();

		serviceScheduler.checkIn(vipCustomer);
		serviceScheduler.getNextCustomer();

		assertNull(serviceScheduler.getNextCustomer(), "Expected null when queue is empty after servicing");
	}

	@Test
	void whenInvalidCustomerType_thenThrowException() {
		Customer invalidCustomer = Customer.builder()
				.name("John Doe")
				.phoneNumber("5551234")
				.customerType(null).build();

		Exception exception = assertThrows(InvalidCustomerTypeException.class, () -> {
			serviceScheduler.checkIn(invalidCustomer);
		});

		String expectedMessage = "Customer type is invalid or null.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void whenCustomerNotFound_thenThrowException() {
		String nonExistentPhoneNumber = "9999999999";

		Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
			serviceScheduler.findCustomer(nonExistentPhoneNumber);
		});

		String expectedMessage = "Customer with phone number " + nonExistentPhoneNumber + " not found.";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
}