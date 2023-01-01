buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.3.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
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