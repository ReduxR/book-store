package com.reduxr.dto.order;

import com.reduxr.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Status status;
}
