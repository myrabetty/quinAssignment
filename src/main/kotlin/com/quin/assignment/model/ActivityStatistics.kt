package com.quin.assignment.model

/**
 * class holding statistics.
 */

data class ActivityStatistics(
        val timeSeries: Map<String, List<Any?>>,
        val weeklyStatistics: Map<String, List<Any>>
)
