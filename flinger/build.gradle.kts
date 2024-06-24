plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
}

android {
    namespace = "io.iamjosephmj.flinger"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.material)

    testImplementation(libs.androidx.core)
    testImplementation(libs.truth)
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components["release"])
                groupId = "io.imjosephmj.flinger"
                artifactId = "release"
                version = "1.3.0"
            }

            create("debug", MavenPublication::class) {
                from(components["debug"])
                groupId = "io.iamjosephmj.flinger"
                artifactId = "release"
                version = "1.3.0"
            }
        }
    }
}
