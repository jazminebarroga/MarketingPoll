package com.jpbbarroga.marketingpoll


/**
 * Created by jeppie on 15/05/2018.
 */

class SmsParser {

    fun parseSms(sms: String): Pair<Person, String> {
        val strings = sms.split(" ", ignoreCase = true, limit = 3)
        var person: Person = Person.UNDETERMINED
        if (strings[0].equals("BB", true)) {
            if (strings[1].equals(Person.HANS.firstName, true)) {
                person = Person.HANS
            } else if (strings[1].equals(Person.LEAN.firstName, true)) {
                person = Person.LEAN
            } else if (strings[1].equals(Person.MIKKO.firstName, true)) {
                person = Person.MIKKO
            }
        }
        var message = ""
        if (strings.size == 3) {
            message = strings[2]
        }

        return Pair(person, message.trim())
    }
}