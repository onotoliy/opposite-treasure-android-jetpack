package com.github.onotoliy.opposite.treasure.di

import android.app.Application
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.github.onotoliy.opposite.treasure.App
import com.github.onotoliy.opposite.treasure.di.database.DatabaseModule
import com.github.onotoliy.opposite.treasure.di.database.TreasureDatabase
import com.github.onotoliy.opposite.treasure.di.restful.RESTFulModule
import com.github.onotoliy.opposite.treasure.di.worker.WorkerModule
import com.github.onotoliy.opposite.treasure.ui.activity.DepositActivity
import com.github.onotoliy.opposite.treasure.ui.activity.DepositPageActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventEditActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventPageActivity
import com.github.onotoliy.opposite.treasure.ui.activity.LoadingActivity
import com.github.onotoliy.opposite.treasure.ui.activity.LoginActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionEditActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionPageActivity
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import java.util.concurrent.Executors

@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    RESTFulModule::class,
    WorkerModule::class,
    DatabaseModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun roomModule(roomModule: DatabaseModule): Builder
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
                    .roomModule(DatabaseModule(Room
                        .databaseBuilder(application, TreasureDatabase::class.java, "treasure-db")
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
