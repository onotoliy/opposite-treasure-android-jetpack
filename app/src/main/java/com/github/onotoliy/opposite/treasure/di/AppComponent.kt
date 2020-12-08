package com.github.onotoliy.opposite.treasure.di

import android.app.Application
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.github.onotoliy.opposite.treasure.App
import com.github.onotoliy.opposite.treasure.di.database.AppDatabase
import com.github.onotoliy.opposite.treasure.di.database.RoomModule
import com.github.onotoliy.opposite.treasure.di.worker.WorkerModule
import com.github.onotoliy.opposite.treasure.ui.activity.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import java.util.concurrent.Executors

@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ResourceModule::class,
    ViewModelFactoryModule::class,
    WorkerModule::class,
    RoomModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun roomModule(roomModule: RoomModule): Builder
        fun build(): AppComponent
    }

    fun inject(activity: DepositActivity)
    fun inject(activity: DepositPageActivity)

    fun inject(activity: EventActivity)
    fun inject(activity: EventEditActivity)
    fun inject(activity: EventPageActivity)

    fun inject(activity: TransactionActivity)
    fun inject(activity: TransactionEditActivity)
    fun inject(activity: TransactionPageActivity)

    fun inject(activity: LoginActivity)
    fun inject(activity: LoadingActivity)

    fun getWorkerFactory(): WorkerFactory

    override fun inject(app: App)

    class Initializer private constructor() {
        companion object {
            fun init(application: Application): AppComponent {

                val coreDataComponent = DaggerAppComponent.builder()
                    .appModule(AppModule(application))
                    .roomModule(RoomModule(Room
                        .databaseBuilder(application, AppDatabase::class.java, "treasure-db")
                        .build())
                    )
                    .build()

                WorkManager.initialize(
                    application,
                    Configuration.Builder()
                        .setExecutor(Executors.newFixedThreadPool(100))
                        .setWorkerFactory(coreDataComponent.getWorkerFactory())
                        .build()
                )

                return coreDataComponent
            }
        }
    }
}
