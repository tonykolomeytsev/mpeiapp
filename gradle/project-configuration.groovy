
class ProjectConfiguration {

    private final File sourcesRoot

    ProjectConfiguration(File projectRoot) {
        sourcesRoot = new File(projectRoot, "modules")
    }

    void apply(Closure include) {
        final foundModuleDirs = new LinkedList([sourcesRoot])
        while (!foundModuleDirs.isEmpty()) {
            final moduleDir = foundModuleDirs.poll()
            if (isModuleRoot(moduleDir)) {
                include(":${moduleDir.name}", moduleDir)
            } else {
                foundModuleDirs.addAll(moduleDir.listFiles() ?: new File[0])
            }
        }
    }

    private boolean isModuleRoot(File file) {
        return new File(file, "build.gradle").exists()
    }
}

new ProjectConfiguration(rootProject.projectDir).apply { moduleName, moduleDir ->
    include(moduleName)
    project(moduleName).projectDir = moduleDir
}