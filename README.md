# Домашнее задание: Тестирование веб-интерфейсов (Selenide)

[![CI](https://github.com/dimitrieva-vika/aqa-homeworks-selenide/actions/workflows/gradle.yml/badge.svg)](https://github.com/dimitrieva-vika/aqa-homeworks-selenide/actions/workflows/gradle.yml)

## Описание проекта

Автотесты для формы заказа доставки карты (SUT: app-card-delivery.jar) с использованием Selenide.

## Статус тестирования

| Тест | Статус | Описание |
|------|--------|----------|
| shouldHaveCorrectDefaultValues | ✅ PASSED | Проверка значений по умолчанию |
| shouldSubmitValidForm | ✅ PASSED | Отправка формы с валидными данными |
| shouldShowErrorWhenCityIsEmpty | ✅ PASSED | Пустой город |
| shouldShowErrorWhenCityIsInvalid | ✅ PASSED | Некорректный город |
| shouldShowErrorWhenDateIsEmpty | ✅ PASSED | Пустая дата |
| shouldShowErrorWhenNameIsEmpty | ✅ PASSED | Пустое имя |
| shouldShowErrorWhenNameContainsDigits | ✅ PASSED | Имя с цифрами |
| shouldShowErrorWhenPhoneIsEmpty | ✅ PASSED | Пустой телефон |
| shouldShowErrorWhenPhoneIsInvalid | ✅ PASSED | Некорректный телефон |
| shouldShowErrorWhenAgreementNotChecked | ✅ PASSED | Неотмеченный чекбокс |
| shouldShowErrorOnlyForCityWhenAllFieldsEmpty | ✅ PASSED | Все поля пустые |

**Итог: ✅ Все 11 тестов успешно пройдены.**

## Технологии

- Java 11
- JUnit 5
- Selenide 6.19.1
- Gradle 8.14.5
- GitHub Actions (CI)

## Запуск тестов

### Локальный запуск

1. Запустите тестируемое приложение:
   ``` bash
   java -jar ./artifacts/app-card-delivery.jar
   ```

2. В другом терминале выполните:
   ``` bash
   ./gradlew test
   ```

### Запуск в CI

Автотесты автоматически запускаются в GitHub Actions при каждом push в ветку main.

## Структура проекта

```
aqa-homeworks-selenide/
├── .github/
│   └── workflows/
│       └── gradle.yml          # CI настройка
├── artifacts/
│   └── app-card-delivery.jar   # Тестируемый сервис
├── src/
│   └── test/
│       └── java/
│           └── ru/
│               └── netology/
│                   └── web/
│                       └── OrderCardDeliveryTest.java  # Автотесты
├── .gitignore
├── README.md
├── build.gradle
└── settings.gradle
```