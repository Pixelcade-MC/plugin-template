plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
    id("io.papermc.paperweight.userdev") version "1.3.7"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    paperDevBundle("${project.property("mcVersion")}-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.7")
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything

        filesMatching("**/plugin.yml") {
            expand(project.properties)
        }
    }

    // Disable jar and replace with shadowJar
    jar {
        enabled = false
    }

    shadowJar {
        relocate("io.papermc.lib", "${project.group}.io.papermc.paperlib")
        minimize()
    }
}
