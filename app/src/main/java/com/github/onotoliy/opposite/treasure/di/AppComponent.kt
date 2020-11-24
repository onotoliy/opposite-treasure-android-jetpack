package com.github.onotoliy.opposite.treasure.di

import android.app.Application
import com.github.onotoliy.opposite.treasure.activity.DepositActivity
import com.github.onotoliy.opposite.treasure.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(activity: DepositActivity)
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
