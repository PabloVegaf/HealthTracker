# AGENTS.md

Guía operativa para agentes de IA que trabajen en HealthTracker.

Este documento es la fuente rápida de contexto del proyecto. Antes de implementar cambios, revisa también:

- `Documentation/Inicial_Notion.pdf`
- `seed.sql`
- `docker-compose.yml`
- `.env` (credenciales reales, excluido de Git)

## Estado Actual

HealthTracker está en fase inicial de desarrollo. Actualmente el repositorio contiene:

- Documentación funcional inicial exportada desde Notion.
- `docker-compose.yml` con PostgreSQL 18 + `.env` con credenciales externalizadas.
- Script de creación de tablas gestionado vía migraciones manuales en BD (no hay `sql/create_tables.sql` versionado).
- Seed SQL con datos sintéticos + importación de datos reales (336 registros diarios, 9 categorías de ejercicio, 5 mediciones corporales).
- Backend Spring Boot 4.0.6 + Java 26 scaffolded en `backend/`.
- `.gitignore` y `.env.example` para seguridad de credenciales.
- Usuario `Pablo Vega` con background deportivo almacenado en BD.

No asumas que hay arquitectura de frontend implementada. Si vas a crear estructura nueva, documenta las decisiones y mantén el alcance pequeño y verificable.

## Objetivo Del Producto

HealthTracker será una aplicación personal, multiusuario y open source para registrar, analizar y visualizar datos de salud, nutrición, peso, medidas corporales y entrenamiento.

La funcionalidad diferencial es un asistente IA que actúe como entrenador/preparador físico y nutricional. Debe poder analizar el historial completo del usuario, detectar patrones, explicar conclusiones de forma clara y ayudar a planificar entrenamiento, nutrición y objetivos.

Principios de producto:

- Uso interno/personal inicialmente.
- Multiusuario desde el principio.
- Cada usuario solo puede ver y modificar sus propios datos.
- Interfaz minimalista, clara y responsive.
- Evitar saturar al usuario con métricas sin explicación.
- Español e inglés desde las primeras fases relevantes.
- Modo claro/oscuro con preferencia del sistema o toggle.
- Sin soporte offline/PWA por ahora: requiere conexión al servidor.
- Licencia objetivo: Apache 2.0.

## Stack Objetivo

Según la documentación inicial:

- Backend: Java 26 + Spring Boot 4.0.6.
- Persistencia: Spring Data JPA + Hibernate 7.12.
- Seguridad: Spring Security + JWT.
- API: Spring Web + Spring Validation.
- Base de datos: PostgreSQL 18.
- Frontend: Astro 6.3 + React 18.3.1.
- Estilos: Tailwind CSS 3.4.17.
- Gráficas: Tremor 3.18.7 para dashboard.
- Formato: Prettier.
- Contenedores: Docker ya instalado.

Antes de fijar versiones en código, verifica disponibilidad real de esas versiones. Si alguna versión no existe o no es viable, propón alternativa conservadora y documenta el motivo.

Versiones confirmadas en el backend scaffold (Mayo 2026):

- Java 26.0.1, Spring Boot 4.0.6 (Spring Framework 7.0.7, Hibernate 7.x).
- JWT: jjwt 0.12.6.
- Gestor de build: Maven 3.9.15 (wrapper incluido).
- Base de datos: PostgreSQL 18.2 en Docker.

## Base De Datos

Motor confirmado: PostgreSQL.

Cliente disponible para agentes: MCP de Tabularis. La conexión detectada en esta sesión fue:

- Nombre: `HealthTrackerDB`
- Base de datos: `fitness_progress`
- Driver: `postgres`
- Host: `localhost`

Al iniciar una tarea relacionada con datos:

1. Intenta introspeccionar con Tabularis.
2. Si Tabularis falla, revisa las entidades JPA en `backend/src/main/java/com/healthtracker/backend/model/`.
3. No hagas migraciones destructivas sin confirmación explícita.
4. No mezcles datos entre usuarios; toda consulta operativa debe estar filtrada por `user_id`.
5. Documenta cualquier diferencia entre la base real y los scripts del repositorio.

Tablas actuales definidas en `sql/create_tables.sql`:

