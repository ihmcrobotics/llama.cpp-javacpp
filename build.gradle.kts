plugins {
    id("java-library")
    id("maven-publish")
}

group = "us.ihmc"
version = "b4829"

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "llamacpp-javacpp"
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            val releasesRepo = uri("https://s01.oss.sonatype.org/content/repositories/releases")
            val snapshotsRepo = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepo else releasesRepo

            credentials {
                username = project.findProperty("publishUsername").toString()
                password = project.findProperty("publishPassword").toString()
            }
        }
    }
}

dependencies {
    // Transitive dependencies
    api("us.ihmc:javacpp:1.5.11-ihmc-2") {
        isTransitive = true
    }
    api("us.ihmc:ihmc-native-library-loader:2.0.4") {
        isTransitive = true
    }

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
