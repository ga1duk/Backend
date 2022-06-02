package ru.netology.nmedia.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface PostRepositoryModule {

    @Binds
    @Singleton
    fun bindsPostRepositoryImpl(postRepositoryImpl: PostRepositoryImpl): PostRepository
}