package com.zoom.startandroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Официальная документация http://reactivex.io/documentation/operators.html
 * Операторы создания позволяют создать Observable.
 */
class СreatingObservables {

    val TAG = СreatingObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        Log.d(TAG, "Creating Operators")

        from()
        range()
        interval()
        fromCallable()
    }

    fun onClear() {
        compositeDisposable.clear()
    }

    /**
     * Cоздает Observable из массива или коллекции
     */
    fun from() {
        Log.d(TAG, "from")
        val fromObservable = Observable.fromArray("one", "two", "three")
        compositeDisposable.add(fromObservable.subscribeBy(
            onNext = {
                Log.d(TAG, "onNext: $it")
            },

            onError = {
                Log.d(TAG, "Error $it")
            },

            onComplete = {
                Log.d(TAG, "onComplete")
            }
        ))
    }

    /**
     * Генерирует 4 числа начиная с 10
     */
    fun range() {
        Log.d(TAG, "range")
        val rangeObservable = Observable.range(10, 4)
        compositeDisposable.add(rangeObservable.subscribeBy(
            onNext = {
                Log.d(TAG, "onNext: $it")
            },

            onError = {
                Log.d(TAG, "Error $it")
            },

            onComplete = {
                Log.d(TAG, "onComplete")
            }
        ))
    }

    /**
     * Выдает последовательность long чисел начиная с 0.
     * Каждые 500 мсек в Observer будет приходить все увеличивающееся значение, начиная с 0.
     */
    fun interval() {
        Log.d(TAG, "interval")
        val rangeObservable = Observable.interval(500, TimeUnit.MILLISECONDS)
        compositeDisposable.add(rangeObservable.subscribeBy(
            onNext = {
                Log.d(TAG, "onNext: $it")
            },

            onError = {
                Log.d(TAG, "Error $it")
            },

            onComplete = {
                Log.d(TAG, "onComplete")
            }
        ))
    }

    /**
     * Если есть синхронный метод, который надо сделать асинхронным
     */
    fun fromCallable() {
        Log.d(TAG, "fromCallable")
        compositeDisposable.add(
            Observable.fromCallable {
                longAction("5")
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d(TAG, "onNext: $it")
                }
        )
    }

    private fun longAction(text: String): Int {
        Log.d(TAG, "longAction")
        Thread.sleep(10000)
        return Integer.parseInt(text)
    }

}