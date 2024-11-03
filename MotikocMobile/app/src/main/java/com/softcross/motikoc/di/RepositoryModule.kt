package com.softcross.motikoc.di

import com.softcross.motikoc.data.FirebaseRepositoryImpl
import com.softcross.motikoc.data.GeminiRepositoryImpl
import com.softcross.motikoc.domain.repository.FirebaseRepository
import com.softcross.motikoc.domain.repository.GeminiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun provideFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl): FirebaseRepository


    @Binds
    @ViewModelScoped
    abstract fun provideGeminiRepository(geminiRepositoryImpl: GeminiRepositoryImpl): GeminiRepository

}