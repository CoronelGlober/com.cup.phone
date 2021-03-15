import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

val sql_delight_version = "1.4.4"
val ktor_version = "1.5.2"
val koin_version= "3.0.1-beta-1"


kotlin {
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "core"
            }
        }
    }

    android()
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                implementation("com.squareup.sqldelight:runtime:$sql_delight_version")
                implementation ("com.squareup.sqldelight:coroutines-extensions:$sql_delight_version")
                implementation("io.ktor:ktor-network:$ktor_version")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                // Koin for Kotlin Multiplatform
                implementation("io.insert-koin:koin-core:$koin_version")

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

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

sqldelight {
    database("MessagesDb") {
        packageName = "com.cup.phone.core.data.db"
        sourceFolders = listOf("sqldelight")
    }
}