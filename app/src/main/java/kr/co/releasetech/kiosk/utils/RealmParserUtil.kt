package kr.co.releasetech.kiosk.utils

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.realm.RealmList
import io.realm.RealmObject

object RealmParserUtil {

    fun getGson(): Gson = GsonBuilder()
        .setLenient()
        .setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaredClass.equals(RealmObject::class.java)
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }

        })
        .create()


    class ArrayToStringTypeAdapter : TypeAdapter<RealmList<RealmObject>>(){
        override fun write(out: JsonWriter?, value: RealmList<RealmObject>?) {
            TODO("Not yet implemented")
        }

        override fun read(`in`: JsonReader?): RealmList<RealmObject> {
            TODO("Not yet implemented")
        }

    }
}