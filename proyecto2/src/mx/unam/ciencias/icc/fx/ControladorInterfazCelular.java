package mx.unam.ciencias.icc.fx;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.BaseDeDatosCelular;
import mx.unam.ciencias.icc.Celular;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.red.Conexion;
import mx.unam.ciencias.icc.red.ConexionCliente;
import mx.unam.ciencias.icc.red.EventoConexion;
import mx.unam.ciencias.icc.red.Mensaje;

/**
 * Clase para el controlador de la ventana principal de la aplicación.
 */
public class ControladorInterfazCelular {

    /* Vista de la forma para conectarse. */
    private static final String CONECTAR_FXML =
        "fxml/forma-conectar.fxml";
    /* Vista de la forma para realizar búsquedas de celulares. */
    private static final String BUSQUEDA_CELULAR_FXML =
        "fxml/forma-busqueda-celular.fxml";
    /* Vista de la forma para agregar/editar celulares. */
    private static final String CELULAR_FXML =
        "fxml/forma-celular.fxml";

    /* Opción de menu para conectar. */
    @FXML private MenuItem menuConectar;
    /* Opción de menu para desconectar. */
    @FXML private MenuItem menuDesconectar;
    /* Opción de menu para agregar. */
    @FXML private MenuItem menuAgregar;
    /* Opción de menu para editar. */
    @FXML private MenuItem menuEditar;
    /* Opción de menu para eliminar. */
    @FXML private MenuItem menuEliminar;
    /* Opción de menu para buscar. */
    @FXML private MenuItem menuBuscar;
    /* El botón de agregar. */
    @FXML private Button botonAgregar;
    /* El botón de editar. */
    @FXML private Button botonEditar;
    /* El botón de eliminar. */
    @FXML private Button botonEliminar;
    /* El botón de buscar. */
    @FXML private Button botonBuscar;

