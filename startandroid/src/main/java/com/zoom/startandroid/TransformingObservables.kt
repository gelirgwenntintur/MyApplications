package com.zoom.startandroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Официальная документация http://reactivex.io/documentation/operators.html
 * Операторы преобразования позволяют преобразовывать данные, которые генерирует Observable.
 */
class TransformingObservables {

    private val TAG = TransformingObservables::class.qualifiedName
    private val compositeDisposable = CompositeDisposable()

    init {
        map()
        buffer()
    }

    fun onClear() {
        compositeDisposable.clear()
    }

    /**
     * map преобразует все элементы последовательности
     * Например, преобразоваться  String-элементы в Integer
     *  Т.е. оператор map не изменил исходный Observable<String>,
     *  а создал новый поверх него.
     *  Этот созданный Observable<Integer> получает данные из Observable<String>,
     *  преобразует их в Integer и шлет дальше, как будто он сам их сгенерировал.
     *
     *  Ошибка спровоцированна специально: последовательность завершается на элементе с ошибкой!
     */
    fun map() {
        Log.d(TAG, "map")
        val mapDisposable = Observable.fromArray("1", "2", "3", "4k", "5", "6")
            .map {
                Log.d(TAG, "map: $it")
                Integer.parseInt(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "onComplete")
                },

                onError = {
                    Log.e(TAG, "onError: $it")
                },

                onNext = {
                    Log.d(TAG, "onNext: $it")
                }
            )

        compositeDisposable.add(mapDisposable)
    }

    /**
     * Оператор buffer собирает элементы и по мере накопления заданного кол-ва отправляет их дальше одним пакетом.
     * Например, Создадим Observable из 8 чисел, и добавим к нему буфер с количеством элементов = 3.
     * Оператор разбил данные на блоки по 3 элемента. Тип данных Observable в случае буфера будет не Integer,
     * а List<Integer>.
     */
    fun buffer() {
        Log.d(TAG, "buffer")
        val bufferDisposable = Observable.fromArray("1", "2", "3", "4", "5", "6", "7", "8")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .buffer(3)
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext: $it")
                },
                onError = {
                   Log.e(TAG, "onError: $it")
                },
                onComplete = {
                    Log.d(TAG,"onComplete")
                }
            )

        compositeDisposable.add(bufferDisposable)
    }
}