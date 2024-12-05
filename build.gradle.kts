plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "modernovo"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.processResources {
	dependsOn("copyFrontendToBuild")
}

tasks.register<Copy>("copyFrontendToBuild"){
	dependsOn("npmBuild")
	from("$projectDir/muzika_frontend/dist/")
	into ("$projectDir/src/main/resources/static/")
}

tasks.register<Exec>("npmBuild"){
	workingDir("$projectDir/muzika_frontend")
	commandLine("npm.cmd", "run", "build")
}