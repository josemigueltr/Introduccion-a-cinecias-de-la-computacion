package mx.unam.ciencias.icc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.fx.ControladorInterfazCelular;
import mx.unam.ciencias.icc.fx.ControladorTablaCelular;

/**
 * ClientePractica10: Parte del cliente para práctica 10: Hilos de ejecución y
 * enchufes.
 */
public class ClienteProyecto2 extends Application {

    /* Vista de la interfaz estudiantes. */
    private static final String INTERFAZ_CELULAR_FXML =
        "fxml/interfaz-celular.fxml";
    /* Vista de la tabla de estudiantes. */
    private static final String TABLA_CELULAR_FXML =
        "fxml/tabla-celular.fxml";
    /* Ícono de la Facultad de Ciencias. */
    private static final String ICONO_CIENCIAS =
        "icons/ciencias.png";

    /**
     * Inicia la aplicación.
     * @param escenario la ventana principal de la aplicación.
     * @throws Exception si algo sale mal. Literalmente así está definido.
     */
    @Override public void start(Stage escenario) throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        String url = cl.getResource(ICONO_CIENCIAS).toString();
        escenario.getIcons().add(new Image(url));
        escenario.setTitle("Administrador de Celulares");

        FXMLLoader cargador =
            new FXMLLoader(cl.getResource(TABLA_CELULAR_FXML));
        GridPane tablaCelular = (GridPane)cargador.load();
        ControladorTablaCelular ControladorTablaCelular =
            cargador.getController();

        cargador = new FXMLLoader(cl.getResource(INTERFAZ_CELULAR_FXML));
        BorderPane interfazCelular= (BorderPane)cargador.load();
        interfazCelular.setCenter(tablaCelular);
        ControladorInterfazCelular controladorInterfazCelular =
            cargador.getController();
        controladorInterfazCelular.setEscenario(escenario);
        controladorInterfazCelular.setControladorTablaCelular(
            ControladorTablaCelular);

        Scene escena = new Scene(interfazCelular);
        escenario.setScene(escena);
        escenario.setOnCloseRequest(e -> controladorInterfazCelular.salir(null));
        escenario.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
