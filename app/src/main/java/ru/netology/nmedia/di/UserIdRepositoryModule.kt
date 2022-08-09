package ru.netology.nmedia.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.TokenRepository
import ru.netology.nmedia.repository.TokenRepositoryPreferences
import ru.netology.nmedia.repository.UserIdRepository
import ru.netology.nmedia.repository.UserIdRepositoryPreferences

@InstallIn(SingletonComponent::class)
@Module
class UserIdRepositoryModule {

    @Provides
    fun providesUserIdRepositoryModule(
        @ApplicationContext context: Context
    ): UserIdRepository = UserIdRepositoryPreferences(context)
}