package com.zoom.startandroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Операторы объединения
 * Официальная документация http://reactivex.io/documentation/operators.html
 */

class CombiningObservables {

    private val TAG = CombiningObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        //merge()
        //concat()
        //zip()
        combineLatest()
    }

    /**
     * Оператор merge объединит элементы из двух Observable в один Observable
     * без сохранения порядка если несколько потоков, то есть если указан Schedulers.io()
     */
    fun merge() {
        Log.d(TAG, "merge")

        compositeDisposable.add(Observable.fromArray("1", "2", "3", "4", "5")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
     * Оператор concat объединит элементы из двух Observable в один Observable
     * c сохранением порядка если несколько потоков, то есть если указан Schedulers.io()
     */
    fun concat() {
        Log.d(TAG, "concat")

        compositeDisposable.add(Observable.fromArray("1", "2", "3", "4", "5")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .concatWith(Observable.fromArray("6", "7", "8", "9", "10"))
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
     * zip выполняется только когда есть элементы с обоих Observable.
     */
    fun zip() {
        Log.d(TAG, "zip")
        val observableNums = Observable.interval(300, TimeUnit.MILLISECONDS)
            .take(10)
        val zipDisposable1 =
            observableNums.zipWith(Observable.fromArray("One", "Two", "Three", "Four", "Five"),
                BiFunction { i: Long, s: String -> "$s : $i" })
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

    fun combineLatest() {
        Log.d(TAG, "zip")
        val observable1 = Observable.interval(300, TimeUnit.MILLISECONDS)
            .take(10)

        val observable2 = Observable.interval(500, TimeUnit.MILLISECONDS)
            .take(10)
            .map {
                it + 100
            }

       val combineLatestDisposable =  Observables.combineLatest(observable1, observable2){ i: Long, s: Long -> "$i and $s" }
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

        compositeDisposable.add(combineLatestDisposable)
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}