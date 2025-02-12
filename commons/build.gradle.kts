plugins {
    id("heny.kotlin-conventions")
    id("heny.docker-conventions")
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    // log
    compileOnly(libs.logback)
    compileOnly(libs.slf4j)
    // jjwt
    api(libs.jjwt)
    // Fastjson2
    api(libs.fastjson2)
}
tasks.test {
    enabled = false;
}
