plugins {
    `kotlin-dsl`
}
dependencies {

    implementation(libs.kotlin.jvm.plugin)
//    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.25")
    implementation(libs.kotlin.noarg.plugin)
//    implementation("org.jetbrains.kotlin:kotlin-noarg:1.9.25")
    implementation(libs.kotlin.spring.plugin)
//    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:1.9.25")

    implementation(libs.spring.boot.plugin)
    implementation("com.bmuschko:gradle-docker-plugin:9.4.0")
//    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.4.0")
}