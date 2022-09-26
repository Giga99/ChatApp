package com.medium.client.data.remote.adapters

import android.annotation.SuppressLint
import com.squareup.moshi.*
import java.time.Instant
import javax.inject.Inject

@SuppressLint("NewApi")
class MoshiInstantAdapter @Inject constructor() : JsonAdapter<Instant?>() {

    @FromJson
    override fun fromJson(reader: JsonReader): Instant? {
        return reader.readJsonValue()?.toString()?.let {
            val milliseconds = it.toLong()
            Instant.ofEpochMilli(milliseconds)
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Instant?) {
        if (value != null) writer.value(value.toEpochMilli())
        else writer.nullValue()
    }
}
