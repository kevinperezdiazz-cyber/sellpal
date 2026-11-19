# ---------- Etapa 1: compilar con Maven (Java 17, igual que el pom.xml) ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ---------- Etapa 2: imagen liviana solo para ejecutar ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Limites pensados para el plan gratuito de Render (512 MB RAM, 0.1 CPU)
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:+UseSerialGC -XX:TieredStopAtLevel=1"
# En Render se sobreescribe desde Environment Variables
ENV SPRING_PROFILES_ACTIVE="postgresql,render"

EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
