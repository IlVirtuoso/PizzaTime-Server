
import TransactionalSagaStep.Companion.transaction
import java.util.Optional
import kotlin.test.Test


class SagaTest {


    @Test
    fun testForwardSequence() {
        var number = 0;
        val flow = SagaFlow().transaction("increment", { t ->
            number = 5;
            return@transaction t;
        }, { t->
            number = 0;
            return@transaction Optional.empty();
        }
        )
        flow.exec("increment", Optional.of(number));
        assert(number == 5);
        flow.cancelAt("increment", Optional.empty());
        assert(number == 0);
    }

    @Test
    fun testCascadeSequence(){
        var number = 0;
        val flow = sagaFlow()
            .transaction("increment", { t ->
                number++;
                return@transaction t;
            }, { t->
                number = 0;
                return@transaction Optional.empty();
            }).transaction("multiply",{t->
                number *= 5;
                return@transaction Optional.empty();
            }, {t->
                number /=5;
                return@transaction Optional.empty();
            });

        flow.exec("increment", Optional.of(number));
        flow.exec("multiply",Optional.empty());
        flow.cancelAt("multiply", Optional.empty());
        assert(number == 0);
    }



}