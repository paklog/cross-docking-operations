package com.paklog.crossdocking.application.command;

import com.paklog.crossdocking.domain.valueobject.*;
import lombok.*;
import javax.validation.constraints.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferOrderCommand {
    @NotNull
    private TransferType transferType;
    @NotBlank
    private String sourceDockId;
    @NotBlank
    private String destinationDockId;
    private List<Map<String, Object>> items;
    private TimingWindow timingWindow;
    private TransferPriority priority;
    private String inboundShipmentId;
    private String outboundShipmentId;
}
