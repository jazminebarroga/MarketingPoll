package com.jpbbarroga.marketingpoll

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SmsParserUnitTest {

    val smsParser = com.jpbbarroga.marketingpoll.SmsParser()
    @Test
    fun parsingIsCorrectForHans() {
        val test1 = smsParser.parseSms("BB HANS")
        assertEquals(Person.HANS, test1.first)
        assertEquals("", test1.second)

        val test2 = smsParser.parseSms("BB HANS HELLO WORLD")
        assertEquals(Person.HANS, test2.first)
        assertEquals("HELLO WORLD", test2.second)

        val test3 = smsParser.parseSms("BB HANS HELLO WORLD     ")
        assertEquals(Person.HANS, test3.first)
        assertEquals("HELLO WORLD", test3.second)

        val test4 = smsParser.parseSms("bB hAns e0w ph0uz  ")
        assertEquals(Person.HANS, test4.first)
        assertEquals("e0w ph0uz", test4.second)

    }

    @Test
    fun parsingIsCorrectForLean() {

        val test1 = smsParser.parseSms("BB LEAN")
        assertEquals(Person.LEAN, test1.first)
        assertEquals("", test1.second)

        val test2 = smsParser.parseSms("BB LEAN HELLO WORLD")
        assertEquals(Person.LEAN, test2.first)
        assertEquals("HELLO WORLD", test2.second)

        val test3 = smsParser.parseSms("BB LEAN HELLO WORLD     ")
        assertEquals(Person.LEAN, test3.first)
        assertEquals("HELLO WORLD", test3.second)

        val test4 = smsParser.parseSms("bB leAn e0w ph0uz  ")
        assertEquals(Person.LEAN, test4.first)
        assertEquals("e0w ph0uz", test4.second)
    }

    @Test
    fun parsingIsCorrectForMikko() {
        val test1 = smsParser.parseSms("BB MIKKO")
        assertEquals(Person.MIKKO, test1.first)
        assertEquals("", test1.second)

        val test2 = smsParser.parseSms("BB MIKKO HELLO WORLD")
        assertEquals(Person.MIKKO, test2.first)
        assertEquals("HELLO WORLD", test2.second)

        val test3 = smsParser.parseSms("BB MIKKO HELLO WORLD     ")
        assertEquals(Person.MIKKO, test3.first)
        assertEquals("HELLO WORLD", test3.second)

        val test4 = smsParser.parseSms("bB mikkO e0w ph0uz  ")
        assertEquals(Person.MIKKO, test4.first)
        assertEquals("e0w ph0uz", test4.second)

    }

}
