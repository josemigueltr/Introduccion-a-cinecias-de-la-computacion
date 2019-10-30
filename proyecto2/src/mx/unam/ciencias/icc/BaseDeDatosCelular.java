package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de celulares.
 */
public class BaseDeDatosCelular
    extends BaseDeDatos<Celular, CampoCelular> {

    /**
     * Crea un celular en blanco.
     * @return un celular en blanco.
     */
    @Override public Celular creaRegistro() {
      return new Celular(null,0,0.0,null,0);
    }
}