    /* La ventana. */
    private Stage escenario;
    /* El controlador de tabla. */
    private ControladorTablaCelular ControladorTablaCelular;
    /* La base de datos. */
    private BaseDeDatosCelular bdd;
    /* La conexión del cliente. */
    private Conexion<Celular> conexion;
    /* Si hay o no conexión. */
    private boolean conectado;
    /* Número de celulares seleccionados. */
    private int seleccionados;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        setSeleccionados(0);
        setConectado(false);
        bdd = new BaseDeDatosCelular();
        bdd.agregaEscucha((e, r1, r2) -> manejaEventoBaseDeDatos(e, r1, r2));
    }

    /* Conecta el cliente con el servidor. */
    @FXML private void conectar(ActionEvent evento) {
        if (conectado)
            return;

        String servidor = null;
        int puerto = -1;

        try {
            FXMLLoader cargador = new FXMLLoader();
            ClassLoader cl = getClass().getClassLoader();
            cargador.setLocation(cl.getResource(CONECTAR_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            escenario.setTitle("Conectar a servidor");
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaConectar controlador = cargador.getController();
            controlador.setEscenario(escenario);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            escenario.showAndWait();
            ControladorTablaCelular.enfocaTabla();
            if (!controlador.isAceptado())
                return;

            servidor = controlador.getServidor();
            puerto = controlador.getPuerto();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar el diálogo '%s'.", CONECTAR_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
            return;
        }

        try {
            Socket enchufe = new Socket(servidor, puerto);
            conexion = new ConexionCliente<Celular>(bdd, enchufe);
            conexion.agregaEscucha((c, e, m) -> manejaEventoConexion(c, e, m));
            new Thread(() -> conexion.manejaMensajes()).start();
        } catch (IOException ioe) {
            conexion = null;
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "conectarnos a %s:%d.\n", servidor, puerto);
            dialogoError("Error al establecer conexión", mensaje);
            return;
        }
        setConectado(true);
    }

    /* Desconecta el cliente del servidor. */
    @FXML private void desconectar(ActionEvent evento) {
        if (!conectado)
            return;
        setConectado(false);
        conexion.desconecta();
        conexion = null;
        bdd.limpia();
    }

    /* Cambia la interfaz gráfica dependiendo de hay o no conexión. */
    public void setConectado(boolean conectado) {
        this.conectado = conectado;
        menuConectar.setDisable(conectado);
        menuDesconectar.setDisable(!conectado);
        menuAgregar.setDisable(!conectado);
        menuBuscar.setDisable(!conectado);
        botonAgregar.setDisable(!conectado);
        botonBuscar.setDisable(!conectado);
    }

    /**
     * Termina el programa.
     * @param evento el evento que generó la acción.
     */
    @FXML public void salir(ActionEvent evento) {
        desconectar(evento);
        Platform.exit();
    }

    /* Agrega un nuevo celular. */
    @FXML private void agregaCelular(ActionEvent evento) {
        ControladorFormaCelular controlador =
            construyeDialogoCelular("Agregar celular", null);
        if (controlador == null)
            return;
        controlador.setVerbo("Agregar");
        controlador.getEscenario().showAndWait();
        ControladorTablaCelular.enfocaTabla();
        if (!controlador.isAceptado())
            return;
        bdd.agregaRegistro(controlador.getCelular());
    }

    /* Edita un celular. */
    @FXML private void editaCelular(ActionEvent evento) {
        Celular celular =
            ControladorTablaCelular.getRenglonSeleccionado();
        ControladorFormaCelular controlador =
            construyeDialogoCelular("Editar celular", celular);
        if (controlador == null)
            return;
        controlador.setVerbo("Actualizar");
        controlador.getEscenario().showAndWait();
        ControladorTablaCelular.enfocaTabla();
        if (!controlador.isAceptado())
            return;
        bdd.modificaRegistro(celular, controlador.getCelular());
    }

    /* Elimina uno o varios celulares. */
    @FXML private void eliminaCelulares(ActionEvent evento) {
        String sujeto = (seleccionados == 1) ? "celular" : "celulares";
        String titulo = "Eliminar " + sujeto;
        String mensaje = (seleccionados == 1) ?
            "Esto eliminará al celular seleccionado" :
            "Esto eliminará a los celulares seleccionados";
        if (!dialogoDeConfirmacion(titulo, mensaje, "¿Está seguro?",
                                   "Eliminar " + sujeto,
                                   "Conservar " + sujeto))
            return;
        for (Celular celular : ControladorTablaCelular.getSeleccion())
            bdd.eliminaRegistro(celular);
    }

    /* Busca Celulares. */
    @FXML private void buscaCelulares(ActionEvent evento) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador =
                new FXMLLoader(cl.getResource(BUSQUEDA_CELULAR_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.setTitle("Buscar celulares");
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaBusquedaCelular controlador;
            controlador = cargador.getController();
            controlador.setEscenario(escenario);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            escenario.showAndWait();
            ControladorTablaCelular.enfocaTabla();
            if (!controlador.isAceptado())
                return;

            Lista<Celular> resultados =
                bdd.buscaRegistros(controlador.getCampo(),
                                   controlador.getValor());

            ControladorTablaCelular.seleccionaRenglones(resultados);
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar el diálogo '%s'.",
                              BUSQUEDA_CELULAR_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
        }
    }

    /* Muestra un diálogo con información del programa. */
    @FXML private void acercaDe(ActionEvent evento) {
        Alert dialogo = new Alert(AlertType.INFORMATION);
        dialogo.initOwner(escenario);
        dialogo.initModality(Modality.WINDOW_MODAL);
        dialogo.setTitle("Acerca de Administrador de Celulares.");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Aplicación para administrar celulares.\n"  +
                               "Creado por: José Miguel Toledo Reyes\n"+
                               "Proyecto #2 ICC Semestre 2018-2\n"+
                               "Copyright © 2018 Facultad de Ciencias, UNAM.");
        dialogo.showAndWait();
        ControladorTablaCelular.enfocaTabla();
    }

    /**
     * Define el controlador de tabla.
     * @param ControladorTablaCelular el controlador de tabla.
     */
    public void setControladorTablaCelular(ControladorTablaCelular
                                               ControladorTablaCelular) {
        this.ControladorTablaCelular = ControladorTablaCelular;
        ControladorTablaCelular.agregaEscuchaSeleccion(
            n -> setSeleccionados(n));
    }

    /**
     * Define el escenario.
     * @param escenario el escenario.
     */
    public void setEscenario(Stage escenario) {
        this.escenario = escenario;
    }

    /* Maneja un evento de cambio en la base de datos. */
    private void manejaEventoBaseDeDatos(EventoBaseDeDatos evento,
                                        Celular celular1,
                                        Celular celular2) {
        switch (evento) {
        case BASE_LIMPIADA:
            Platform.runLater(() -> limpiaTabla());
            break;
        case REGISTRO_AGREGADO:
            Platform.runLater(() -> agregaCelular(celular1));
            break;
        case REGISTRO_ELIMINADO:
            Platform.runLater(() -> eliminaCelular(celular1));
            break;
        case REGISTRO_MODIFICADO:
            Platform.runLater(() -> reordenaTabla());
            break;
        }
    }

    /* Actualiza la interfaz dependiendo del número de renglones
     * seleccionados. */
    private void setSeleccionados(int seleccionados) {
        this.seleccionados = seleccionados;
        menuEliminar.setDisable(seleccionados == 0);
        menuEditar.setDisable(seleccionados != 1);
        botonEliminar.setDisable(seleccionados == 0);
        botonEditar.setDisable(seleccionados != 1);
    }

    /* Crea un diálogo con una pregunta que hay que confirmar. */
    private boolean dialogoDeConfirmacion(String titulo,
                                          String mensaje, String pregunta,
                                          String aceptar, String cancelar) {
        Alert dialogo = new Alert(AlertType.CONFIRMATION);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(mensaje);
        dialogo.setContentText(pregunta);

        ButtonType si = new ButtonType(aceptar);
        ButtonType no = new ButtonType(cancelar, ButtonData.CANCEL_CLOSE);
        dialogo.getButtonTypes().setAll(si, no);

        Optional<ButtonType> resultado = dialogo.showAndWait();
        ControladorTablaCelular.enfocaTabla();
        return resultado.get() == si;
    }

    /* Maneja los distintos eventos de la conexión. */
    private void manejaEventoConexion(Conexion<Celular> conexion,
                                      EventoConexion evento, String mensaje) {
        String t, m;
        switch (evento) {
        case DESCONEXION:
            conexion.desconecta();
            t = "Servidor desconectado";
            m = "El servidor solicitó terminar la conexión";
            Platform.runLater(() -> dialogoAdvertencia(t, m, true));
            break;
        case ADVERTENCIA:
            t = "Avertencia del servidor";
            m = "Advertencia.";
            Platform.runLater(() -> dialogoAdvertencia(t, m, false));
            break;
        case ERROR:
            t = "Error de comunicación";
            m = "Ocurrión un error al tratar de comunicarnos con el servidor.";
            Platform.runLater(() -> dialogoError(t, m));
            break;
        }
    }

    /* Construye un diálogo para crear o editar un celular. */
    private ControladorFormaCelular
    construyeDialogoCelular(String titulo,
                               Celular celular) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador =
                new FXMLLoader(cl.getResource(CELULAR_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.setTitle(titulo);
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaCelular controlador = cargador.getController();
            controlador.setEscenario(escenario);
            controlador.setCelular(celular);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            return controlador;
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de cargar " +
                              "el diálogo '%s'.", CELULAR_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
            return null;
        }
    }

    /* Muestra un diálogo de advertencia. */
    private void dialogoAdvertencia(String titulo,
                                    String mensaje,
                                    boolean limpia) {
        Alert dialogo = new Alert(AlertType.WARNING);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        if (limpia)
            dialogo.setOnCloseRequest(e -> limpiaTabla());
        dialogo.show();
        ControladorTablaCelular.enfocaTabla();
    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        if (conexion != null && conexion.activa())
            conexion.desconecta();
        Alert dialogo = new Alert(AlertType.ERROR);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        dialogo.setOnCloseRequest(e -> limpiaTabla());
        dialogo.show();
        ControladorTablaCelular.enfocaTabla();
    }

    /* Agrega un celular a la tabla. */
    private void agregaCelular(Celular celular) {
        ControladorTablaCelular.agregaRenglon(celular);
    }

    /* Elimina un celular de la tabla. */
    private void eliminaCelular(Celular celular) {
        ControladorTablaCelular.eliminaRenglon(celular);
    }

    /* Reordena la tabla. */
    private void reordenaTabla() {
        ControladorTablaCelular.reordena();
    }

    /* Limpia la tabla. */
    private void limpiaTabla() {
        ControladorTablaCelular.limpiaTabla();
    }
}
