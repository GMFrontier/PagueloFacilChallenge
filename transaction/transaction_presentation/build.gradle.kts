apply {
    from("$rootDir/navigation-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.transactionDomain))
    "implementation"(platform("com.google.firebase:firebase-bom:31.2.0"))
    "implementation"("com.google.firebase:firebase-config-ktx:21.2.1")
}