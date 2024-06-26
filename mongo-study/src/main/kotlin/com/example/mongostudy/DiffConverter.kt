package com.example.mongostudy

import org.bson.Document
import org.springframework.data.convert.PropertyValueConverter
import org.springframework.data.convert.ValueConversionContext

class DiffConverter :
    PropertyValueConverter<Map<String, DiffValue<String, String>>, Document, ValueConversionContext<*>> {


    override fun read(
        value: Document,
        context: ValueConversionContext<*>
    ): Map<String, DiffValue<String, String>> {

        val toMap = value.map {
            val diffValue = it.value as Document
            val key = it.key!!

            val pair = key to DiffValue(
                origin = diffValue["origin"].toString(),
                new = diffValue["new"].toString(),
            )
            pair
        }
            .toMap()

        val mapValues: Map<String, () -> DiffValue<String, String>> = value.mapValues { (key, value) ->
            {
                val diffValue = value as Document
                DiffValue(
                    origin = diffValue["origin"].toString(),
                    new = diffValue["new"].toString(),
                )
            }
        }

        val mapValue2s: Map<String, DiffValue<String, String>> = value.mapValues { (key, value) ->
            val diffValue = value as Document
            DiffValue(
                origin = diffValue["origin"].toString(),
                new = diffValue["new"].toString(),
            )
        }


        return toMap
    }

    override fun write(
        value: Map<String, DiffValue<String, String>>,
        context: ValueConversionContext<*>
    ): Document {

        val mapValues = value.mapValues { (key, value) ->

            mapOf(
                "origin" to value.origin,
                "new" to value.new
            )

        }


        val document = Document(mapValues)
        return document

    }
}