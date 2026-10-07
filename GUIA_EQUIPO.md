# Guía de trabajo en equipo — EcoSuma

Repositorio: https://github.com/KathyRot/MyApplication1

Cada integrante trabaja en su propia rama, hace commits desde su cuenta de GitHub y abre un Pull Request (PR). Otra persona del equipo lo revisa y lo aprueba. Al final, `develop` se une a `main` como versión entregable.

---

## 1. Ramas

```
main                      ← versión entregable (protegida, solo se actualiza por PR)
 └── develop              ← integración del equipo
      ├── feature/backend-login     Integrante 2
      ├── feature/frontend-login    Integrante 1
      └── feature/vistas-roles      Integrante 3 (sale de develop cuando ya está el login)
```

## 2. Reparto

| Integrante | Rama | Responsable de | Preguntas que debe dominar |
|---|---|---|---|
| **1 — Kathy** (dueña del repo) | `feature/frontend-login` | Proyecto KMP, Gradle, capas `domain` y `data`, Ktor Client, ViewModel, pantalla de login, tema | Cómo se conecta el serializador en el cliente, `expect/actual`, StateFlow, por qué `10.0.2.2` |
| **2** | `feature/backend-login` | Todo `ecosuma-backend/`: Ktor Server, `users.json`, modelos, plugins, ruta de login | Cómo se lee el JSON, ContentNegotiation, CORS, `application.yaml`, qué responde con credenciales malas |
| **3** | `feature/vistas-roles` | Rutas por rol, `AppNavigation`, vistas de Ciudadano, Administrador y Acopio, componentes, datos de ejemplo | Cómo se elige la vista según el rol, `popUpTo`, `SessionManager`, cómo se cierra sesión |

Todos deben poder explicar el flujo completo del README (sección 4) y la estructura de carpetas, porque las preguntas pueden ser de cualquier parte.

---

## 3. Preparación (lo hace Kathy una sola vez)

### 3.1 Invitar a los compañeros
GitHub → repositorio → **Settings → Collaborators → Add people**. Cada uno acepta la invitación desde su correo.

### 3.2 Crear `develop` con la estructura base
En una carpeta de trabajo:

```bash
git clone https://github.com/KathyRot/MyApplication1.git
cd MyApplication1
git config user.name "Kathy ..."          # tu nombre
git config user.email "tu-correo@..."     # el correo de tu cuenta de GitHub
```

Si el repositorio ya tiene el proyecto Android anterior, muévelo a una carpeta `anterior/` para conservar el avance:

```bash
mkdir anterior
git mv app gradle build.gradle.kts settings.gradle.kts gradle.properties gradlew gradlew.bat anterior/  # ajusta según lo que exista
git commit -m "chore: mover proyecto Android inicial a anterior/"
```

Copia de este paquete: `README.md`, `GUIA_EQUIPO.md`, `.gitignore` y la carpeta `.github/`.

```bash
git add README.md GUIA_EQUIPO.md .gitignore .github
git commit -m "docs: estructura del proyecto EcoSuma, guía del equipo y plantilla de PR"
git push origin main
git switch -c develop
git push -u origin develop
```

### 3.3 Proteger `main`
**Settings → Branches → Add branch protection rule** (o *Rulesets*): nombre `main`, marcar *Require a pull request before merging* y *Require approvals: 1*. Haz lo mismo con `develop` si quieren más orden.

---

## 4. Trabajo de cada integrante

Pasos comunes antes de empezar (cada quien en su computadora y con su cuenta):

```bash
git clone https://github.com/KathyRot/MyApplication1.git
cd MyApplication1
git config user.name "Tu nombre"
git config user.email "correo-de-tu-cuenta@..."
git switch develop
git pull
git switch -c feature/NOMBRE-DE-TU-RAMA
```

La idea es hacer **varios commits pequeños**, uno por paso, y probar antes de cada uno.

### Integrante 2 — `feature/backend-login`

1. Generar el proyecto en https://start.ktor.io: artifact `com.ecosuma.ecosuma-backend`, motor Netty, configuración YAML, plugins Routing, Content Negotiation, kotlinx.serialization, CORS, Call Logging y Status Pages. Descomprimirlo como `ecosuma-backend/` en la raíz del repo.
   ```bash
   git add ecosuma-backend
   git commit -m "feat(backend): proyecto base generado con el wizard de Ktor"
   ```
2. Copiar `models/Models.kt`:
   ```bash
   git add ecosuma-backend
   git commit -m "feat(backend): modelos serializables de usuario y login"
   ```
   Los siguientes pasos se registran igual (`git add` + `git commit`) con el mensaje indicado.
3. Copiar `resources/data/users.json` y `data/UserRepository.kt` → commit `feat(backend): credenciales predefinidas en users.json y lectura con UserRepository`
4. Copiar la carpeta `plugins/` (borra los archivos de plugins que generó el wizard si se repiten) → commit `feat(backend): serialización JSON, CORS, logs y manejo de errores`
5. Copiar `routes/Routing.kt`, `Application.kt` y `application.yaml` → commit `feat(backend): ruta POST /api/auth/login`
6. Probar: abrir `ecosuma-backend/` en Android Studio, abrir `Application.kt` y presionar la flecha verde junto a `main`. Luego:
   ```bash
   curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"maria\",\"password\":\"maria123\"}"
   ```
7. `git push -u origin feature/backend-login` y abrir el PR hacia **develop**.

