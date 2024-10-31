package com.positiveHelicopter.doobyplus.model

data class SocialsTab(
    val title: String,
    val icon: Int,
    val monoIcon: Int,
    val color: Int,
    val subTabs: List<String>,
    var selectedIndex: Int = 0
)
