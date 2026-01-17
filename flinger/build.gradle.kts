plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
}

android {
    namespace = "io.iamjosephmj.flinger"
    compileSdk = 35

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
    
    buildFeatures {
        compose = true
    }
}


dependencies {
    // Compose BOM for version management
    implementation(platform(libs.androidx.compose.bom))
    
    // Core Compose dependencies
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui)
    
    // AppCompat for backward compatibility
    implementation(libs.androidx.appcompat)

    // Material (for FlingBehavior compatibility)
    implementation(libs.androidx.material)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.truth)
    testImplementation(libs.mockito.core)
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components["release"])
                groupId = "io.iamjosephmj.flinger"
                artifactId = "flinger"
                version = "1.4.0"
            }

            create("debug", MavenPublication::class) {
                from(components["debug"])
                groupId = "io.iamjosephmj.flinger"
                artifactId = "flinger-debug"
                version = "1.4.0"
            }
        }
    }
}
