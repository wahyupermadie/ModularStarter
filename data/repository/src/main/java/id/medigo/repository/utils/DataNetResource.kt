package id.medigo.repository.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

abstract class DataNetResource<ResultType, RequestType> {

    val TAG = DataCacheResource::class.java.simpleName

    lateinit var result: Observable<ResultType>

    fun build(): DataNetResource<ResultType, RequestType> {
        result = networkData().map { processResponse(it) }
        return this
    }

    private fun networkData(): Observable<RequestType> {
        return if (shouldSaveOnIO()) {
            createCall().doOnNext {
                storeData(processResponse(it))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe {
                        Log.d(TAG,"Data saved on disk")
                    }
            }
        } else {
            createCall()
        }
    }

    fun storeData(data: ResultType): Completable {
        return Completable.mergeArray(saveCallResults(data))
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @MainThread
    protected abstract fun shouldSaveOnIO(): Boolean

    @WorkerThread
    protected abstract fun saveCallResults(data: ResultType): Completable

    @MainThread
    protected abstract fun createCall(): Observable<RequestType>
}