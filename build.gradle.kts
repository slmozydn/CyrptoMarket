buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.1.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.2")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

subprojects {
  apply(from = rootProject.file("ktlint.gradle.kts"))
}

task("clean", Delete::class) {
  delete(rootProject.buildDir)
}