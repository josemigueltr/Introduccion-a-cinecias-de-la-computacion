package mx.unam.ciencias.icc.red;

import java.io.IOException;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosCelular;
import mx.unam.ciencias.icc.CampoCelular;
import mx.unam.ciencias.icc.Celular;;

/**
 * Clase para servidores de bases de datos de cellulares
 */
public class ServidorBaseDeDatosCelular
    extends ServidorBaseDeDatos<Celular> {

    /**
     * Construye un servidor de base de datos de celulares
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param archivo el archivo en el disco del cual cargar/guardar la base de
     *                datos.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatosCelular(int puerto, String archivo)
        throws IOException {
        super(puerto,archivo);
    }

    /**
     * Crea una base de datos de cellulares
     * @return una base de datos de cellulares
     */
    @Override public
    BaseDeDatos<Celular, CampoCelular> creaBaseDeDatos() {
     return new BaseDeDatosCelular();
    }
}
