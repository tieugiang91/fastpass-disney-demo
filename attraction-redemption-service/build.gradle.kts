plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
}

group = "com.example.attractionredemption"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":grpc-contract"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}