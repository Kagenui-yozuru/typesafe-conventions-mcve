rootProject.name = "typesafe-conventions-mcve"
include("commons")


dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            name = "aliyun-gradle-plugin"
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        mavenCentral()
    }
}