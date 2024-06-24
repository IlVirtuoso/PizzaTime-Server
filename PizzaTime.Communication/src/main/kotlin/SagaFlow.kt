import java.util.Optional
import javax.swing.text.html.Option

abstract class SagaStep(val stepId: String){


    open fun execute(content: Optional<Any>) : Optional<Any>{
        return Optional.empty()
    }

    open fun cancel(content: Optional<Any>) : Optional<Any>{
        return Optional.empty()
    }
}

open class UnCancellableSagaStep(stepId: String,val fn: (Optional<Any>)-> Optional<Any>) :SagaStep(stepId){

    companion object{
        fun SagaFlow.forward(stepId: String, fn: (Optional<Any>)-> Optional<Any>) : SagaFlow{
            val step = UnCancellableSagaStep(stepId,fn);
            next(step);
            return this;
        }
    }


    override fun execute(content: Optional<Any>) : Optional<Any>{
        return fn(content);
    }
}

open class TransactionalSagaStep(stepId: String,val fn: (Optional<Any>)-> Optional<Any>, val cancelFn: (content: Optional<Any>) -> Optional<Any>) :SagaStep(stepId){

    companion object{
        fun SagaFlow.transaction(stepId: String, fn: (Optional<Any>)-> Optional<Any>, cancelFn: (Optional<Any>) -> Optional<Any>) : SagaFlow{
            val step = TransactionalSagaStep(stepId, fn,cancelFn);
            next(step)
            return this;
        }
    }

    override fun execute(content: Optional<Any>) : Optional<Any>{
        return fn(content);
    }

    override fun cancel(content: Optional<Any>) :Optional<Any> {
        return cancelFn(content);
    }
}


class SagaFlow {
    val steps: MutableList<SagaStep> = mutableListOf()

    fun next(step: SagaStep){
        steps.add(step);
    }

    fun exec(stepId: String, content: Optional<Any>){
        steps.find { it.stepId == stepId }?.execute(content);
    }

    fun cancelAt(stepId: String, content: Optional<Any>){
        var result = content;
        for(i in (0..steps.indexOfFirst { t-> t.stepId == stepId }).reversed()){
            result = steps[i].cancel(result)
        }
    }

}

fun sagaFlow(vararg steps: SagaStep): SagaFlow {
    val sagaFlow = SagaFlow()
    for(step in steps){
        sagaFlow.next(step);
    }
    return sagaFlow;
}


