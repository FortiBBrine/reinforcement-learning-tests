plugins {
    id("org.openjfx.javafxplugin") version "0.0.13"
    java
    application
}

group = "me.fortibrine"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.chen0040:java-reinforcement-learning:1.0.5")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = "16"
        targetCompatibility = "16"
    }

    application {
        mainClass = "me.fortibrine.snake.Main"
    }

    javafx {
        version = "16"
        modules = listOf(
            "javafx.controls",
            "javafx.fxml"
        )
    }


    jar {
        manifest {
            attributes["Main-Class"] = "me.fortibrine.snake.Main"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from (
            configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
        )

    }


}
