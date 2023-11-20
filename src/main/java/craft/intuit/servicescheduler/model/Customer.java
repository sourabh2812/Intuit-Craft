package craft.intuit.servicescheduler.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    @Schema(description = "Name of the customer", example = "John Doe")
    @NotBlank(message = "Customer name must not be blank")
    private String name;

    @Schema(description = "Phone number of the customer", example = "1234567890")
    @NotBlank(message = "Phone number must not be blank")
    private String phoneNumber;

    @Schema(description = "Type of the customer (Regular or VIP)", example = "REGULAR")
    @NotNull(message = "Customer type must not be null")
    private CustomerType customerType;

    @Schema(description = "Sequential service number assigned to the customer", example = "101")
    private Integer serviceNumber;
}
