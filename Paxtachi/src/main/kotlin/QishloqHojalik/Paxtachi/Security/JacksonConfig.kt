package QishloqHojalik.Paxtachi.Security

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

class SafeIntDeserializer : StdDeserializer<Int?>(Int::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Int? {
        return when (p.currentToken) {
            JsonToken.VALUE_NUMBER_INT   -> p.intValue
            JsonToken.VALUE_NUMBER_FLOAT -> p.doubleValue.toInt()
            JsonToken.VALUE_STRING       -> p.text?.trim()?.replace(",", ".")?.toDoubleOrNull()?.toInt()
            JsonToken.VALUE_NULL         -> null
            else                         -> null
        }
    }
    override fun getNullValue(ctxt: DeserializationContext?): Int? = null
}

class SafeDoubleDeserializer : StdDeserializer<Double?>(Double::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Double? {
        return when (p.currentToken) {
            JsonToken.VALUE_NUMBER_FLOAT,
            JsonToken.VALUE_NUMBER_INT -> p.doubleValue
            JsonToken.VALUE_STRING     -> p.text?.trim()?.replace(",", ".")?.toDoubleOrNull()
            JsonToken.VALUE_NULL       -> null
            else                       -> null
        }
    }
    override fun getNullValue(ctxt: DeserializationContext?): Double? = null
}

@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val safeModule = SimpleModule().apply {
            addDeserializer(Int::class.javaObjectType,    SafeIntDeserializer())
            addDeserializer(Double::class.javaObjectType, SafeDoubleDeserializer())
        }
        return ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            registerModule(safeModule)
        }
    }
}
