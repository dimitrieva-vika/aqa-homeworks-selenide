package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class OrderCardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        // Настройки не требуются - Selenide использует значения по умолчанию
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    /**
     * Тест-кейс №1: Загрузка страницы
     */
    @Test
    void shouldHaveCorrectDefaultValues() {
        $("[data-test-id='city'] input").shouldHave(Condition.value(""));
        $("[data-test-id='city'] .input__sub").shouldHave(text("Выберите ваш город"));

        String expectedDate = formatDate(LocalDate.now().plusDays(3));
        $("[data-test-id='date'] input").shouldHave(Condition.value(expectedDate));
        $("[data-test-id='date'] .input__sub").shouldHave(text("Выберите дату встречи с представителем банка"));

        $("[data-test-id='name'] .input__sub").shouldHave(text("Укажите точно как в паспорте"));
        $("[data-test-id='phone'] .input__sub").shouldHave(text("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно."));
        $("[data-test-id='agreement'] .checkbox__control").shouldNotBe(checked);
    }

    /**
     * Тест-кейс №2: Валидные данные
     */
    @Test
    void shouldSubmitValidForm() {
        String city = "Москва";
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван Иванов";
        String phone = "+79270000000";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        $(".spin").shouldBe(visible);
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__title").shouldHave(text("Успешно!"));
        $("[data-test-id='notification'] .notification__content").shouldHave(text("Встреча успешно забронирована на " + date));
    }

    /**
     * Тест-кейс №3: Пустой город
     */
    @Test
    void shouldShowErrorWhenCityIsEmpty() {
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван Иванов";
        String phone = "+79270000000";

        // Очищаем город
        $("[data-test-id='city'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='city'] input").sendKeys(Keys.DELETE);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        $("[data-test-id='city'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    /**
     * Тест-кейс №4: Некорректный город
     */
    @Test
    void shouldShowErrorWhenCityIsInvalid() {
        String city = "New York";
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван Иванов";
        String phone = "+79270000000";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        $("[data-test-id='city'] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    /**
     * Тест-кейс №5: Пустая дата
     * Проверяем, что появляется сообщение об ошибке
     */
    @Test
    void shouldShowErrorWhenDateIsEmpty() {
        String city = "Москва";
        String name = "Иван Иванов";
        String phone = "+79270000000";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        // Проверяем текст ошибки (не класс, так как класс может не добавляться)
        $("[data-test-id='date'] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    /**
     * Тест-кейс №7: Пустое имя
     */
    @Test
    void shouldShowErrorWhenNameIsEmpty() {
        String city = "Москва";
        String date = formatDate(LocalDate.now().plusDays(4));
        String phone = "+79270000000";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        // Очищаем имя
        $("[data-test-id='name'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='name'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        $("[data-test-id='name'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    /**
     * Тест-кейс №8: Имя с цифрами
     */
    @Test
    void shouldShowErrorWhenNameContainsDigits() {
        String city = "Москва";
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван123";
        String phone = "+79270000000";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        String expectedError = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        $("[data-test-id='name'] .input__sub").shouldHave(text(expectedError));
    }

    /**
     * Тест-кейс №9: Пустой телефон
     */
    @Test
    void shouldShowErrorWhenPhoneIsEmpty() {
        String city = "Москва";
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван Иванов";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);

        // Очищаем телефон
        $("[data-test-id='phone'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='phone'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        $("[data-test-id='phone'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    /**
     * Тест-кейс №10: Некорректный телефон
     */
    @Test
    void shouldShowErrorWhenPhoneIsInvalid() {
        String city = "Москва";
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван Иванов";
        String phone = "+7(927)000-00-00";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement'] .checkbox__box").click();

        $$(".button__text").find(text("Забронировать")).click();

        String expectedError = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        $("[data-test-id='phone'] .input__sub").shouldHave(text(expectedError));
    }

    /**
     * Тест-кейс №11: Неотмеченный чекбокс
     * Проверяем, что появляется ошибка (красный текст)
     */
    @Test
    void shouldShowErrorWhenAgreementNotChecked() {
        String city = "Москва";
        String date = formatDate(LocalDate.now().plusDays(4));
        String name = "Иван Иванов";
        String phone = "+79270000000";

        $("[data-test-id='city'] input").setValue(city);

        // Очищаем и заполняем дату
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);

        // Снимаем отметку с чекбокса
        if ($("[data-test-id='agreement'] .checkbox__control").isSelected()) {
            $("[data-test-id='agreement'] .checkbox__box").click();
        }

        $$(".button__text").find(text("Забронировать")).click();

        // Проверяем, что текст чекбокса стал красным
        // Используем проверку CSS свойства color
        String color = $("[data-test-id='agreement'] .checkbox__text").getCssValue("color");
        // Проверяем, что цвет содержит красный компонент
        boolean isRed = color.contains("rgb(255") || color.contains("255");
        assert isRed : "Текст чекбокса должен стать красным. Текущий цвет: " + color;
    }

    /**
     * Тест-кейс №12: Все поля пустые
     */
    @Test
    void shouldShowErrorOnlyForCityWhenAllFieldsEmpty() {
        // Очищаем все поля
        $("[data-test-id='city'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='city'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='name'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='name'] input").sendKeys(Keys.DELETE);

        $("[data-test-id='phone'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='phone'] input").sendKeys(Keys.DELETE);

        $$(".button__text").find(text("Забронировать")).click();

        // Проверяем, что только поле "Город" получило ошибку
        $("[data-test-id='city'] .input__sub").shouldHave(text("Поле обязательно для заполнения"));

        // Проверяем, что остальные поля не имеют ошибок
        $("[data-test-id='date'] .input__sub").shouldHave(text("Выберите дату встречи с представителем банка"));
        $("[data-test-id='name'] .input__sub").shouldHave(text("Укажите точно как в паспорте"));
        $("[data-test-id='phone'] .input__sub").shouldHave(text("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно."));
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }
}