package com.paklog.crossdocking.application.command;

import jakarta.validation.constraints.NotBlank;

public record InitiateDirectTransferCommand(
    @NotBlank
    String transferOrderId
) {}
