plugins {
    id("java")
    id("war")
}

group "modernovo"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

ext {
    junitVersion = "5.9.2"
}

sourceCompatibility = JavaVersion.VERSION_11
targetCompatibility = JavaVersion.VERSION_11

tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}

dependencies {
    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:10.0.0")

    implementation("org.hibernate.orm:hibernate-core:6.6.1.Final")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.6.1.Final")

    implementation("jakarta.data:jakarta.data-api:1.0.1")


    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}


tasks.processResources {
    dependsOn("copyFrontendToBuild")
}

tasks.register<Copy>("copyFrontendToBuild"){
    dependsOn("npmBuild")
    from("$projectDir/muzika_frontend/dist/")
    into ("$buildDir/resources/main/static/")
}

tasks.register<Exec>("npmBuild"){
    workingDir("$projectDir/muzika_frontend/")
    commandLine("npm.cmd", "run", "build")
}


test {
    useJUnitPlatform()
}