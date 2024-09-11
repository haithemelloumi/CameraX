package com.helloumi.ui.di

import com.helloumi.ui.utils.dispatchers.DispatcherProvider
import com.helloumi.ui.utils.dispatchers.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutineDispatcherModule {

    @Provides
    fun providesDispatcherProvider(): DispatcherProvider =
        DispatcherProviderImpl(
            main = Dispatchers.Main,
            io = Dispatchers.IO
        )
}
