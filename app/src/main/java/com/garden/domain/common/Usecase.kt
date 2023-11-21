package com.garden.domain.common

interface Usecase<Input, Output> {
    operator fun invoke(input: Input): Output
}
