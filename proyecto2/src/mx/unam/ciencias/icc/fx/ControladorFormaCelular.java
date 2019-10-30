package mx.unam.ciencias.icc.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mx.unam.ciencias.icc.Celular;;

/**
 * Clase para el controlador del contenido del diálogo para editar y crear
 * celulars.
 */
public class ControladorFormaCelular extends ControladorForma {

    /* La entrada verificable para el nombre. */
    @FXML private EntradaVerificable entradaNombre;
    /* La entrada verificable para el número de cuenta. */
    @FXML private EntradaVerificable entradaSerie;
    /* La entrada verificable para el promedio. */
    @FXML private EntradaVerificable entradaPrecio;
      @FXML private EntradaVerificable entradaMarca;
    /* La entrada verificable para la edad. */
    @FXML private EntradaVerificable entradaTelefono;

    /* El valor del nombre. */
    private String nombre;
    /* El valor del número de cuenta. */
    private int serie;
    /* El valor del promedio. */
    private double precio;
    /* El valor de la edad. */
    private String marca;

    private int telefono;

    /* El celular creado o editado. */
    private Celular celular;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaNombre.setVerificador(n -> verificaNombre(n));
        entradaSerie.setVerificador(s -> verificaSerie(s));
        entradaPrecio.setVerificador(p -> verificaPrecio(p));
        entradaMarca.setVerificador(m -> verificaMarca(m));
        entradaTelefono.setVerificador(t -> verificaTelefono(t));

        entradaNombre.textProperty().addListener(
            (o, v, n) -> verificaCelular());
        entradaSerie.textProperty().addListener(
            (o, v, n) -> verificaCelular());
        entradaPrecio.textProperty().addListener(
            (o, v, n) -> verificaCelular());
            entradaMarca.textProperty().addListener(
                    (o, v, n) -> verificaCelular());
        entradaTelefono.textProperty().addListener(
            (o, v, n) -> verificaCelular());

    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        actualizaCelular();
        aceptado = true;
        escenario.close();
    }

    /**
     * Define el celular del diálogo.
     * @param celular el nuevo celular del diálogo.
     */
    public void setCelular(Celular celular) {
        this.celular = celular;
        if (celular == null)
            return;
        this.celular = new Celular(null, 0, 0, null, 0);
        this.celular.actualiza(celular);
        entradaNombre.setText(celular.getNombre());
        String s = String.format("%d", celular.getSerie());
        entradaSerie.setText(s);
        String p = String.format("%2.2f", celular.getPrecio());
        entradaPrecio.setText(p);
        entradaMarca.setText(celular.getMarca());
        entradaTelefono.setText(String.valueOf(celular.getTelefono()));
    }

    /* Verifica que los cuatro campos sean válidos. */
    private void verificaCelular() {
        boolean n = entradaNombre.esValida();
        boolean c = entradaSerie.esValida();
        boolean p = entradaPrecio.esValida();
        boolean m = entradaMarca.esValida();
        boolean e = entradaTelefono.esValida();
        botonAceptar.setDisable(!n || !c || !p || !m || !e );
    }

    /* Verifica que el nombre sea válido. */
    private boolean verificaNombre(String n) {
      if(n==null || n.isEmpty())
     return false;
     nombre=n;
              return true;
    }

    /* Verifica que el número de cuenta sea válido. */
    private boolean verificaSerie(String c) {
      try {

            int l = Integer.parseInt(c);
            if(l>1000 && l<10000 ){
              serie=l;
            return true;
                   }
                return false;


    }catch (Exception e) {
            return false;

    }
    }

    /* Verifica que el promedio sea válido. */
    private boolean verificaPrecio(String p) {
      try {
                double c = Double.parseDouble(p);
                if(c>0){
                	precio=c;
                     return true;
                 }
                 return false;
        }catch (Exception e) {
                return false;
        }
    }

    private boolean verificaMarca(String m) {
      if(m==null || m.isEmpty())
     return false;
     marca=m;
              return true;
    }


    /* Verifica que la edad sea válida. */
    private boolean verificaTelefono(String e) {
      try{
               int c = Integer.parseInt(e);
               if(c>10000000 && c<100000000){
                 telefono=c;
               return true;
           }
           return false;
       }catch(Exception s) {
               return false;
       }
    }

    /* Actualiza al celular, o lo crea si no existe. */
    private void actualizaCelular() {
      if(celular==null){
              this.celular = new Celular(this.entradaNombre.getText(),
                                                Integer.parseInt(this.entradaSerie.getText()),
                                                Double.parseDouble(this.entradaPrecio.getText()),
                                                this.entradaMarca.getText(),
                                               Integer.parseInt(this.entradaTelefono.getText()));

     }else{

           String n=this.entradaNombre.getText();
           int c =Integer.parseInt(this.entradaSerie.getText());
           double p=Double.parseDouble(this.entradaPrecio.getText());
          String m=this.entradaMarca.getText();
           int e = Integer.parseInt(this.entradaTelefono.getText());

       celular.setNombre(n);
       celular.setSerie(c);
       celular.setPrecio(p);
       celular.setMarca(m);
       celular.setTelefono(e);
}
    }

    /**
     * Regresa el celular del diálogo.
     * @return el celular del diálogo.
     */
    public Celular getCelular() {
        return celular;
    }

    /**
     * Define el verbo del botón de aceptar.
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) {
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaNombre.requestFocus();
    }
}
