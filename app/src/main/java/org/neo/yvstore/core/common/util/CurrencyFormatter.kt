package org.neo.yvstore.core.common.util

fun Double.formatAsPrice(): String = "$%.2f".format(this)
