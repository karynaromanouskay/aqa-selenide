import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {

    private String dateGenerate(int addDays) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    @Test
    public void shouldSuccessDeliveryCard() {
        open("http://localhost:9999");

        String expectedDate = dateGenerate(21);
        $("[data-test-id='city'] input").setValue("Тамбов");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(expectedDate);
        $("[data-test-id='name'] input").setValue("Романовская Карина Владимировна");
        $("[data-test-id='phone'] input").setValue("+79163305338");
        $("[data-test-id=agreement]").click();

        $$("button").find(Condition.exactText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.hidden, Duration.ofSeconds(100));
        $(withText("Встреча успешно забронирована")).shouldBe(Condition.hidden, Duration.ofSeconds(100));

        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(100));
        $("[data-test-id=notification]").shouldHave(Condition.text("Успешно!\n" + "Встреча успешно забронирована на " + expectedDate)).shouldBe(Condition.visible);
    }
}