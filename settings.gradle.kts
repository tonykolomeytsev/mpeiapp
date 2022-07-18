rootProject.name = "mpeix"

File(rootProject.projectDir, "modules").walk()
    .filter { it.isBuildGradleScript() }
    .mapNotNull { it.parentFile }
    .forEach { moduleDir ->
        val moduleName = ":${moduleDir.name}"
        include(moduleName)
        project(moduleName).projectDir = moduleDir
    }

fun File.isBuildGradleScript(): Boolean =
    isFile && name.matches("build\\.gradle(\\.kts)?".toRegex())