### Integrante 1 (Kathy) — `feature/frontend-login`

Puede trabajar al mismo tiempo que el Integrante 2.

1. Generar el proyecto KMP: en Android Studio con el plugin **Kotlin Multiplatform** (*File → New → New Project → Kotlin Multiplatform*) o en https://kmp.jetbrains.com. Nombre `EcoSuma`, ID `com.ecosuma.app`, plataformas Android, iOS (Share UI) y Web. Guardarlo como `ecosuma-frontend/`.
   → commit `feat(frontend): proyecto Kotlin Multiplatform generado con el wizard`
2. Aplicar `gradle-snippets/` (versiones, dependencias, `AndroidManifest.xml`, `Info.plist`) y sincronizar Gradle (*Sync Now*).
   → commit `build(frontend): dependencias de Ktor Client, serialization y navigation`
3. Copiar `ApiConfig.kt` y los tres `ApiConfig.*.kt` → commit `feat(frontend): URL del backend por plataforma con expect/actual`
4. Copiar la carpeta `domain/` → commit `feat(frontend): capa de dominio (User, Role, LoginUseCase)`
5. Copiar `data/model`, `data/remote`, `data/repository`, `data/session` y `di/` → commit `feat(frontend): capa de datos con Ktor Client y repositorio de autenticación`
6. Copiar `ui/login/LoginViewModel.kt`, `composeApp/theme/Theme.kt`, `composeApp/screens/LoginScreen.kt`, `composeApp/components/DemoCredentialsCard.kt`, y como `App.kt` el archivo temporal `entregas-intermedias/App.kt.integrante1`. Borrar `Greeting.kt` y `Platform.kt` del wizard.
   → commit `feat(frontend): pantalla de login con ViewModel y tema EcoSuma`
7. Probar en el Pixel 9 con el backend encendido (si el PR del Integrante 2 aún no se une, puede correr el backend desde la rama de su compañero con `git switch feature/backend-login` en otra carpeta). Al entrar con `maria` debe aparecer "Sesión iniciada: María López (Ciudadano)".
8. `git push -u origin feature/frontend-login` y abrir el PR hacia **develop**.

### Integrante 3 — `feature/vistas-roles`

Empieza cuando los dos PR anteriores ya estén unidos a `develop`:

```bash
git switch develop
git pull
git switch -c feature/vistas-roles
```

1. Copiar `composeApp/components/RoleScaffold.kt` y `EcoCards.kt` → commit `feat(ui): componentes compartidos para las vistas por rol`
2. Copiar `data/sample/SampleData.kt` → commit `feat(ui): datos de ejemplo de la propuesta (materiales, centros, beneficios, campañas)`
3. Copiar `screens/roles/CiudadanoScreen.kt` → commit `feat(ui): vista del ciudadano con perfil, materiales, centros y beneficios`
4. Copiar `AdministradorScreen.kt` y `AcopioScreen.kt` → commit `feat(ui): vistas de administrador y personal de acopio`
5. Copiar `navigation/Routes.kt`, `navigation/AppNavigation.kt` y reemplazar `App.kt` por la versión final → commit `feat(nav): navegación por rol y cierre de sesión`
6. Probar los cuatro usuarios en el Pixel 9 y que "Cerrar sesión" regrese al login.
7. `git push -u origin feature/vistas-roles` y abrir el PR hacia **develop**.

---

## 5. Revisión de Pull Requests

Cada PR lo revisa **otra persona**, no quien lo hizo:

| PR | Revisa |
|---|---|
| `feature/backend-login` | Integrante 1 |
| `feature/frontend-login` | Integrante 3 |
| `feature/vistas-roles` | Integrante 2 |

En GitHub: PR → *Files changed* → *Review changes* → escribir un comentario → *Approve* → *Merge pull request*. Si algo falla, se pide el cambio con *Request changes*, el autor corrige en la misma rama y hace push.

## 6. Versión final

Cuando las tres ramas estén en `develop` y todo funcione:

1. Abrir un PR de `develop` → `main` titulado **"Entrega: login con roles v1.0"**. Lo aprueba cualquier integrante distinto a quien lo abre.
2. Crear la etiqueta: **Releases → Draft a new release → tag `v1.0`**, rama `main`.
3. Poner el link del repositorio en la documentación de la entrega.

---

## 7. Lo mismo desde Android Studio (sin terminal)

| Acción | Dónde |
|---|---|
| Clonar | *File → New → Project from Version Control* |
| Cambiar o crear rama | Nombre de la rama abajo a la derecha (o arriba en la barra) → *New Branch* |
| Commit | *Ctrl + K* (Mac: *Cmd + K*), elegir archivos y escribir el mensaje |
| Push | *Ctrl + Shift + K* |
| Traer cambios | *Git → Pull* |

La primera vez pedirá iniciar sesión en GitHub: cada quien entra con **su** cuenta.

## 8. Problemas frecuentes

- **"Permission denied" al hacer push:** falta aceptar la invitación de colaborador.
- **El login dice que no se pudo conectar:** el backend no está corriendo, o se está usando `localhost` en vez de `10.0.2.2` en el emulador.
- **Conflicto al hacer pull:** Android Studio muestra *Merge Conflicts*; elegir los cambios correctos con *Merge...* y hacer commit. Trabajar cada quien en sus archivos evita casi todos los conflictos.
- **Los commits aparecen con otro nombre:** revisar `git config user.email`; debe ser el correo de la cuenta de GitHub.
