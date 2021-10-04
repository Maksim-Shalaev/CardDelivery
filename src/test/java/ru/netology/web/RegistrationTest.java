package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RegistrationTest {

    LocalDate date = LocalDate.now();
    LocalDate newDate = date.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void shouldSubmittingAForm() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79001112233");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $(Selectors.withText("Успешно!")).shouldBe(Condition.appear, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + formatter.format(newDate)));
    }

    @Test
    void shouldCityNotFromTheList() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Солт-Лейк Сити");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79001112233");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=city] .input__sub").shouldBe(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNonRussianLetters() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Hugh Jackman");
        $("[data-test-id=phone] input").setValue("+79001112233");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=name] .input__sub").shouldBe(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldWithoutDate() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("+79001112233");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=date] .input__sub").shouldBe(Condition.text("Неверно введена дата"));
    }

    @Test
    void shouldWithoutSurnameAndFirstname() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79001112233");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=name] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWithoutPhoneNumber() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иван Иванов");
        $("[data-test-id=phone] input").setValue("");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=phone] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));
    }


    @Test
    void shouldWithoutCheckbox() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79001112233");
        $("[class=button__text]").click();
        $("[class=checkbox__text]").shouldBe(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldEmptyForm() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("");
        $("[class=button__text]").click();
        $("[data-test-id=city] .input__sub").shouldBe(Condition.text("Поле обязательно для заполнения"));
    }
}
