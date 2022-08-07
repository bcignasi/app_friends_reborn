import java.io.File;
import java.time.LocalDate;

public class Main {


    static LocalDate today = null;

    static App app;

    static {
        app = new App();
    }

    public static void printMenu() {

        String menu = """
                                
                                
                1.Show friend list (by date)
                2.Create friend
                3.Delete friend
                4.Show friend
                5.Edit friend
                6.Sort friend by name
                7. Update friend nextDate
                0.Exit
                                
                                
                """;


        System.out.println(menu);
    }


/*
        boolean mustRepeat = true;
        int option = 0;
        do {
            System.out.print("Elija una opción (0 para salir): ");
            if (scanner.hasNextInt()) {     // Comprueba si tenemos un int a la entrada
                option = scanner.nextInt(); // Caso que sí, lo leemos
                scanner.nextLine();         // Scanner debe leer el resto de la línea (el carácter \n) para descartarla
                mustRepeat = false;         // Ya podemos salir
            } else if (scanner.hasNextLine()) {     // Si no hay int, leemos la línea para descartarla
                scanner.nextLine();
                System.out.println("Error. Se espera un número");
            }
        } while (mustRepeat);

        return option;
*/

    public static void executeOption(int option) throws Exception {


        switch (option) {
            case 1 -> {
                System.out.println();
                for (Friend elem : app.getFriendListSortedByNextDate()) {
                    System.out.println(elem.showData());
                }
                System.out.println();
            }
            case 2 -> {
                System.out.println("enter name to add friend");
                String lovedFriend = Input.getString();
                if (lovedFriend.isBlank()) {
                    System.out.println("name can't be empty");
                    break;
                }
                if (app.friendAlreadyExists(lovedFriend) >= 0) {
                    System.out.println("name already in use by some other of your stupid friends (you're stupid as well");
                    break;
                }
                System.out.println("enter days between appointments");
                int incDaysOfFriend = Input.getNumber();
                app.addFriend(lovedFriend, incDaysOfFriend);
                app.saveData();
            }
            case 3 -> {
                System.out.println("enter name to delete friend");
                String hatedFriend = Input.getString();
                app.removeFriend(hatedFriend);
                app.saveData();
            }
            case 4 -> {
                System.out.println("enter name of friend");
                String inquiredFriend = Input.getString();
                if (app.friendAlreadyExists(inquiredFriend) == -1) {

                    System.out.println("friend doesn't exist");
                    break;
                }
                Friend thatFriend = app.getFriend(inquiredFriend);
                System.out.println("\n" + thatFriend.showData() + "\n");
            }
            case 5 -> {
                System.out.println("enter name of friend");
                String selectedFriend = Input.getString();
                try {
                    Friend theFriend = app.getFriend(selectedFriend);
                    System.out.println("\n" + theFriend.toString() + "\n");
                    System.out.println("""
                            1 = edit name\s
                            2 = edit date\s
                            3 = edit days between appointments""".indent(1));

                    int opt = Input.getNumber();

                    switch (opt) {
                        case 1 -> {
                            System.out.println("enter new name");
                            String newName = Input.getString();
                            if (app.friendAlreadyExists(newName) > -1) {

                                System.out.println("name already in use");
                                break;
                            }
                            try {
                                theFriend.setName(newName);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        case 2 -> {
                            System.out.println("enter new date, format yyyy-mm-dd");
                            LocalDate newDate2 = Input.getDate();
                            if (newDate2 != null) {
                                app.editNextDateManual(theFriend.getName(), newDate2);
                            }
                        }
                        case 3 -> {
                            System.out.println("enter number of days");
                            int numberOfDays = Input.getNumber();
                            try {
                                theFriend.setIncDays(numberOfDays);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        default -> System.out.println("you don't wanna collaborate, okay");
                    }

                    app.saveData();

                } catch (Exception e) {

                    System.out.println(e.getMessage());
                }
            }

            case 6 -> {

                System.out.println();
                for (Friend elem : app.getFriendListSortedByName()) {
                    System.out.println(elem.showData());
                }
                System.out.println();
            }

            case 7 -> {
                System.out.println("enter name of friend");
                String inquiredFriend = Input.getString();
                if (app.friendAlreadyExists(inquiredFriend) == -1) {

                    System.out.println("friend doesn't exist");
                    break;
                }
                Friend thatFriend = app.getFriend(inquiredFriend);
                thatFriend.setNextDate();
                System.out.println("\n" + thatFriend.showData() + "\n");

            }
        }
    }

    private static void updateTime() {

        if (today == null || today.compareTo(LocalDate.now()) < 0) {
            today = LocalDate.now();
            app.updateFriends();
        }
    }


    public static void main(String[] args) throws Exception {

        File f = new File("friendList.txt");
        if (f.isFile()) {

            app.loadData();
        }
        app.saveData();

        while (true) {

            printMenu();

            int option = Input.getNumber();

            if (option == 0) {
                System.out.println("get fucked");
                break;
            }
            executeOption(option);
            app.saveData();
        }
    }
}
