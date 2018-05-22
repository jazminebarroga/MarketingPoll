package com.jpbbarroga.marketingpoll


/**
 * Created by jeppie on 15/05/2018.
 */

class SmsParser {

    companion object {
        fun parseSms(sms: String): Pair<Person, String> {
            val strings = sms.split(" ", ignoreCase = true, limit = 3)
            var person: Person = Person.UNDETERMINED
            var message = ""
            if (strings.size >= 2 && strings[0].equals("BB", true)) {
                val name = strings[1]
                if (name.equals(Person.HANS.firstName, true)) {
                    person = Person.HANS
                } else if (name.equals(Person.LEAN.firstName, true)) {
                    person = Person.LEAN
                } else if (name.equals(Person.MIKKO.firstName, true)) {
                    person = Person.MIKKO
                }
            }
            if (strings.size == 3) {
                message = strings[2]
            }

            return Pair(person, message.trim())
        }
    }

}