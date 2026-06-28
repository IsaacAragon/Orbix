package com.orbix

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class SignUpValidationTest {

    @Test
    fun testAgeLimitsCalculation() {
        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)

        val maxCalendar = Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
        }
        val minCalendar = Calendar.getInstance().apply {
            add(Calendar.YEAR, -150)
        }

        // The max year in birthdate should be exactly currentYear - 18
        assertEquals(currentYear - 18, maxCalendar.get(Calendar.YEAR))
        // The min year in birthdate should be exactly currentYear - 150
        assertEquals(currentYear - 150, minCalendar.get(Calendar.YEAR))
        // The min calendar time should be less than the max calendar time
        assertTrue(minCalendar.timeInMillis < maxCalendar.timeInMillis)
    }
}
