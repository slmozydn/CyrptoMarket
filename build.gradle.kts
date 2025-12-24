buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.7.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.1.0")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.52")
    classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.0-1.0.29")
  }
}

subprojects {
  apply(from = rootProject.file("ktlint.gradle.kts"))
}

task("clean", Delete::class) {
  delete(rootProject.buildDir)
}