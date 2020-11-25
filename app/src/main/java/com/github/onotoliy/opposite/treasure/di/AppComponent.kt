package com.github.onotoliy.opposite.treasure.di

import android.app.Application
import com.github.onotoliy.opposite.treasure.App
import com.github.onotoliy.opposite.treasure.ui.activity.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ViewModelFactoryModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(activity: DepositActivity)
    fun inject(activity: DepositPageActivity)

    fun inject(activity: EventActivity)
    fun inject(activity: EventPageActivity)

    fun inject(activity: TransactionActivity)
    fun inject(activity: TransactionPageActivity)

    override fun inject(app: App)

    class Initializer private constructor() {
        companion object {
            fun init(application: Application): AppComponent {
                return DaggerAppComponent.builder()
                    .appModule(AppModule(application))
                    .build()
            }
        }
    }
}
