package com.planetmovie.data

import com.planetmovie.data.local.LocalDataSource
import com.planetmovie.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository
@Inject
constructor(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource
) {
    val remoteData = remoteDataSource
    val localData = localDataSource
}