- `users`: usuarios con `email`, `password_hash`, `name`, `athlete_background` (contexto deportivo/nutricional para el asistente IA), timestamps.
- `exercise_categories`: categorías de ejercicio por usuario.
- `daily_records`: registro diario de peso, grasa corporal, kcal, notas y timestamps. El `exercise_duration_min` es la suma total calculada a partir de las sesiones del día.
- `exercise_sessions`: sesiones de ejercicio vinculadas a un `daily_record`. Cada una tiene `exercise_category`, `duration_min`, `notes`. Varias por día.
- `body_measurements`: medidas corporales por fecha.
- `user_configs`: configuración de proveedor/base URL/modelo/API key IA por usuario. Cualquier proveedor con API OpenAI-compatible.
- `chat_messages`: historial persistente del chat.
- `refresh_tokens`: refresh tokens de autenticación.

Restricciones importantes:

- `users.email` es único.
- `daily_records` tiene un único registro por `user_id + record_date`.
- `body_measurements` tiene un único registro por `user_id + measurement_date`.
- `exercise_categories` tiene nombre único por usuario.
- `chat_messages.role` está limitado a `user`, `assistant`, `system`, `tool`.

Pendientes probables de modelo de datos:

- Objetivos de usuario: peso objetivo, objetivo nutricional, actividad, sexo, edad, altura, experiencia deportiva.
- Macronutrientes: proteínas, carbohidratos, grasas, fibra, agua, etc.
- Calculadora de macros y snapshots de objetivos.
- Logs de auditoría.
- Importaciones externas.
- Fotos de progreso.
- Programas/rutinas de entrenamiento.
- Preferencias i18n/theme.
- Roles futuros si la app evoluciona a entrenador/cliente.

## Seguridad Y Privacidad

Los datos de salud son sensibles aunque el requisito inicial solo exija cifrar contraseñas. Trabaja con criterio conservador.

Reglas obligatorias:

- Contraseñas siempre con hash fuerte, nunca texto plano.
- JWT de acceso con expiración corta: objetivo inicial 30 minutos.
- Refresh token con expiración: objetivo inicial 7 días.
- Logout debe invalidar refresh token en servidor.
- Todas las rutas privadas deben derivar el `user_id` desde el token, no desde parámetros confiados del cliente.
- Nunca expongas API keys de usuario al frontend después de guardarlas.
- Las API keys de LLM deben almacenarse cifradas o gestionarse mediante mecanismo seguro.
- No registres secretos en logs.
- No subas credenciales reales al repositorio.
- Mantén `.env`/secretos fuera de Git cuando se inicialice el repositorio.

Riesgo actual:

- ~~`docker-compose.yml` contiene credenciales de PostgreSQL en claro.~~ -> RESUELTO: credenciales externalizadas a `.env`. `.env` excluido de Git vía `.gitignore`. Existe `.env.example` como plantilla.

Auditoría:

- El proyecto requiere logs de auditoría: quién hizo qué y cuándo.
- Diseñar `audit_logs` antes de exponer operaciones críticas o multiusuario reales.

## Autenticación

Requisitos iniciales:

- Registro con email/password.
- Login con JWT.
- Access token: 30 minutos.
- Refresh token: 7 días.
- Logout con invalidación de refresh token.
- Multiusuario.
- Sin roles por ahora: todos los usuarios son equivalentes.

Futuro:

- OAuth.
- 2FA.
- Posibles perfiles entrenador/cliente.

No implementes OAuth/2FA antes de que la base email/password esté cerrada, probada y documentada.

## Asistente IA

El asistente IA es la prioridad funcional principal.

Debe:

- Leer el historial completo del usuario.
- Analizar patrones y tendencias.
- Dar recomendaciones de entrenamiento y nutrición.
- Ayudar a planificar programas completos.
- Responder en chat general de salud con límites claros.
- Escribir datos mediante tools controladas cuando corresponda.
- Explicar conclusiones de forma sencilla y verificable.

Proveedores objetivo:

- Cualquier proveedor con API OpenAI-compatible (OpenAI, Ollama, OpenRouter, DeepSeek, Groq, Mistral, etc.).
- El usuario configura provider, base_url y API key en su perfil.
- No hay restricción de proveedores en la base de datos.

Flujo objetivo del chat:

1. Usuario envía mensaje a `POST /api/v1/chat/messages`.
2. Backend valida JWT.
3. Backend construye system prompt con rol, límites, datos relevantes y tools disponibles.
4. Backend llama al proveedor configurado por el usuario.
5. Si el modelo solicita una tool, el backend ejecuta la acción filtrada por `user_id`.
6. Backend devuelve resultado de tool al modelo.
7. Backend transmite respuesta final por streaming SSE.
8. Backend persiste historial de mensajes.

Tools candidatas:

- `get_records`
- `create_record`
- `update_record`
- `get_stats`
- `get_body_measurements`
- `get_user_goals`
- `calculate_macros`
- `list_exercise_categories`

