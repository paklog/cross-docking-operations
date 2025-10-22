package com.paklog.crossdocking.application.command;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteOperationCommand {
    @NotBlank
    private String operationId;
    private Integer itemsProcessed;
}
