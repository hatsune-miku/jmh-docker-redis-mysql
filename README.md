# ENGI-9875 course project

Student: Zhen Guan (202191382)

Package name `com.mikutart.*` is just a nickname used widely in my projects.

---

Simple profiling program using Java Microbenchmark Harness (JMH), Docker, MySQL (for disk-intensive tasks) and Redis (for memory-intensive tasks).

Works on Linux, macOS and Windows.

### Windows-only Preliminaries

- Requires Docker running on either Windows Containers or Linux Containers
- For Windows Containers, enable `experimental` feature.

### Common Steps

- Build Jar

Windows:

```sh
gradlew.bat jar
```

Linux:

```sh
./gradlew jar
```

- Run

```sh
docker-compose up -d
```
