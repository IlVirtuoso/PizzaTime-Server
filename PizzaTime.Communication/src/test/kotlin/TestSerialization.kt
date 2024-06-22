import com.google.gson.Gson
import kotlin.test.Test


open class Partial{
    open var name: String = "";
}

class TestSerialization {
    @Test
    fun testPartialSerialization(){
        var data = object : Any() {
            var name: String = "ciao";
            var type : String = "Tuipo";
        }

        var serialized = Gson().toJson(data);
        println(serialized);

        var deser : Partial = Gson().fromJson(serialized, Partial::class.java);
        var seri2 = Gson().toJson(deser);
        println(seri2);
    }
}