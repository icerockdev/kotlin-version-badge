import dev.icerock.kotless.kotlinversion.Dependency
import dev.icerock.kotless.kotlinversion.getDependencies
import dev.icerock.kotless.kotlinversion.getKotlinVersion
import dev.icerock.kotless.kotlinversion.getLatestVersion
import org.junit.Test
import kotlin.test.assertEquals

class ParsingTest {
    @Test
    fun `mavenMetadata xml parsing`() {
        val input = """
<?xml version="1.0" encoding="UTF-8"?>
<metadata>
  <groupId>dev.icerock.moko</groupId>
  <artifactId>geo-android</artifactId>
  <versioning>
    <latest>0.3.2</latest>
    <release>0.3.3</release>
    <versions>
      <version>0.3.3</version>
      <version>0.3.2</version>
    </versions>
    <lastUpdated>20210403133557</lastUpdated>
  </versioning>
</metadata>
        """.trimIndent()
        val output = getLatestVersion(input)

        assertEquals(expected = "0.3.2", actual = output)
    }

    @Test
    fun `pom xml parsing & kotlin version extraction`() {
        val input = """
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- This module was also published with a richer model, Gradle metadata,  -->
  <!-- which should be used instead. Do not delete the following line which  -->
  <!-- is to indicate to Gradle or any Gradle module metadata file consumer  -->
  <!-- that they should prefer consuming it instead. -->
  <!-- do_not_remove: published-with-gradle-metadata -->
  <modelVersion>4.0.0</modelVersion>
  <groupId>dev.icerock.moko</groupId>
  <artifactId>geo-android</artifactId>
  <version>0.3.2</version>
  <packaging>aar</packaging>
  <name>MOKO geo</name>
  <description>Geolocation access for mobile (android &amp; ios) Kotlin Multiplatform development</description>
  <url>https://github.com/icerockdev/moko-geo</url>
  <licenses>
    <license>
      <url>https://github.com/icerockdev/moko-geo/blob/master/LICENSE.md</url>
    </license>
  </licenses>
  <developers>
    <developer>
      <id>Alex009</id>
      <name>Aleksey Mikhailov</name>
      <email>aleksey.mikhailov@icerockdev.com</email>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:ssh://github.com/icerockdev/moko-geo.git</connection>
    <developerConnection>scm:git:ssh://github.com/icerockdev/moko-geo.git</developerConnection>
    <url>https://github.com/icerockdev/moko-geo</url>
  </scm>
  <dependencies>
    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-stdlib</artifactId>
      <version>1.4.31</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-stdlib-common</artifactId>
      <version>1.4.31</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>androidx.appcompat</groupId>
      <artifactId>appcompat</artifactId>
      <version>1.1.0</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>androidx.lifecycle</groupId>
      <artifactId>lifecycle-extensions</artifactId>
      <version>2.1.0</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>com.google.android.gms</groupId>
      <artifactId>play-services-location</artifactId>
      <version>18.0.0</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.jetbrains.kotlinx</groupId>
      <artifactId>kotlinx-coroutines-core-jvm</artifactId>
      <version>1.4.2</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>dev.icerock.moko</groupId>
      <artifactId>parcelize-android</artifactId>
      <version>0.6.1</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>dev.icerock.moko</groupId>
      <artifactId>permissions-android</artifactId>
      <version>0.8.0</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-android-extensions-runtime</artifactId>
      <version>1.4.31</version>
      <scope>runtime</scope>
    </dependency>
  </dependencies>
</project>
        """.trimIndent()
        val output = getDependencies(input)

        assertEquals(expected = 9, actual = output.size)
        assertEquals(
            expected = Dependency(
                groupId = "org.jetbrains.kotlin",
                artifactId = "kotlin-stdlib",
                version = "1.4.31"
            ),
            actual = output.first()
        )

        val kotlinVersion = getKotlinVersion(output)
        assertEquals(expected = "1.4.31", actual = kotlinVersion)
    }
}