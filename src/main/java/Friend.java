import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Friend {
    /**
     * Friends name
     */
    private String name;
    /**
     * Next notification date
     */
    private LocalDate nextDate;

    private int incDays;

    /**
     * @param name creates instance af Friend with introduced name
     */
    public Friend(String name, int incDays) throws Exception {
        setName(name);
        setIncDays(incDays);
        this.nextDate = LocalDate.now().plusDays(incDays);
    }

    public Friend(String name, LocalDate nextDate, int incDays) throws Exception {
        setIncDays(incDays);
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

    public void setName(String name) throws Exception {
        if(name == null || name.strip().equals("")){
            throw new Exception("you need to introduce at least one piece of shit fucking character " +
                    "mentally retarded human");
        }
        this.name = name.strip();
    }

    public LocalDate getNextDate() {
        return this.nextDate;
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

            this.nextDate = this.nextDate.plusDays(getIncDays());
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

        return getName() + "," + getNextDate().toString() + "," + getIncDays();
    }

    public int getIncDays() {

        return incDays;
    }

    public void setIncDays(int incDays) throws Exception {

        if (incDays <= 0){

            throw new Exception ("son of a fucking bitch introduce correct number " +
                    "piece of absolute dogshit");
        }

        this.incDays = incDays;

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

        return friendColor + " --> " + getName() + daysRemaining +
                " || " + getNextDate().getDayOfWeek() + " <-- " + System.lineSeparator() + Colors.ANSI_RESET;
    }


    /**
     * @return number of days remaining until nextDate.
     */
    public long remainingDays() {

        return DAYS.between(LocalDate.now(), nextDate);

    }

/*
    public static void main(String args[]) {

        LocalDate x = LocalDate.now();
        System.out.println(x);

        x = x.plusDays(7);
        System.out.println(x);

        Friend y = new Friend("Diego");

        System.out.println(y.initDate);

    }
*/

}
