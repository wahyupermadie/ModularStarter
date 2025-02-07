package id.medigo.repository.di

import id.medigo.repository.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatchers(AndroidSchedulers.mainThread(), Schedulers.io()) }
    factory { UserRepositoryImpl(get(), get()) as UserRepository }
    factory { AuthRepositoryImpl(get(), get()) as AuthRepository }
    factory { PreferenceRepositoryImpl(get()) as PreferenceRepository }
}