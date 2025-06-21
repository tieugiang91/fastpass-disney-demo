pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "fastpass-disney-demo"

include (
    "ticket-management-service",
    "attraction-redemption-service",
    "grpc-contract"
)