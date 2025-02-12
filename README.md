# Typesafe Conventions Plugin MCVE

This is a Minimal, Complete, and Verifiable Example (MCVE) for an issue related to the `typesafe-conventions` Gradle plugin.

## Main Issue Description

When using the `typesafe-conventions` plugin in `buildSrc/build.gradle.kts`, running any task in subprojects (e.g., `commons`) results in the following errors:

```
e: file:///../buildSrc/build.gradle.kts:6:20: Unresolved reference: libs
e: file:///../buildSrc/build.gradle.kts:8:20: Unresolved reference: libs
e: file:///../buildSrc/build.gradle.kts:10:20: Unresolved reference: libs
e: file:///../buildSrc/build.gradle.kts:13:20: Unresolved reference: libs
```

The issue can be temporarily resolved by explicitly declaring dependencies without using `libs`:

```kotlin
implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.25")
```

However, it's worth noting that `libs` still works correctly in convention plugins:

```kotlin
version = libs.versions.project
```

## Additional Question (Optional)

While working on this project, I'm also learning about Gradle plugin development and encountered an issue with a custom Docker conventions plugin. When trying to use it:

```kotlin
plugins {
    id("heny.kotlin-conventions")
    id("heny.docker-conventions")
}
docker {
    // configuration
}
```

I get the error:
```
Unresolved reference: docker
```

If anyone has experience with Gradle plugin development and would be willing to share some tips or point me in the right direction, I would greatly appreciate it.

## Project Structure

The project follows a conventional Gradle structure with buildSrc for custom plugins:

- `buildSrc/`: Contains custom convention plugins
  - `kotlin-conventions`
  - `docker-conventions`
  - `spring-boot-conventions`
- `commons/`: Example subproject using the convention plugins
- `gradle/libs.versions.toml`: Version catalog for dependency management

## Environment

- Gradle: 8.4
- Kotlin: 1.9.25
- Java: 17