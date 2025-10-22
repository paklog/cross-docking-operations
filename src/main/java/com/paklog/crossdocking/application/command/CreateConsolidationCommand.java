package com.paklog.crossdocking.application.command;

import com.paklog.crossdocking.domain.valueobject.ConsolidationStrategy;
import lombok.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsolidationCommand {
    @NotEmpty
    private List<String> transferOrderIds;
    @NotBlank
    private String destinationDock;
    @NotNull
    private ConsolidationStrategy strategy;
}
