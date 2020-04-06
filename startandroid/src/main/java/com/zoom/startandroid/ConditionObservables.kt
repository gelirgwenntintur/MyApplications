package com.zoom.startandroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Операторы условий
 *  Официальная документация http://reactivex.io/documentation/operators.
 */
class ConditionObservables {

    private val TAG = ConditionObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        takeUntil()
        all()
    }

    /**
     * Оператор takeUntil будет брать элементы пока не попадется элемент,
     * удовлетворяющий определенному условию.
     */
    fun takeUntil() {
        Log.d(TAG, "takeUntil")
        compositeDisposable.add(Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeUntil { it == 5 }
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            ))
    }

    /**
     * Оператор all позволяет узнать все ли элементы удовлетворяют указанному условию.
     */
    private fun all() {
        Log.d(TAG, "all")
        compositeDisposable.add(Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeUntil { it < 10 }
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                    Log.d(TAG, "onError: $it")
                },

                onComplete = {
                    Log.d(TAG, "onComplete")
                }
            ))
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}