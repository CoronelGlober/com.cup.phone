import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
    `maven-publish`
}

val sql_delight_version = "1.4.4"
val ktor_version = "1.4.1"


group = "com.cup.phone"
version = "0.7.1"


kotlin {

    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    android{
        publishLibraryVariants("release","debug")
    }
    ios {
        binaries {
            framework {
                baseName = "core"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")  {
                    version {
                        strictly("1.3.9-native-mt-2")
                    }
                }
                implementation("com.squareup.sqldelight:runtime:$sql_delight_version")
                implementation ("com.squareup.sqldelight:coroutines-extensions:$sql_delight_version")
                implementation("io.ktor:ktor-network:$ktor_version")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation ("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sql_delight_version")
                implementation("io.ktor:ktor-client-android:$ktor_version")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sql_delight_version")
                implementation("io.ktor:ktor-client-ios:$ktor_version")
            }
        }
    }
}


repositories {
    gradlePluginPortal()
    mavenCentral()
}


android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

sqldelight {
    database("MessagesDb") {
        packageName = "com.cup.phone.core.data.db"
    }
}



publishing {
    publications {
        create<MavenPublication>("debug") {
            // Applies the component for the release build variant.
//            from(components["release"])
            groupId = "com.cup.phone"
            artifactId = "core"
            version = version
            artifact("$buildDir/outputs/aar/core-release.aar") {
                builtBy(tasks.getByName("assemble"))
            }
        }
    }

    repositories {
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/CoronelGlober/com.cup.phone")
            credentials {
                username = "CoronelGlober"
                password = "XXX7de69faa0e9766856c6XXX70a1a67dfe071fd0dccfbXXX"
            }
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(
        mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)