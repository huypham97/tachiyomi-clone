package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.R

interface BaseMapper<D, E> {
    fun toEntity(dto: D): E

    fun fromEntity(entity: E): D
}