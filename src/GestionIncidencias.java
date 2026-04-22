import java.sql.*;
import java.util.Scanner;

public class GestionIncidencias {
//Ejercicio 3 Crear una tabla incidencias y implementar un programa que las gestione
    static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    static String usuario = "RIBERA";
    static String password = "ribera";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int opcion;

        do {

            System.out.println("---MENU INCIDENCIAS---");
            System.out.println("1. Insertar incidencia");
            System.out.println("2. Mostrar todas las incidencias");
            System.out.println("3. Mostrar incidencias de un ciclista");
            System.out.println("0. Salir");

            opcion = sc.nextInt();

            switch (opcion) { //Llamamos a los metodos

                case 1:
                    insertarIncidencia(sc, url, usuario, password);
                    break;

                case 2:
                    mostrarTodas(url, usuario, password);
                    break;

                case 3:
                    mostrarPorCiclista(sc, url, usuario, password);
                    break;

            }

        } while (opcion != 0);

        sc.close();
    }

    // INSERTAR INCIDENCIA

    public static void insertarIncidencia(Scanner sc, String url, String usuario, String password) {

        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {

            conexion.setAutoCommit(false); //desactivamos autocomit

            try {

                System.out.print("ID Ciclista: ");
                int idCiclista = sc.nextInt();

                System.out.print("Numero Etapa: ");
                int numeroEtapa = sc.nextInt();
                sc.nextLine();

                System.out.print("Tipo incidencia: ");
                String tipo = sc.nextLine();

                //Verificamos el ciclista

                String sql = "SELECT COUNT(*) FROM ciclista WHERE id_ciclista = ?";

                PreparedStatement psCiclista = conexion.prepareStatement(sql);

                psCiclista.setInt(1, idCiclista);

                ResultSet rsC = psCiclista.executeQuery();

                rsC.next();

                if (rsC.getInt(1) == 0) {

                    System.out.println("El ciclista no existe.");
                    conexion.rollback(); //realizamos rollback si el ciclista no existe
                    return; //salimos.
                }

                //Verificamos la etapa

                String sql2 = "SELECT COUNT(*) FROM etapa WHERE numero = ?";

                PreparedStatement psEtapa = conexion.prepareStatement(sql2);

                psEtapa.setInt(1, numeroEtapa);

                ResultSet rsE = psEtapa.executeQuery();

                rsE.next();

                if (rsE.getInt(1) == 0) {

                    System.out.println("La etapa no existe.");
                    conexion.rollback();
                    return;
                }

                // INSERTAR INCIDENCIA


                String sqlInsert = "INSERT INTO incidencia (id_ciclista, numero_etapa, tipo) VALUES (?, ?, ?)";

                PreparedStatement psInsert = conexion.prepareStatement(sqlInsert);

                psInsert.setInt(1, idCiclista);
                psInsert.setInt(2, numeroEtapa);
                psInsert.setString(3, tipo);

                psInsert.executeUpdate();

                conexion.commit(); //realizamos commit si todo va bien

                System.out.println("Incidencia insertada correctamente.");

            } catch (SQLException e) {

                conexion.rollback(); //si algo falla vamos al catch y realizamos rollback.

                System.out.println("Error: operacion cancelada." + e.getMessage());;
            }

        } catch (SQLException e) {

            System.out.println("Error conexion: " + e.getMessage());
        }
    }

    // MOSTRAR TODAS

    public static void mostrarTodas(String url, String usuario, String password) {

        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {

            String sql = "SELECT i.id_incidencia, c.nombre, " +
                            "i.numero_etapa, i.tipo " +
                            "FROM incidencia i " +
                            "JOIN ciclista c " +
                            "ON i.id_ciclista = c.id_ciclista " +
                            "ORDER BY i.id_incidencia";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            //MOSTRAMOS DATOS

            while (rs.next()) {

                int id =
                        rs.getInt("id_incidencia");

                String nombre =
                        rs.getString("nombre");

                int etapa =
                        rs.getInt("numero_etapa");

                String tipo =
                        rs.getString("tipo");

                System.out.println("[" + id + "] " + nombre + " - Etapa " + etapa + " - " + tipo);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // MOSTRAR POR CICLISTA

    public static void mostrarPorCiclista(Scanner sc, String url, String usuario, String password) {

        System.out.print("ID Ciclista: ");
        int idBuscado = sc.nextInt();

        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {

            String sql = "SELECT i.id_incidencia, c.nombre, " +
                            "i.numero_etapa, i.tipo " +
                            "FROM incidencia i " +
                            "JOIN ciclista c " +
                            "ON i.id_ciclista = c.id_ciclista " +
                            "WHERE i.id_ciclista = ? " +
                            "ORDER BY i.numero_etapa";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idBuscado);

            ResultSet rs = ps.executeQuery();

            //Mostramos datos

            while (rs.next()) {

                int id =
                        rs.getInt("id_incidencia");

                String nombre = rs.getString("nombre");

                int etapa =
                        rs.getInt("numero_etapa");

                String tipo = rs.getString("tipo");

                System.out.println("[" + id + "] " + nombre + " - Etapa " + etapa + " - " + tipo);
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }
}