# ETAPA 1: Compilacion de Kotlin/Wasm

FROM eclipse-temurin:21-jdk-jammy AS build

ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}"

RUN apt-get update && \
    apt-get install -y curl unzip git ca-certificates && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    curl -fsSL https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip \
    -o /tmp/android-tools.zip && \
    unzip -q /tmp/android-tools.zip -d /tmp/android-tools && \
    mv /tmp/android-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm -rf /tmp/android-tools /tmp/android-tools.zip

RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-36" "build-tools;36.0.0"

WORKDIR /app/frontend

COPY frontend/ ./

RUN chmod +x gradlew

RUN ./gradlew :webApp:wasmJsBrowserDistribution \
    --no-daemon \
    --no-configuration-cache \
    --max-workers=2 \
    -Dorg.gradle.jvmargs="-Xmx2048m -Dfile.encoding=UTF-8"

# ETAPA 2: Publicacion con Nginx

FROM nginx:stable-alpine

COPY deploy/nginx/default.conf /etc/nginx/conf.d/default.conf

RUN rm -rf /usr/share/nginx/html/*

COPY --from=build /app/frontend/webApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
