package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Estudiante}.
 */
public enum CampoCelular {

  /** El nombre del Celular. */
     NOMBRE,
     /** El número de serie del celular. */
     SERIE,
     /** El precio del celular. */
     PRECIO,
     /** La Marca del celular. */
     MARCA,
   /** El Numero Telefonico asociado al celular. */
    TELEFONO;
    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
      switch(this) {
           case NOMBRE: return "Nombre";
           case SERIE: return "# Serie";
           case PRECIO:return "Precio";
           case MARCA: return "Marca";
           case  TELEFONO: return "# Telefonico";
      default:
      throw new IllegalArgumentException();
         }
          }
      }
