plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile("com.profesorfalken","jSensors","2.2.1")
    compile(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar {
        archiveName = "HardwareMonitoringDisplay.jar"

        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)

        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })

        manifest {
            attributes["Main-Class"] = "HardwareMonitoringDisplay"
        }

        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
}

tasks.register("export") {
    group = "build"
    description = "Cleans the project and exports the project as jar file"

    dependsOn("clean")
    dependsOn("jar")
    tasks.findByName("jar")?.mustRunAfter("clean")
}