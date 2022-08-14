package com.fsquiroz.campsite.util;

import java.time.temporal.Temporal;

public record Range<T extends Temporal>(T start, T end) {
}
