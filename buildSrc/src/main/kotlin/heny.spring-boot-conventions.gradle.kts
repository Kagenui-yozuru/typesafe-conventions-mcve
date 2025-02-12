import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
}
group = "com.mcve"
version = libs.versions.project
tasks {
    withType<BootJar> {
        archiveFileName.set("${project.name}-${project.version}.jar")
        enabled = true
    }
}
