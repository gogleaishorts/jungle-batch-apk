package com.example

import com.example.data.model.PhysicalLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PhysicalLogTest {

    @Test
    fun testArmyRunGroup1Scoring() {
        val log = PhysicalLog(
            cadetId = 1,
            runTimeSeconds = 325, // 5 min 25 sec
            pullUpsCount = 10
        )
        assertEquals("Group 1 (60 Marks)", log.runPetGroup)
        assertEquals(60, log.runMarks)
        assertEquals(40, log.pullUpsMarks)
        assertEquals(100, log.totalPetMarks)
        assertTrue(log.isAllQualifiersPassed)
    }

    @Test
    fun testArmyRunGroup2Scoring() {
        val log = PhysicalLog(
            cadetId = 1,
            runTimeSeconds = 340, // 5 min 40 sec
            pullUpsCount = 8 // 27 marks
        )
        assertEquals("Group 2 (48 Marks)", log.runPetGroup)
        assertEquals(48, log.runMarks)
        assertEquals(27, log.pullUpsMarks)
        assertEquals(75, log.totalPetMarks)
    }

    @Test
    fun testBeamPullUpsGrading() {
        val log10 = PhysicalLog(cadetId = 1, runTimeSeconds = 320, pullUpsCount = 10)
        val log9 = PhysicalLog(cadetId = 1, runTimeSeconds = 320, pullUpsCount = 9)
        val log8 = PhysicalLog(cadetId = 1, runTimeSeconds = 320, pullUpsCount = 8)
        val log7 = PhysicalLog(cadetId = 1, runTimeSeconds = 320, pullUpsCount = 7)
        val log6 = PhysicalLog(cadetId = 1, runTimeSeconds = 320, pullUpsCount = 6)
        val log5 = PhysicalLog(cadetId = 1, runTimeSeconds = 320, pullUpsCount = 5)

        assertEquals(40, log10.pullUpsMarks)
        assertEquals(33, log9.pullUpsMarks)
        assertEquals(27, log8.pullUpsMarks)
        assertEquals(21, log7.pullUpsMarks)
        assertEquals(16, log6.pullUpsMarks)
        assertEquals(0, log5.pullUpsMarks)
    }
}
