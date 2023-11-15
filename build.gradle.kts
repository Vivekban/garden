buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.jacoco)
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// For Git hooks
tasks {
    register<Copy>("copyGitHooks") {
        description = "Copies the git hooks from scripts/git-hooks to the .git folder."
        group = "git hooks"
        from("${rootProject.rootDir}/pre-commit")
        into(".git/hooks")
    }

    register<Exec>("installGitHooks") {
        description = "Installs the pre-commit git hooks from scripts/git-hooks."
        group = "git hooks"
        workingDir(rootDir)
        commandLine("chmod")
        args("-R", "+x", ".git/hooks/")
        dependsOn(named("copyGitHooks"))
        onlyIf {
            isLinuxOrMacOs()
        }
        doLast {
            logger.info("Git hooks installed successfully.")
        }
    }
    register<Delete>("deleteGitHooks") {
        description = "Delete the pre-commit git hooks."
        group = "git hooks"
        delete(fileTree(".git/hooks/"))
    }
}

afterEvaluate {
    tasks["clean"].dependsOn(tasks.named("installGitHooks"))
}

fun isLinuxOrMacOs() = System.getProperty("os.name").lowercase(java.util.Locale.ROOT) in listOf("linux", "mac os", "macos")
