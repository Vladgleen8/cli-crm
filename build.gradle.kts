plugins {
    application
    checkstyle
}

group = "ru.mpbank.crm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "6.1.3"
val assertjVersion = "3.27.7"
val lombokVersion = "1.18.48"
val checkstyleVersion = "14.3.0"

checkstyle {
    toolVersion = checkstyleVersion
    maxErrors = 0
    maxWarnings = 999
}

tasks.withType<Checkstyle>().configureEach {
    group = "verification"
}

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ru.mpbank.crm.app.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}