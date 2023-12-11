plugins {
    java
    `java-library`
    `maven-publish`
}

group = "io.musician101"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnlyApi("org.spongepowered:spongeapi:10.0.0")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.musician101"
            artifactId = "spongecmd"
            version = "${project.version}"

            from(components["java"])
        }
    }
}
