plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.heroku.sdk.heroku-gradle") version "2.0.0"
	java
}

group = "org.headcrab"
version = "1.6"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val springVersion: String = "2.6.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")
	compileOnly("org.projectlombok:lombok:1.18.22")
	runtimeOnly("org.postgresql:postgresql:42.3.1")
	annotationProcessor("org.projectlombok:lombok:1.18.22")
	testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
	testImplementation("org.springframework.security:spring-security-test:5.5.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val filePath: String = "build/libs/web-$version.jar"

heroku {
	appName = "headcrab"
	jdkVersion = "11"
	includes = listOf(filePath)
	isIncludeBuildDir = false
	processTypes = mapOf("web" to "java \$JAVA_OPTS -Dserver.port=\$PORT -jar $filePath")
}

tasks.processResources {
	filesMatching("application.yml") {
		expand(project.properties)
	}
}
