package com.garden.common

interface Usecase<Input, Output> {
    operator fun invoke(input: Input): Output
}
