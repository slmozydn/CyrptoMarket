buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.2.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
  }
}

subprojects {
  apply(from = rootProject.file("ktlint.gradle.kts"))
}

task("clean", Delete::class) {
  delete(rootProject.buildDir)
}