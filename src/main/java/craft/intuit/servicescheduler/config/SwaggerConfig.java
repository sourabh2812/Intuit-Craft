package craft.intuit.servicescheduler.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("service-scheduler")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Service Scheduler API")
                        .description("This API handles the scheduling of customer services in a service center. " +
                                "It supports different customer types such as VIP and Regular, ensuring " +
                                "efficient and fair handling of service requests.")
                        .version("v1.0")))
                .build();
    }
}
