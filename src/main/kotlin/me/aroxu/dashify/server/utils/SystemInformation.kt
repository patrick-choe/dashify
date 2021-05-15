package me.aroxu.dashify.server.utils

import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean;
import kotlin.math.round

object SystemInformation {
    fun getCPUInformation(): Array<Any> {
        val operatingSystemMXBean: OperatingSystemMXBean =
            ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val archInfo = operatingSystemMXBean.arch
        val availableProcessors = operatingSystemMXBean.availableProcessors
        val cpuLoad = round((operatingSystemMXBean.processCpuLoad * 100) * 100) / 100
        return arrayOf(archInfo, availableProcessors, cpuLoad)
    }

    fun getRAMInformation(): Array<Long> {
        val operatingSystemMXBean: OperatingSystemMXBean =
            ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean

        val maxJvmMemory = Runtime.getRuntime().totalMemory()
        val freeJvmMemory = Runtime.getRuntime().freeMemory()
        val usingJvmMemory =
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
        val maxJvmHeapSize = Runtime.getRuntime().maxMemory()

        val maxPhysicalRamSize = operatingSystemMXBean.totalPhysicalMemorySize
        val freePhysicalRamSize = operatingSystemMXBean.freePhysicalMemorySize
        val usedPhysicalRamSize = maxPhysicalRamSize - freePhysicalRamSize

        val maxVirtualRamSize = operatingSystemMXBean.totalSwapSpaceSize
        val freeVirtualRamSize = operatingSystemMXBean.freeSwapSpaceSize
        val usedVirtualRamSize = maxVirtualRamSize - freeVirtualRamSize
        val committedVirtualRamSize = operatingSystemMXBean.committedVirtualMemorySize

        return arrayOf(
            maxJvmMemory,
            freeJvmMemory,
            usingJvmMemory,
            maxJvmHeapSize,
            maxPhysicalRamSize,
            freePhysicalRamSize,
            usedPhysicalRamSize,
            maxVirtualRamSize,
            freeVirtualRamSize,
            usedVirtualRamSize,
            committedVirtualRamSize
        )
    }

    fun getOSInformation(): Array<String> {
        val operatingSystemMXBean: OperatingSystemMXBean =
            ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean

        val osName = operatingSystemMXBean.name
        val osVersion = operatingSystemMXBean.version
        return arrayOf(osName, osVersion)
    }
}