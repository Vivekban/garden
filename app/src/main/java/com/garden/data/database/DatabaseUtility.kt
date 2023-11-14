package com.garden.data.database

/**
 * Contains various utility and helper functions related to database / room.
 */
object DatabaseUtility {

    /**
     *  Need to make necessary changes in query so that similar searches can be made otherwise db
     *  will match exactly same.
     *  Ex. If a query is empty then it will match items with empty value.
     *
     *  Appending '%' so we can allow other characters to be before and after the query string
     */
    fun toDatabaseQuery(query: String?) = "%${query?.replace(" ", "%") ?: ""}%"
}