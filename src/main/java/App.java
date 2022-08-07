

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class App {
    /**
     * num of maxim friends possible
     */
    private final int MAX_FRIENDS = 10;
    /**
     * array where all friends are added.
     */
    private final Friend[] friends = new Friend[10];

    /**
     * Adds new friend to array Friend[]
     *
     * @param name friends name
     * @throws Exception a
     */
    public void addFriend(String name, int incDays) throws Exception {

        if (this.friendAlreadyExists(name) >= 0) {

            throw new Exception("friends name already exists");
        }

        for (int i = 0; i < MAX_FRIENDS; i++) {

            if (friends[i] == null) {

                friends[i] = new Friend(name, incDays);

                return;
            }
        }


        throw new Exception("Not room for more friends :/");

    }

    private void friendLoader(String name, LocalDate nextDate, int incDays) throws Exception {

        if (this.friendAlreadyExists(name) >= 0) {

            throw new Exception("friends name already exists");
        }

        for (int i = 0; i < MAX_FRIENDS; i++) {

            if (friends[i] == null) {

                try {
                    friends[i] = new Friend(name, nextDate, incDays);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.printf("incorrect format: name=[%s], nextDate=[%s], incDays=[%d]\n", name, nextDate.toString(), incDays);
                    //skip this entry
                }

                return;
            }
        }


        throw new Exception("Not room for more friends :/");

    }

    /**
     * Remove friend from array Friend[]
     *
     * @param name friends name
     */
    public void removeFriend(String name) {

        int i = this.friendAlreadyExists(name);

        if (i >= 0) {

            friends[i] = null;

        }
    }

    /**
     * checks if friends name is in use already
     *
     * @param name friends name
     * @return no. of entry of the name or -1 if name doesn't exist in the array.
     */
    public int friendAlreadyExists(String name) {

        for (int i = 0; i < MAX_FRIENDS; i++) {

            if (friends[i] == null) {

                continue;
            }

            if (name.equals(friends[i].getName())) {

                return i;
            }
        }

        return -1;
    }

    /**
     * @return all not null entries from the array Friend[]
     */


    public List<Friend> getFriendList() {

        List<Friend> list = new ArrayList<>();

        for (Friend elem : friends) {

            if (elem != null) {

                list.add(elem);

            }
        }

        return list;
    }

    public List<Friend> getFriendListSortedByNextDate() {
        List<Friend> list = getFriendList();
        list.sort(Comparator.comparing(Friend::getNextDate));
        //list.sort((Friend a, Friend b) -> a.getNextDate().compareTo(b.getNextDate()));

        return list;
    }

    /*
        private static int compareFriendByName(Friend a, Friend b) {
            String aName = a.getName();
            String bName = b.getName();
            return aName.compareTo(bName);
        }
    */
    public List<Friend> getFriendListSortedByName() {
        List<Friend> list = getFriendList();
        //list.sort(App::compareFriendByName);

        list.sort(Comparator.comparing(Friend::getName));

        return list;
    }

    /**
     * @param name friends name
     * @return one specific friend from the array
     */
    public Friend getFriend(String name) throws Exception {

        int i = this.friendAlreadyExists(name);

        if (i >= 0) {

            return friends[i];

        } else {

            throw new Exception("this friend doesn't exist");
        }

    }

    /**
     * @return Array of friends converted into a String of data.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (Friend elem : friends) {
            if (elem != null) {
                sb.append(elem).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public void loadData() throws Exception {

        for (int i = 0; i < MAX_FRIENDS; i++) {

            friends[i] = null;
        }

        try {
            File file = new File("friendList.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                // Tratamiento linea en blanco
                if (line.strip().equals("")) continue;
                // Tratamiento linea incorrecta
                if (line.length() == line.replace(",", "").length() - 2) {
                    throw new Exception(String.format("Bad line: %s", line));
                }
                String[] fields = line.split(",");
                this.friendLoader(fields[0], LocalDate.parse(fields[1]), Integer.parseInt(fields[2]));
            }
            br.close();
        } catch (IOException e) {
            System.out.println("couldn't load friendlist.txt");
            System.exit(-1);
        }
    }

    public void saveData() {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("friendList.txt"));
            writer.write(this.toString());
            writer.close();
        } catch (IOException e) {

            System.out.println("couldn't save data");
            System.exit(-1);
        }

    }

    public void updateFriend(String name) throws Exception {
        Friend elem = getFriend(name);
        elem.setNextDate();
    }

    public void updateFriends() {

        for (Friend elem : friends) {

            if (elem != null && elem.needUpdate()) {
                elem.setNextDate();
            }
        }
    }

    public void editNextDateManual(String name, LocalDate nextDate) throws Exception {

        Friend elem = getFriend(name);

        elem.setCustomNextDate(nextDate);

    }
}
