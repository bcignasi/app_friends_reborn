import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    // Este es el objeto de clase App sobre el que hacemos las pruebas
    App app;

    // Guardo aquí el resultado de getFriendList(), para usarlo en varios tests luego
    List<Friend> lista;

    // Fecha de hoy para ajustar fechas anteriores y posteriores en las siguientes variables
    LocalDate today = LocalDate.now();

    // Fechas relativas a hoy (en formato texto)
    String fechaHoy = today.toString();
    String fechaMaria = fechaHoy;
    String fechaMas6 = today.plusDays(6).toString();
    // Asigno esas fechas relativas a cada usuario para no liarme al aplicarlas
    String fechaPepe = fechaMas6;
    String fechaMenos1 = today.plusDays(-1).toString();
    String fechaJuan = fechaMenos1;
    String fechaMenos2 = today.plusDays(-2).toString();
    String fechaIgnasi = fechaMenos2;
    String fechaMenos16 = today.plusDays(-16).toString();
    String fechaGuille = fechaMenos16;
    String fechaMas5 = today.plusDays(+5).toString();
    String fechaGuilleActualizada = fechaMas5;

    // La lista de usuarios con las fechas sincronizadas a día de hoy
    String mockingList = String.format("""
            pepe,%s,7
            maria,%s,1
            ignasi,%s,4
            guille,%s,7
            juan,%s,7
            """, fechaPepe, fechaMaria, fechaIgnasi, fechaGuille, fechaJuan);
    //String fechaMas1 = today.plusDays(1).toString();
    //String fechaMas2 = today.plusDays(2).toString();
    String fechaMas16 = today.plusDays(16).toString();
    String fechaScarlett = fechaMas16;
    // Para el mocking guardo la friendList.txt original en friendList.txt.bak
    // Y así la recreo para las pruebas, al final restauraré la original
    String friendListBackupFile = "friendList.txt.bak";
    String friendListFile = "friendList.txt";

    // Guardo la friendList original y la sustituyo por mis datos mockup
    void setMockingFile() throws IOException {
        if (Files.exists(Path.of(friendListFile))) {
            Files.copy(Path.of(friendListFile),
                    Path.of(friendListBackupFile),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        BufferedWriter br = new BufferedWriter(new FileWriter(friendListFile));
        br.write(mockingList);
        br.close();
    }

    // Restaura la friendList original, borrando la de prueba
    void unsetMockingFile() throws IOException {
        if (Files.exists(Path.of(friendListBackupFile))) {
            Files.copy(Path.of(friendListBackupFile),
                    Path.of(friendListFile),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.delete(Path.of(friendListBackupFile));
        }
    }

    // Devuelve el contenido del fichero friendList (el de pruebas)
    String readMockingFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(friendListFile));
        String friendListString = br.lines().collect(Collectors.joining());
        br.close();
        return friendListString;
    }

    ///////////////////////////////////////////////////////////////////////////

    // Esta función de JUnit se ejecuta ANTES de cada test
    // Crea el fichero de prueba, crea la app, carga los datos y obtiene la lista de amigos
    @BeforeEach
    void setUp() throws Exception {
        setMockingFile();
        app = new App();
        app.loadData();
        lista = app.getFriendList();
    }

    // Esta función de JUnit se ejecuta DESPUÉS de cada test
    // Lo único que hace es restaurar la lista de amigos original
    @AfterEach
    void tearDown() throws IOException {
        unsetMockingFile();
    }

    ///////////////////////////////////////////////////////////////////////////

    @Test
    void comprobar_se_ha_cargado_el_fichero_de_datos() {
        assertEquals(5, lista.size());
    }

    @Test
    void comprobar_primero_y_ultimo() {
        Friend friend = lista.get(0);
        assertEquals("pepe", friend.getName());
        assertEquals(fechaPepe, friend.getNextDate().toString());
        assertEquals(7, friend.getIncDays());

        friend = lista.get(lista.size() - 1);
        assertEquals("juan", friend.getName());
        assertEquals(fechaJuan, friend.getNextDate().toString());
        assertEquals(7, friend.getIncDays());
    }

    // NOTA El méremoveFriend() no da información de si el borrado funcionó o no
    @Test
    void comprobar_borrado_amigo_existente_e_inexistente() {
        app.removeFriend("ignasi");
        List<Friend> result = app.getFriendList();
        assertEquals(4, result.size());

        app.removeFriend("scarlett");
        result = app.getFriendList();
        assertEquals(4, result.size());
    }

    /*
    NOTA Es incómodo que una función que sugiere devolver true/false
        devuelva en realidad un integer. Sería mejor que esta función
        devolviera true/false y existiera otra getFriedIndex() que
        devolviera el índice propiamente (y que serviría para construir esta)
      */

    @Test
    void comprobar_control_existencia_amigos() {
        int index = app.friendAlreadyExists("ignasi");
        assertEquals(2, index);
        index = app.friendAlreadyExists("scarlett");
        assertEquals(-1, index);
    }

    // NOTA Lo hago indirectamente, comprobando el toString() de List<Friend>
    @Test
    void comprobar_getList() {
        String expected = String.format("[pepe,%s,7, maria,%s,1, ignasi,%s,4, guille,%s,7, juan,%s,7]",
                fechaPepe, fechaMaria, fechaIgnasi, fechaGuille, fechaJuan);
        assertEquals(expected, app.getFriendList().toString());
    }

    @Test
    void comprobar_getFriend() throws Exception {
        String expected = String.format("ignasi,%s,4", fechaIgnasi);
        Friend friend = app.getFriend("ignasi");
        assertEquals(expected, friend.toString());
    }

    // NOTA El toString de la clase es el contenido del fichero friendList.txt
    @Test
    void comprobar_ToString() {
        assertEquals(mockingList, app.toString().replace("\r", ""));
    }

    // NOTA Desactivado porque, en realidad, esto queda testeado en el setup()
    /*
    @Test
    void loadData() {
    }
    */

    /*
    NOTA saveData() se hace aparte, quizás sería mejor hacerlo en la propia addFriend()
        y de esta forma se eliminan todas las referencias en Main (más encapsulación)
     */
    @Test
    void comprobar_save_ingresando_y_borrando_friend() throws Exception {

        String expected = String.format("scarlett,%s,1", fechaScarlett);

        app.addFriend("scarlett", 1);
        app.editNextDateManual("scarlett", LocalDate.parse(fechaScarlett));
        app.saveData();

        String friendList = readMockingFile();
        assertTrue(friendList.contains(expected));

        app.removeFriend("scarlett");
        app.saveData();

        friendList = readMockingFile();
        assertFalse(friendList.contains("scarlett"));
    }

    // Ejecuta la actualización y comprueba que todas las fechas son futuras
    @Test
    void comprobar_que_updateFriends_convierte_todas_las_fechas_en_futuras() {
        LocalDate today = LocalDate.now();
        app.updateFriends();
        for (Friend friend : app.getFriendList()) {
            LocalDate ld = friend.getNextDate();
            assertFalse(ld.isBefore(today));
        }
    }

    @Test
    void comprobar_edicion_manual_nextDate() throws Exception {
        // Fecha futura (respecto a la actual)
        app.editNextDateManual("ignasi", LocalDate.parse(fechaMas16));
        Friend friend = app.getFriend("ignasi");
        assertEquals(fechaMas16, friend.getNextDate().toString());

        // Fecha actual
        app.editNextDateManual("ignasi", LocalDate.parse(fechaHoy));
        friend = app.getFriend("ignasi");
        assertEquals(fechaHoy, friend.getNextDate().toString());

        // Fecha pasada - Actualmente no se permite, da un error (véase función comprobar_errores() a continuación)
        //app.editNextDateManual("ignasi", LocalDate.parse(fechaMenos2));
        //friend = app.getFriend("ignasi");
        //assertEquals(fechaMenos2, friend.getNextDate().toString());
    }

    // Comprueba que no es posible asignar una fecha pasada (da Exception)
    @Test
    void comprobar_errores() {
        assertThrows(Exception.class,
                () -> app.editNextDateManual("ignasi", LocalDate.parse("2022-07-20")));
    }

    @Test
    void comprobar_updateNextDate_para_un_amigo_solo() throws Exception {
        app.updateFriend("guille");
        Friend guille = app.getFriend("guille");
        assertEquals(fechaGuilleActualizada, guille.getNextDate().toString());
    }
}
