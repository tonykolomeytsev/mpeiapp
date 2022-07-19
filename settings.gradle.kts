rootProject.name = "mpeix"

rootProject.projectDir.walk()
    .filter { it.isBuildGradleScript() }
    .filter { it != rootProject.buildFile }
    .mapNotNull { it.parentFile }
    .forEach { moduleDir ->
        val moduleName = ":${moduleDir.name}"
        include(moduleName)
        project(moduleName).projectDir = moduleDir
    }

fun File.isBuildGradleScript(): Boolean =
    isFile && name.matches("build\\.gradle(\\.kts)?".toRegex())