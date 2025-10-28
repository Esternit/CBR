# Конвертер валют по курсу ЦБ РФ

Простое клиент-серверное приложение для конвертации валют на основе официальных котировок ЦБ РФ.  
Поддерживает два бэкенда на выбор: **Spring Boot** и **Ktor**, общий фронтенд на **Next.js** и общую базу данных **PostgreSQL**.

---

## Структура проекта

```

CBR/
├── ktor/ # Бэкенд на Ktor (Kotlin)
├── spring/ # Бэкенд на Spring Boot (Java)
├── react/ # Фронтенд на Next.js (TypeScript + React)
├── docker-compose.yml
├── docker-compose.spring.yml
├── docker-compose.ktor.yml
├── .env.example # Переменные окружения
└── README.md

```

---

## Требования

- Docker и Docker Compose
- JDK 21 (для Ktor и Spring)
- Maven 3.9+ (для Spring)
- Gradle 8.11+ (для Ktor)
- Node.js 20+ и npm (для React)

---

## Запуск с Docker (рекомендуется)

### 1. Настройка

Скопируйте `.env.example` в `.env` (если нет — создайте):

### 2. Выберите бэкенд

#### Для Spring Boot:

```bash
docker-compose -p cbr -f docker-compose.yml -f docker-compose.spring.yml up -d --build
```

#### Для Ktor:

```bash
docker-compose -p cbr -f docker-compose.yml -f docker-compose.ktor.yml up -d --build
```

Бэкенд будет доступен на `http://localhost:8080`.
Приложение будет доступно по адресу: [http://localhost:3000](http://localhost:3000)

---

## Запуск без Docker (для разработки)

### 0. Настройте .env

Убедитесь, что в .env база указана через localhost

### 1. Запустите базу данных через Docker

```bash
docker-compose up -d
```

> Фронт и бэк будут подключаться к ней по `localhost:5432`.

### 2. Запустите бэкенд

#### Spring Boot:

```bash
cd spring
mvn spring-boot:run
```

#### Ktor:

```bash
cd ktor
./gradlew run
# или на Windows: gradlew.bat run
```

Бэкенд будет доступен на `http://localhost:8080`.

### 3. Запустите фронтенд

```bash
cd react
npm install
npm run dev
```

---

## API эндпоинты

- `GET /api/currencies` — список всех валют
- `GET /api/currencies/{id}` — данные по валюте по ID

---

## Очистка

Остановить всё:

```bash
docker-compose down
```

Остановить с удалением томов:

```bash
docker-compose down -v
```

---

## Примечания

- Фронтенд всегда обращается к `http://localhost:8080`, поэтому **запущен должен быть только один бэкенд**.
- При работе без Docker убедитесь, что в `.env` указано:
  ```env
  DB_URL=jdbc:postgresql://localhost:5432/db
  ```
- Для Ktor включён CORS для `localhost:3000`.
- База данных инициализируется автоматически при первом запуске.
