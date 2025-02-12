import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage
import com.bmuschko.gradle.docker.tasks.image.DockerTagImage


class DockerConventionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.bmuschko.docker-remote-api")

        val extension = project.extensions.create(
            "docker",
            DockerConventionsExtension::class.java
        )

        project.afterEvaluate {
            // 1) 读取 gradle.properties（如果有），但只在 extension 上“没设置值”时才覆盖
            extension.dockerImageName = overrideIfBlankOrNull(
                extension.dockerImageName,
                project.findProperty("dockerImageName")
            )
            extension.registryUrl = overrideIfBlankOrNull(
                extension.registryUrl,
                project.findProperty("registryUrl")
            )
            extension.registryUsername = overrideIfBlankOrNull(
                extension.registryUsername,
                project.findProperty("registryUsername")
            )
            extension.registryPassword = overrideIfBlankOrNull(
                extension.registryPassword,
                project.findProperty("registryPassword")
            )
            extension.dockerCertPath = overrideIfBlankOrNull(
                extension.dockerCertPath,
                project.findProperty("dockerCertPath")
            )

            // 2) 对必填项进行校验
            val missingParams = mutableListOf<String>()
            if (extension.dockerImageName.isNullOrBlank()) {
                missingParams += "dockerImageName"
            }
            if (extension.registryUrl.isNullOrBlank()) {
                missingParams += "registryUrl"
            }
            if (extension.registryUsername.isNullOrBlank()) {
                missingParams += "registryUsername"
            }
            if (extension.registryPassword.isNullOrBlank()) {
                missingParams += "registryPassword"
            }
            if (extension.dockerCertPath.isNullOrBlank()) {
                missingParams += "dockerCertPath"
            }

            if (missingParams.isNotEmpty()) {
                throw GradleException(
                    "这些必填参数尚未指定: ${missingParams.joinToString(", ")}.\n" +
                            "请在 build.gradle(.kts) 的 dockerConventions{} 或 gradle.properties 中补充。"
                )
            }

            // 3) 只有在全部必填项都已经设置后，再进行 Docker 任务的配置
            configureDockerTasks(project, extension)
        }
    }

    abstract class DockerConventionsExtension {
        /**
         * Docker Image name，例如 "myorg/myapp:1.0"
         */
        var dockerImageName: String? = null

        /**
         * Docker Registry URL，例如 "registry.example.com"
         */
        var registryUrl: String? = null

        /**
         * Docker Registry 用户名
         */
        var registryUsername: String? = null

        /**
         * Docker Registry 密码
         */
        var registryPassword: String? = null

        /**
         * Docker Daemon 的访问 URL
         */
        var dockerUrl: String? = null

        /**
         * Docker TLS 证书路径
         */
        var dockerCertPath: String? = null
    }

    /**
     * 如果 extensionValue 本身非空(非blank)，则说明用户在 build.gradle.kts 里显式设置 -> 不再覆盖
     * 否则，如果 gradlePropertiesValue 非空，则用它来覆盖
     */
    private fun overrideIfBlankOrNull(
        extensionValue: String?,
        gradlePropertiesValue: Any?
    ): String? {
        val propertyVal = gradlePropertiesValue as? String
        return if (extensionValue.isNullOrBlank() && !propertyVal.isNullOrBlank()) {
            propertyVal
        } else {
            extensionValue
        }
    }

    private fun configureDockerTasks(project: Project, extension: DockerConventionsExtension) {
        // 由于到这里必填项都不为空，可以安全地强制拆箱
        val dockerImageName = extension.dockerImageName!!
        val registryUrl = extension.registryUrl!!
        val registryImage = "$registryUrl/$dockerImageName"

        // 配置 Docker Remote API
        project.extensions.configure<com.bmuschko.gradle.docker.DockerExtension>("docker") {
            url.set(extension.dockerUrl)
            certPath.set(project.file(extension.dockerCertPath!!))
            registryCredentials {
                username.set(extension.registryUsername!!)
                password.set(extension.registryPassword!!)
            }
        }

        project.tasks.register("removeOldImage", DockerRemoveImage::class.java) {
            group = "docker"
            description = "Removes the old Docker image."
            targetImageId(dockerImageName)
            force.set(true)
            onError {
                println("Image $dockerImageName could not be removed: $message")
            }
        }

        project.tasks.register("buildImage", DockerBuildImage::class.java) {
            group = "docker"
            dependsOn("bootJar", "removeOldImage")
            doFirst {
                project.copy {
                    from("${project.layout.buildDirectory.get()}/libs")
                    into("src/main/docker")
                    include("${project.name}-${project.version}.jar")
                }
            }
            inputDir.set(project.file("src/main/docker"))
            images.add(dockerImageName)
            buildArgs.set(
                mapOf(
                    "JAR_FILE" to "${project.name}-${project.version}.jar"
                )
            )
        }

        project.tasks.register("tagImage", DockerTagImage::class.java) {
            group = "docker"
            dependsOn("buildImage")
            imageId.set(dockerImageName)
            repository.set("$registryUrl/${dockerImageName.substringBefore(":")}")
            tag.set(
                dockerImageName.substringAfter(
                    ":",
                    project.version.toString()
                )
            )
        }

        project.tasks.register("pushImage", DockerPushImage::class.java) {
            group = "docker"
            dependsOn("tagImage")
            images.add(registryImage)
        }

        project.tasks.register("cleanImageTag", DockerRemoveImage::class.java) {
            group = "docker"
            description = "Cleans up the local Docker image tag."
            dependsOn("pushImage")
            imageId.set(registryImage)
        }

        project.tasks.register("dockerDeploy") {
            group = "docker"
            dependsOn("cleanImageTag")
            doLast {
                println("Docker image deployed and cleaned up successfully.")
            }
        }
    }
}