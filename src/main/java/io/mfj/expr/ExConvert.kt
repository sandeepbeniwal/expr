/*
Copyright 2018 Measures for Justice Institute.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.mfj.expr

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Date

object ExConvert {

  fun convertStr(v: String?, dataType: ExDataType) : Any? {
    try {
      return when (dataType) {
        ExDataType.STRING -> anyToString(v)
        ExDataType.REGEX -> Regex( anyToString(v) ?: throw IllegalArgumentException( "Regex pattern cannot be null" ) )
        ExDataType.INTEGER -> anyToInt(v)
        ExDataType.DOUBLE -> anyToDouble(v)
        ExDataType.DATE -> anyToLocalDate(v)
        ExDataType.TIME -> anyToLocalTime(v)
        ExDataType.DATETIME -> anyToLocalDateTime(v)
        ExDataType.BOOLEAN -> anyToBoolean(v)
      }
    } catch (e: Exception) {
      throw RuntimeException("Error converting \"$v\" to $dataType - ${e.message}", e )
    }
  }

  fun anyToString(v: Any?) : String? = when (v) {
    null -> null
    is String -> v
    else -> "$v"
  }

  fun anyToInt(v: Any?) : Any? = when (v) {
    null -> null
    is Int -> v
    is Long -> v.toInt()
    is String -> {
      if (v.isEmpty()) {
        null
      } else {
        BigDecimal(v).setScale(0, RoundingMode.FLOOR).toInt()
      }
    }
    is Float -> v.toInt()
    is Double -> v.toInt()
    is BigDecimal -> v.setScale(0, RoundingMode.FLOOR).toInt()
    else -> "$v".toInt()
  }

  fun anyToLong(v: Any?) : Any? = when (v) {
    null -> null
    is Int -> v.toLong()
    is Long -> v
    is String -> {
      if (v.isEmpty()) {
        null
      } else {
        BigDecimal(v).setScale(0, RoundingMode.FLOOR).toLong()
      }
    }
    is Float -> v.toLong()
    is Double -> v.toLong()
    is BigDecimal -> v.setScale(0, RoundingMode.FLOOR).toLong()
    else -> "$v".toLong()
  }

  fun anyToDouble(v: Any?) : Any? = when (v) {
    null -> null
    is Int -> v.toDouble()
    is Long -> v.toDouble()
    is Float -> v.toDouble()
    is Double -> v
    is String -> {
      if (v.isEmpty()) {
        null
      } else {
        v.toDouble()
      }
    }
    is BigDecimal -> v.toDouble()
    else -> "$v".toDouble()
  }

  fun anyToBoolean(v: Any?) : Any? = when (v) {
    null -> null
    is Boolean -> v
    is String -> {
      if (v.isEmpty()) {
        null
      } else {
        v.toBoolean()
      }
    }
    else -> "$v".toBoolean()
  }

  fun anyToLocalDate(v: Any?) : Any? = when (v) {
    null -> null
    is Date -> v.toInstant().atZone(ZoneId.of("UTC")).toLocalDate()
    is LocalDate -> v
    is Int -> throw RuntimeException("Bad date (int) $v")
    is Long -> throw RuntimeException("Bad date (long) $v")
    else -> LocalDate.parse("$v", DateTimeFormatter.ISO_DATE)
  }

  fun anyToLocalTime(v: Any?) : Any? = when (v) {
    null -> null
    is Date -> v.toInstant().atZone(ZoneId.of("UTC")).toLocalTime()
    is LocalDate -> v
    is Int -> throw RuntimeException("Bad date (int) $v")
    is Long -> throw RuntimeException("Bad date (long) $v")
    else -> LocalTime.parse("$v", DateTimeFormatter.ISO_TIME)
  }

  fun anyToLocalDateTime(v: Any?) : Any? = when (v) {
    null -> null
    is Date -> LocalDateTime.ofInstant(v.toInstant(), ZoneId.of("UTC"))
    is LocalDate -> v
    is Int -> throw RuntimeException("Bad datetime (int) $v")
    is Long -> throw RuntimeException("Bad datetime (int) $v")
    else -> LocalDateTime.parse("$v", DateTimeFormatter.ISO_DATE_TIME)
  }

  fun anyToDaysSinceEpoch(v: Any?) : Any? = when (v) {
    null -> null
    is Date -> {
      val sdf = SimpleDateFormat("yyyy-MM-dd")
      LocalDate.parse(sdf.format(v)).toEpochDay().toInt()
    }
    is LocalDate -> v.toEpochDay().toInt()
    is Int -> v
    is Long -> v.toInt()
    else -> "$v".toInt()
  }

  fun anyToMillisSinceEpoch(v: Any?) : Any? = when (v) {
    null -> null
    is Date -> v.time.toInt()
    is LocalDateTime -> v.toInstant(ZoneOffset.UTC).toEpochMilli().toInt()
    is Int -> v
    is Long -> v.toInt()
    else -> "$v".toInt()
  }

}
