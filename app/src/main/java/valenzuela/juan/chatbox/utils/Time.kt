package valenzuela.juan.chatbox.utils

import java.util.*
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Time {
    fun timeStamp():String{
        val timeStamp= Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}