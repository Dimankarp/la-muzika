plugins {
    id("java")
    id("war")
}

group = "modernovo"
version = "1.1"

repositories {
    mavenCentral()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

val junitVersion = "5.9.2"


dependencies {

    compileOnly("jakarta.platform:jakarta.jakartaee-web-api:10.0.0")

    implementation("org.hibernate.orm:hibernate-core:6.6.1.Final")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.6.1.Final")

    implementation("jakarta.data:jakarta.data-api:1.0.1")

    providedCompile("org.slf4j:slf4j-api:2.0.13")


    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    dependsOn("npmBuild")
    from("$projectDir/muzika_frontend/dist/")
}


tasks.register<Exec>("npmBuild") {
    workingDir("$projectDir/muzika_frontend/")
    commandLine("npm.cmd", "run", "build")
}


