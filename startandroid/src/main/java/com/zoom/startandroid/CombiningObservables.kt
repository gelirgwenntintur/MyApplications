package com.zoom.startandroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy

/**
 * Операторы объединения
 * Официальная документация http://reactivex.io/documentation/operators.html
 */

class CombiningObservables {

    private val TAG = CombiningObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        merge()
        zip()
    }

    /**
     * Оператор merge объединит элементы из двух Observable в один Observable
     */
    fun merge() {
        Log.d(TAG, "merge")

        compositeDisposable.add(Observable.fromArray("1", "2", "3", "4", "5")
            .mergeWith(Observable.fromArray("6", "7", "8", "9", "10"))
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
     * Оператор zip попарно сопоставит элементы из двух Observable.
     * Из каждой пары элементов с помощью функции будет получен один элемент,
     * который будет добавлен в итоговый Observable.
     */
    fun zip() {
        Log.d(TAG, "zip")
        val zipDisposable1 =
            Observable.fromArray("1", "2", "3", "4", "5")
                .zipWith(Observable.fromArray("One", "Two", "Three", "Four", "Five"),
                    BiFunction { i: String, s: String -> "$s : $i" })
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
                )

        val observable1 = Observable.fromArray("11", "12", "13")
        val observable2 = Observable.fromArray("Eleven", "Twenty", "Thirteen")
        val zipDisposable2 =
            Observables.zip(observable1, observable2)
            { i, s -> "$s : $i" }
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
                )

        compositeDisposable.add(zipDisposable1)
        compositeDisposable.add(zipDisposable2)
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}