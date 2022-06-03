package ru.netology.nmedia.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.TokenRepository
import ru.netology.nmedia.repository.TokenRepositoryPreferences

@InstallIn(SingletonComponent::class)
@Module
class TokenRepositoryModule {

    @Provides
    fun providesTokenRepositoryModule(
        @ApplicationContext context: Context
    ): TokenRepository = TokenRepositoryPreferences(context)
}