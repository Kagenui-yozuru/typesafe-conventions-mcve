plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.noarg")
}

group = "com.mcve"
version = libs.versions.project

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
    }
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    test {
        enabled = false
    }
}

noArg {
    annotation("com.heny.commons.annotation.NoArg")
    annotations("com.baomidou.mybatisplus.annotation.TableName")
}