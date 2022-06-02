package ru.netology.nmedia.repository

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UserRepositoryModule {

    @Binds
    @Singleton
    fun provideUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl): UserRepository
}