Reglas para tools IA:

- Nunca aceptar `user_id` generado por el modelo.
- El backend debe inyectar siempre el `user_id` autenticado.
- Validar input de tools igual que cualquier request externa.
- Limitar operaciones de escritura y pedir confirmación UX cuando haya riesgo de sobrescribir datos.
- Registrar acciones relevantes en auditoría.

System prompt:

- Pendiente de diseño.
- Debe incluir límites: no diagnosticar enfermedades, no sustituir profesionales médicos, pedir atención médica ante síntomas graves.
- Debe obligar a citar datos concretos cuando haga recomendaciones basadas en historial.
- Debe distinguir entre dato observado, inferencia y recomendación.

## Funcionalidades Core

### RF01 Usuarios

- Registro.
- Login.
- Logout.
- Sesiones con refresh tokens.
- Aislamiento estricto por usuario.

### RF02 Registro Diario

Formulario con:

- Fecha, por defecto ayer.
- Peso.
- Porcentaje graso.
- Kcal gastadas.
- Kcal ingeridas.
- Una o varias sesiones de ejercicio (cada una con tipo de deporte, duración en minutos y notas).
- Tiempo total de ejercicio en minutos (suma de sesiones, calculado automáticamente).
- Notas.
- Selector rápido: hoy, ayer, calendario.
- Validación en tiempo real.
- Si ya hay datos para la fecha: pedir confirmar sobrescritura o cancelar.

### Dashboard

Debe mostrar información relevante sin saturar:

- Evolución de peso.
- Evolución de porcentaje graso.
- Kcal ingeridas/gastadas.
- Medidas corporales.
- Tendencias y conclusiones simples.
- Gráficas clave.
- Futuro: maniquí/visualización corporal con medidas.

Tipos de gráficas candidatos:

- Líneas temporales.
- Barras semanales.
- Radar de macros.
- Heatmaps de actividad.

### Configuración

- Datos de usuario.
- Configuración del chatbot.
- Proveedor IA.
- API key.
- Modelo activo.
- Gestión de sesión.
- Futuro: idioma, tema, exportaciones e importaciones.

### Exportación

Debe poder exportar todo el historial del usuario en:

- CSV.
- TOON, Token-Oriented Object Notation.

### Importación

Futuro:

- MyFitnessPal.
- Apple Health.
- Google Health/Fit.
- Garmin/Strava u otras plataformas típicas, pendiente de investigación.
- Renpho Health.

Antes de implementar importaciones, investigar APIs, formatos de exportación, autenticación y límites reales.

## UI/UX

Dirección visual:

- Minimalista.
- Limpia.
- Datos claros.
- Conclusiones fáciles de leer.
- Responsive para móvil, tablet y PC.
- Modo claro/oscuro.
- Evitar dashboards densos sin jerarquía.

Frontend:

- Astro para estructura.
- React para componentes interactivos.
- Tailwind para estilos.
- Tremor para gráficas si encaja con el diseño y versiones.

No sacrifiques claridad por mostrar más métricas. Si una métrica necesita explicación, añade copy corto o tooltip.

## Comandos De Desarrollo

Desde `backend/`:

```bash
# Compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Arrancar la aplicación (requiere PostgreSQL corriendo vía docker compose)
./mvnw spring-boot:run

# Crear una nueva clase Java (desde cualquier subdirectorio de src/main/java/)
bash scripts/new-java.sh service UserService
bash scripts/new-java.sh entity DailyRecord
bash scripts/new-java.sh controller DashboardController
bash scripts/new-java.sh repository UserRepository
bash scripts/new-java.sh dto LoginRequest
bash scripts/new-java.sh config SecurityConfig
bash scripts/new-java.sh class MiClase    # clase genérica (default)
```

El script `scripts/new-java.sh` detecta automáticamente el package según el directorio donde te encuentres.

## Testing Y Calidad

El usuario no ha fijado estrategia de tests, pero la app debe poder probarse completamente antes de lanzarse.

Expectativa mínima para agentes:

- Backend: tests unitarios de servicios críticos.
- Backend: tests de integración para auth, aislamiento por usuario y operaciones de datos.
- Frontend: tests de componentes/formularios críticos cuando haya scaffold.
- E2E: flujos principales cuando exista app funcional.
- Validación manual documentada para cambios que no puedan automatizarse todavía.

Casos críticos:

- Un usuario no puede leer datos de otro.
- Login/logout/refresh token.
- Sobrescritura controlada de registro diario.
- Validaciones numéricas.
- Chat IA no puede ejecutar tools sobre otro usuario.
- Exportación contiene solo datos del usuario autenticado.

