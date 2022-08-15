package com.fsquiroz.campsite.api;

import java.time.Instant;

public record GenericResponseDTO(Instant timestamp, String status, String message) {
}
