package mx.unam.ciencias.icc.fx;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import mx.unam.ciencias.icc.Celular;
import mx.unam.ciencias.icc.Lista;

/**
 * Clase para el controlador de la tabla para mostrar la base de datos.
 */
public class ControladorTablaCelular {

    /* La tabla. */
    @FXML private TableView<Celular> tabla;

    /* La columna del nombre. */
    @FXML private TableColumn<Celular, String> columnaNombre;
    /* La columna del número de serie. */
    @FXML private TableColumn<Celular, Number> columnaSerie;
    /* La columna del precio. */
    @FXML private TableColumn<Celular, Number> columnaPrecio;
    /*La columna de la marca.*/
    @FXML private TableColumn<Celular, String> columnaMarca;
    /* La columna del telefono. */
    @FXML private TableColumn<Celular, Number> columnaTelefono;

    /* El modelo de la selección. */
    TableView.TableViewSelectionModel<Celular> modeloSeleccion;
    /* La selección. */
    private ObservableList<TablePosition> seleccion;
    /* Lista de escuchas de selección. */
    private Lista<EscuchaSeleccion> escuchas;
    /* Los renglones en la tabla. */
    private ObservableList<Celular> renglones;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE);
        seleccion = modeloSeleccion.getSelectedCells();
        ListChangeListener<TablePosition> lcl = c -> cambioEnSeleccion();
        seleccion.addListener(lcl);
        columnaNombre.setCellValueFactory(c -> c.getValue().nombreProperty());
        columnaSerie.setCellValueFactory(c -> c.getValue().serieProperty());
        columnaPrecio.setCellValueFactory(c -> c.getValue().precioProperty());
          columnaMarca.setCellValueFactory(c -> c.getValue().marcaProperty());
        columnaTelefono.setCellValueFactory(c -> c.getValue().telefonoProperty());
        escuchas = new Lista<EscuchaSeleccion>();
    }

    /* Notifica a los escuchas que hubo un cambio en la selección. */
    private void cambioEnSeleccion() {
        for (EscuchaSeleccion escucha : escuchas)
            escucha.renglonesSeleccionados(seleccion.size());
    }

    /**
     * Limpia la tabla.
     */
    public void limpiaTabla() {
        renglones.clear();
    }

    /**
     * Agrega un renglón a la tabla.
     * @param celular el renglón a agregar.
     */
    public void agregaRenglon(Celular celular) {
        renglones.add(celular);
        tabla.sort();
    }

    /**
     * Elimina un renglón de la tabla.
     * @param celular el renglón a eliminar.
     */
    public void eliminaRenglon(Celular celular) {
        renglones.remove(celular);
        tabla.sort();
    }

    /**
     * Selecciona renglones de la tabla.
     * @param celulares los renglones a seleccionar.
     */
    public void seleccionaRenglones(Lista<Celular> celulares) {
        modeloSeleccion.clearSelection();
        for (Celular celular : celulares)
            modeloSeleccion.select(celular);
    }

    /**
     * Regresa una lista con los registros seleccionados en la tabla.
     * @return una lista con los registros seleccionados en la tabla.
     */
    public Lista<Celular> getSeleccion() {
        Lista<Celular> seleccionados = new Lista<Celular>();
        for (TablePosition tp : seleccion) {
            int r = tp.getRow();
            seleccionados.agregaFinal(renglones.get(r));
        }
        return seleccionados;
    }

    /**
     * Regresa el Celular seleccionado en la tabla.
     * @return el Celular seleccionado en la tabla.
     */
    public Celular getRenglonSeleccionado() {
        int r = seleccion.get(0).getRow();
        return renglones.get(r);
    }

    /**
     * Agrega un escucha de selección.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscuchaSeleccion(EscuchaSeleccion escucha) {
        escuchas.agregaFinal(escucha);
    }

    /**
     * Fuerza un reordenamiento de la tabla.
     */
    public void reordena() {
        tabla.sort();
    }

    /**
     * Enfoca la tabla.
     */
    public void enfocaTabla() {
        tabla.requestFocus();
    }
}
