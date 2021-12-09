plugins {
	id("org.springframework.boot") version "2.6.1"
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

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
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
