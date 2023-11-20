package craft.intuit.servicescheduler.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    @Schema(description = "Name of the customer", example = "John Doe")
    private String name;

    @Schema(description = "Phone number of the customer", example = "1234567890")
    private String phoneNumber;

    @Schema(description = "Type of the customer (Regular or VIP)", example = "REGULAR")
    private CustomerType customerType;

    @Schema(description = "Sequential service number assigned to the customer", example = "101")
    private Integer serviceNumber;
}
