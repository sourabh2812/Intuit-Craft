package craft.intuit.servicescheduler.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The type of customer, which can be either Regular or VIP")
public enum CustomerType {
    @Schema(description = "A regular customer")
    REGULAR,

    @Schema(description = "A VIP customer with higher priority")
    VIP
}