import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.rmi.server.ExportException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {

    private LocalDate deliveryDate(int delay) {
        return LocalDate.now().plusDays(delay);
    }

    private String expectedNotifyText(String date) {
        return String.format("Успешно!\nВстреча успешно забронирована на %1$s", date);
    }

    private SelenideElement inputByDataTestId(String id, String type) {
        return $(String.format("[data-test-id='%s'] %s", id, type));
    }

    private SelenideElement cityInput() {
        return inputByDataTestId("city", "input");
    }

    private SelenideElement dateInput() {
        return inputByDataTestId("date", "input");
    }

    private SelenideElement dateIcon() {
        return inputByDataTestId("date", "span.icon_name_calendar");
    }

    private SelenideElement nameInput() {
        return inputByDataTestId("name", "input");
    }

    private SelenideElement phoneInput() {
        return inputByDataTestId("phone", "input");
    }

    private SelenideElement agreementCheckbox() {
        return inputByDataTestId("agreement", "");
    }

    private SelenideElement submitButton() {
        return $$("button").find(Condition.exactText("Забронировать"));
    }

    private SelenideElement notificationAlert() {
        return $("[data-test-id=notification]");
    }


    private void selectCity(String CityName,
                            int lettersCount) {
        String prefix = CityName.substring(0, lettersCount); // "Тамбов" -> "Та"
        cityInput()
                .setValue(prefix);

        $$("div.menu-item_type_block>span.menu-item__control").find(Condition.exactText(CityName)).click();
    }

    private void selectDate(LocalDate expectedDate) {
        long timeStamp = expectedDate.atStartOfDay(ZoneId.of("Europe/Moscow")).toEpochSecond() * 1000;

        dateIcon().click();

        String cssSelectorPattern = "table.calendar__layout>tbody>tr>td.calendar__day[data-day=\"%1$s\"]";
        String cssSelectorText = String.format(cssSelectorPattern, timeStamp);

        if ($$(cssSelectorText).isEmpty()){
            $("div.calendar__title>div[role=\"button\"][data-step=\"1\"]")
                    .click();
        }
        $(cssSelectorText).click();

    }

    @Test
    public void shouldSuccessDeliveryCard() {
        open("http://localhost:9999");

        String expectedDate = deliveryDate(21).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


        cityInput()
                .setValue("Тамбов");
        dateInput()
                .doubleClick()
                .sendKeys(expectedDate);
        nameInput()
                .setValue("Романовская Карина Владимировна");
        phoneInput()
                .setValue("+79163305338");
        agreementCheckbox()
                .click();

        submitButton()
                .click();
        notificationAlert()
                .shouldBe(Condition.hidden, Duration.ofSeconds(100))
                .shouldBe(Condition.visible, Duration.ofSeconds(100))
                .shouldHave(Condition.text(expectedNotifyText(expectedDate)));
    }

    @Test
    public void shouldSuccessCityEnter() {
        open("http://localhost:9999");

        LocalDate expectedDate = deliveryDate(30);
        String expectedDateText = expectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        selectCity("Тамбов", 2);
        selectDate(expectedDate);

        nameInput()
                .setValue("Романовская Карина Владимировна");
        phoneInput()
                .setValue("+79163305338");
        agreementCheckbox()
                .click();

        submitButton()
                .click();
        notificationAlert()
                .shouldBe(Condition.hidden, Duration.ofSeconds(100))
                .shouldBe(Condition.visible, Duration.ofSeconds(100))
                .shouldHave(Condition.text(expectedNotifyText(expectedDateText)));
    }

}