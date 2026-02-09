package com.ibrahim.restaurant.dto.timerange;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeRangeDto {

    @NotBlank(message = "Open time must be provided")
    @Pattern(
            regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
            message = "Open time must be in HH:mm format (00:00 - 23:59)"
    )
    private String openTime;

    @NotBlank(message = "Open time must be provided")
    @Pattern(
            regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
            message = "Close time must be in HH:mm format (00:00 - 23:59)"
    )
    private String closeTime;
}
