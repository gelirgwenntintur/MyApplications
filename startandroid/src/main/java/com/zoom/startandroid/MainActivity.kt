package com.zoom.startandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.qualifiedName

    private val creationOperators: СreatingObservables by lazy { СreatingObservables() }
    private val transformingOperators: TransformingObservables by lazy { TransformingObservables() }
    private val filteringOperators: FilteringObservables by lazy { FilteringObservables() }
    private val combiningOperators: CombiningObservables by lazy { CombiningObservables() }
    private val conditionAndBooleanOperators: ConditionAndBooleanObservables by lazy { ConditionAndBooleanObservables() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creationOperators
        //transformingOperators

        //filteringOperators.debounce(searchEditText)

        combiningOperators
        //conditionAndBooleanOperators
    }

    override fun onStop() {
        super.onStop()
        creationOperators.onClear()
        transformingOperators.onClear()
        combiningOperators.onClear()
        conditionAndBooleanOperators.onClear()
    }
}
