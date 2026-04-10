plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "de.juyas"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.citizensnpcs.co/repo")
    maven {
        name = "MavenRepo"
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    // Paper API & Internals
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.20.1-R0.1-SNAPSHOT")

    // Citizens API
    compileOnly("net.citizensnpcs:citizens-main:2.0.32-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }

    // Hilfsbibliotheken
    compileOnly("org.jetbrains:annotations:24.0.1")

    // Lombok - Version auf 1.18.30 erhöht für Java 21 Support
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

java {
    // Falls dein Server auf Java 17 läuft, solltest du hier ggf. auf 17 zurückgehen.
    // Da du Java 21 angegeben hast, lassen wir es für den Compiler so:
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        // Sorgt dafür, dass der Compiler Java 21 versteht
        options.release.set(21)
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        // Name der fertigen Datei
        archiveFileName.set("CustomTrader.jar")
    }
}