import java.time.LocalDate;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class Friend {
    /**
     * Friends name
     */
    private String name;
    /**
     * date friend was first added
     */
    private LocalDate initDate;
    /**
     * Next notification date
     */
    private LocalDate nextDate;

    /**
     * @param name creates instance af Friend with introduced name
     */
    public Friend(String name) {
        setName(name);
        this.nextDate = LocalDate.now().plusDays(7);
    }

    public Friend(String name, LocalDate nextDate) {
        setName(name);
        this.nextDate = nextDate;
    }

    /* public Friend(String name, LocalDate nextDate, int incDays){
        setName(name);
        this.nextDate = nextDate;
        this.incDays = incDays


    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public LocalDate getNextDate() {
        return this.nextDate;
    }

    /**
     * @return true if notification is needed.
     */

    public boolean checkDate() {

        return (remainingDays() == 0);

    }

    /**
     * @return true if nextDate needs to be updated to next week.
     */
    public boolean needUpdate() {

        return (remainingDays() <= -1);
    }

    /**
     * Increases nextDate by 6 days. Check happens on -1 days, so in order to make it a full week we
     * only add 6 days.
     */
    public void setNextDate() {

        while (this.nextDate.compareTo(LocalDate.now()) < 0) {

            this.nextDate = this.nextDate.plusDays(7);
        }
    }

    public void setCustomNextDate(LocalDate customNextDate) throws Exception {

        if (customNextDate.isBefore(LocalDate.now())) {
            throw new Exception("this date is in the past already, move on");
        } else {
            this.nextDate = customNextDate;
        }
    }

    /**
     * Converts the attributes to String.
     */
    @Override
    public String toString() {

        String s = getName() + "," + getNextDate().toString();

        return s;
    }

    /**
     * @return returns same as previous method + remaining days.
     */
    public String showData() {

        String friendColor;
        String daysRemaining;

        // Past dates are negatives.
        if (this.remainingDays() < 0) {

            friendColor = Colors.ANSI_RED;
            daysRemaining = " || you're " + -remainingDays() + " days late!!";

            // Future dates are positive
        } else if (remainingDays() > 0) {

            friendColor = Colors.ANSI_GREEN;
            daysRemaining = " || days remaining " + remainingDays();

            // Today is case 0
        } else {     // In case I ony want to use 3 colors.

            friendColor = Colors.ANSI_YELLOW;
            daysRemaining = " || disappointment is today!!";

        }

        String s = friendColor + " --> " + getName() + daysRemaining +
                " || " + getNextDate().getDayOfWeek() + " <-- " + System.lineSeparator() + Colors.ANSI_RESET;

        return s;
    }


    /**
     * @return number of days remaining until nextDate.
     */
    public long remainingDays() {

        return DAYS.between(LocalDate.now(), nextDate);

    }

    public static void main(String args[]) {

        LocalDate x = LocalDate.now();
        System.out.println(x);

        x = x.plusDays(7);
        System.out.println(x);

        Friend y = new Friend("Diego");

        System.out.println(y.initDate);

    }

}
