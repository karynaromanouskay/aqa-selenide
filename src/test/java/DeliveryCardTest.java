import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.
public class DeliveryCardTest {

    private String dateGenerate(int addDays, String dateFormat) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(dateFormat));
    }
    @Test
    public void shouldSuccessDeliveryCard() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Тамбов");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(dateGenerate(21, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Романовская Карина Владимировна");
        $("[data-test-id='phone'] input").setValue("+79163305338");
        $("[data-test-id=agreement]").click();

        $$("button").find(Condition.exactText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(Condition.hidden, Duration.ofSeconds(100));
        $(withText("Встреча успешно забронирована")).shouldBe(Condition.hidden, Duration.ofSeconds(100));

        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(100));
        $("[data-test-id=notification]").shouldHave(Condition.text("Успешно!\n" + "Встреча успешно забронирована на " + dateGenerate(7, "dd.MM.yyyy"))).shouldBe(Condition.visible);
    }
}