## Estilo De Código

- Código claro, legible y documentado.
- Preferir nombres explícitos sobre abreviaturas.
- Comentarios breves para decisiones no obvias.
- Prettier para formato cuando aplique.
- Evitar abstracciones prematuras.
- Mantener endpoints, DTOs, entidades y migraciones coherentes.
- Documentar comandos de desarrollo cuando se añadan.

Si creas scaffold:

- Añade README o sección de comandos.
- Añade `.env.example`.
- No incluyas secretos reales.
- Añade `.gitignore`.
- Añade licencia Apache 2.0 si se inicializa la publicación open source.

## Convenciones De API

Objetivo de prefijo:

- `/api/v1`

Endpoints candidatos:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`
- `GET /api/v1/daily-records`
- `POST /api/v1/daily-records`
- `PUT /api/v1/daily-records/{id}`
- `GET /api/v1/body-measurements`
- `POST /api/v1/body-measurements`
- `GET /api/v1/dashboard/summary`
- `POST /api/v1/chat/messages`
- `GET /api/v1/chat/messages`
- `DELETE /api/v1/chat/messages`
- `GET /api/v1/user-config`
- `PUT /api/v1/user-config`
- `GET /api/v1/export.csv`
- `GET /api/v1/export.toon`

No fijes contratos definitivos sin alinear DTOs, validaciones y necesidades del frontend.

## Trabajo Con La Base

Para cambios de esquema:

- Preferir migraciones versionadas cuando se introduzca backend.
- Mantener compatibilidad con datos existentes siempre que sea posible.
- Si hay cambios incompatibles, documentar estrategia de migración.
- No borrar tablas ni columnas sin confirmación.
- Mantener índices para consultas frecuentes por `user_id` y fecha.

Datos legacy:

- El usuario tiene datos manuales en hoja de cálculo.
- La migración desde esa hoja se hará más adelante.
- No diseñar importador definitivo sin ver columnas/formato real.

## Backlog Priorizado

Prioridad alta:

- Confirmar/scaffoldear backend y frontend.
- Externalizar secretos de `docker-compose.yml`.
- Definir migraciones.

Prioridad media:

- Calculadora de macros.
- Exportación CSV/TOON.
- Auditoría.
- i18n español/inglés.
- Tests de integración y E2E.
- Mejoras de visualización.

Prioridad futura:

- OAuth.
- 2FA.
- Importaciones de apps externas.
- Fotos de progreso.
- Perfiles entrenador/cliente.
- Rutinas/programas avanzados.

## Preguntas Abiertas

Antes de grandes decisiones, aclarar:

- ~~¿Se confirma Java 26/Spring Boot 4 aunque sean versiones muy recientes?~~ -> **Sí, confirmado: Java 26.0.1 + Spring Boot 4.0.6.**
- ~~¿Se usará monorepo con `backend/` y `frontend/`?~~ -> **Sí, monorepo con `backend/`. Frontend en `frontend/` cuando se cree.**
- ~~¿Qué gestor de build exacto para backend: Maven o Gradle?~~ -> **Maven (wrapper incluido).**
- ¿Qué herramienta de tests E2E se prefiere?
- ¿Cómo se almacenarán/cifrarán API keys de proveedores IA?
- ¿Cuál será el formato exacto de TOON para exportación?
- ¿Qué datos necesita la calculadora de macros en la primera versión?
- ¿Qué hoja de cálculo legacy habrá que migrar y con qué columnas?

## Instrucciones Para Agentes

Antes de modificar:

1. Lee este archivo.
2. Revisa documentación y scripts relevantes.
3. Comprueba el estado real de archivos.
4. Si trabajas con datos, intenta introspección con Tabularis.
5. Distingue entre requisito confirmado, inferencia y propuesta.
6. El `.env` raíz se carga automáticamente al arrancar el backend vía `DotenvLoader`.
7. Backend en `backend/`, scripts SQL en `sql/`, seed en raíz.

Durante la implementación:

- Mantén cambios pequeños y trazables.
- No introduzcas dependencias pesadas sin justificar.
- No uses secretos reales.
- No rompas el aislamiento multiusuario.
- No implementes recomendaciones médicas como diagnóstico.
- Añade validaciones en backend aunque existan en frontend.
- Si cambias contratos API, actualiza documentación.

Al terminar:

- Resume qué cambió.
- Indica comandos ejecutados y resultado.
- Señala tests pendientes si no se pudieron ejecutar.
- Documenta riesgos o decisiones que deba revisar una persona.

