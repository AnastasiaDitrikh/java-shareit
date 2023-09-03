version: '3.8'
services:
  gateway:
    build: ./gateway
    container_name: gateway
    ports:
      - "8080:8080"
    depends_on:
      - server
      - db
    environment:
      - SHAREIT_SERVER_URL=http://server:9090

  server:
    build: ./server
    container_name: server
    ports:
      - "9090:9090"
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/shareit

  db:
    # образ, из которого должен быть запущен контейнер
    image: postgres:13.7-alpine
    container_name: db
    ports:
      - "5432:5432"
    # volume и связанная с ним директория в контейнере
    volumes:
      - /var/lib/postgresql/data/
    # переменные окружения
    environment:
      - POSTGRES_DB=shareit
      - POSTGRES_USER=shareit
      - POSTGRES_PASSWORD=shareit