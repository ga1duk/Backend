package ru.netology.nmedia.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.UserRepository
import ru.netology.nmedia.repository.UserRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UserRepositoryModule {

    @Binds
    @Singleton
    fun provideUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl): UserRepository
}