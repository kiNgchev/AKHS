plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AKHS"
include("akhs-discord")
include("akhs-telegram")
include("akhs-twitch")
include("akhs-core")
include("akhs-youtube")

include("akhs-configurations")