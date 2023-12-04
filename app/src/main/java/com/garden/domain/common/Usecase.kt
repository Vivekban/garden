package com.garden.domain.common

interface Usecase<Input, Output> {
    operator fun invoke(input: Input): Output
}

interface SuspendUsecase<Input, Output> {
    suspend operator fun invoke(input: Input): Output
}
