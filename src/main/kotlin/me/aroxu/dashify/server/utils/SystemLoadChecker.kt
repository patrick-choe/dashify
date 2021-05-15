package me.aroxu.dashify.server.utils

object SystemLoadChecker {

    fun getRAMInformation(): LongArray {
        val maxRuntimeMemoryInMB = Runtime.getRuntime().totalMemory() / 1048576L
        val actualUsingMemoryInMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L
        val maxHeapSizeInMB = Runtime.getRuntime().maxMemory() / 1048576L
        return longArrayOf(maxRuntimeMemoryInMB, actualUsingMemoryInMB, maxHeapSizeInMB)
    }
}