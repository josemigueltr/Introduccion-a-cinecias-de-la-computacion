package mx.unam.ciencias.icc.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoCelular;;

/**
 * Clase para el controlador del contenido del diálogo para buscar celulares.
 */
public class ControladorFormaBusquedaCelular extends ControladorForma {

    /* El combo del campo. */
    @FXML private ComboBox<CampoCelular> opcionesCampo;
    /* El campo de texto para el valor. */
    @FXML private EntradaVerificable entradaValor;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaValor.setVerificador(s -> verificaValor(s));
        entradaValor.textProperty().addListener(
            (o, v, n) -> revisaValor(null));
    }

    /* Revisa el valor después de un cambio. */
    @FXML private void revisaValor(ActionEvent evento) {
        Tooltip.install(entradaValor, getTooltip());
        String s = entradaValor.getText();
        botonAceptar.setDisable(!entradaValor.esValida());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        aceptado = true;
        escenario.close();
    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {
        String m = "";
        switch (opcionesCampo.getValue()) {
        case NOMBRE:
            m = "Buscar por nombre necesita al menos un carácter";
            break;
        case SERIE:
            m = "Buscar por #serie necesita un número de 4 cifras ";
            break;
        case PRECIO:
            m = "Buscar por precio necesita un número mayor a 0";
            break;
            case MARCA:
                m = "Buscar por marca necesita al menos un carácter";
                break;
        case TELEFONO:
            m = "Buscar por telefono necesita un número de 8 digitos";
            break;
        }
        return new Tooltip(m);
    }

    /* Verifica el valor. */
    private boolean verificaValor(String s) {
        switch (opcionesCampo.getValue()) {
        case NOMBRE:   return verificaNombre(s);
        case SERIE:    return verificaSerie(s);
        case PRECIO:   return verificaPrecio(s);
        case MARCA:    return verificaMarca(s);
        case TELEFONO: return verificaTelefono(s);
        default:       return false;
        }
    }

    /* Verifica que el nombre a buscar sea válido. */
    private boolean verificaNombre(String n) {
      if(n==null || n.isEmpty())
     return false;

              return true;
    }

    /* Verifica que el número de serie a buscar sea válido. */
    private boolean verificaSerie(String s) {
      try {
          int serie = Integer.parseInt(s);
          if(serie>1000 && serie<10000)
          return true;
             return false;
  }catch (Exception e) {
          return false;

  }
    }

    /* Verifica que el precio a buscar sea válido. */
    private boolean verificaPrecio(String p) {
      try {
              double c = Double.parseDouble(p);
              if(c>0)
              return true;
          return false;
      }catch (Exception e) {
              return false;
      }
    }

/*verifica que la marca a buscar sea valida*/
    private boolean verificaMarca(String m) {
      if(m==null || m.isEmpty())
     return false;

              return true;
    }


    /* Verifica que el telefono a buscar sea válida. */
    private boolean verificaTelefono(String t) {
      try{
            int c = Integer.parseInt(t);
            if(c>10000000 && c<100000000)
            return true;
        return false;
    }catch(Exception s) {
            return false;
    }
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoCelular getCampo() {
        return opcionesCampo.getValue();
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {
        switch (opcionesCampo.getValue()) {
        case NOMBRE:   return entradaValor.getText();
        case SERIE:   return Integer.parseInt(entradaValor.getText());
        case PRECIO: return Double.parseDouble(entradaValor.getText());
        case MARCA:   return entradaValor.getText();
        case TELEFONO:     return Integer.parseInt(entradaValor.getText());
        default:       return entradaValor.getText(); // No debería ocurrir.
        }
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaValor.requestFocus();
    }
}
