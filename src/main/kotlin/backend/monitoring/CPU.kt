package backend.monitoring

import backend.errors.NoComponentFoundException
import backend.errors.NoComponentSensorFoundException
import com.profesorfalken.jsensors.JSensors

class CPU {

    val cpus = JSensors.get.components().cpus
    val name = cpus.first().name

    fun temperature(): Double? {
        if (cpus != null) {
            for (cpu in cpus) {
                if (cpu.sensors != null) {
                    return cpu.sensors.temperatures.first().value
                }else {
                    throw NoComponentSensorFoundException("CPU")
                }
            }
        }else {
            throw NoComponentFoundException("CPU")
        }

        return null
    }

}