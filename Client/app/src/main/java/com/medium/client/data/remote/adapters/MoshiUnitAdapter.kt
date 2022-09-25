package com.medium.client.data.remote.adapters

import com.squareup.moshi.*
import javax.inject.Inject

class MoshiUnitAdapter @Inject constructor() : JsonAdapter<Unit?>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Unit? {
        reader.skipValue()
        return Unit
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Unit?) {
        writer.nullValue()
    }
}
