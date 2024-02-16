package ru.sample.duckapp.infra

import android.widget.ImageView

interface DuckProvider {
    fun fetchRandomDuckImage(): ImageView?
}