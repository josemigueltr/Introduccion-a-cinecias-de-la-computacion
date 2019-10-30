package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para servidores de bases de datos genéricas.
 */
public abstract class ServidorBaseDeDatos<R extends Registro<R, ?> > {

/* La base de datos. */
private BaseDeDatos<R, ? extends Enum> bdd;
/* El servidor de enchufes. */
private ServerSocket servidor;
/* El puerto. */
private int puerto;
/* El archivo donde cargar/guardar la base de datos. */
private String archivo;
/* Lista con las conexiones. */
private Lista<ConexionServidor<R> > conexiones;
/* Bandera de continuación. */
private Boolean continuaEjecucion;
/* Escuchas del servidor. */
private Lista<EscuchaServidor> escuchas;

/**
 * Crea un nuevo servidor usando el archivo recibido para poblar la base de
 * datos.
 * @param puerto el puerto dónde escuchar por conexiones.
 * @param archivo el archivo en el disco del cual cargar/guardar la base de
 *                datos. Puede ser <code>null</code>, en cuyo caso se usará
 *                el nombre por omisión <code>base-de-datos.bd</code>.
 * @throws IOException si ocurre un error de entrada o salida.
 */
public ServidorBaseDeDatos(int puerto, String archivo)
throws IOException {
        try {
                this.puerto=puerto;
                this.archivo=(archivo !=null) ? archivo : ("Base-de-datos.bd");
                this.continuaEjecucion=true;
                servidor=new ServerSocket(puerto);
                conexiones=new Lista<ConexionServidor<R> >();
                escuchas=new Lista<EscuchaServidor>();
                bdd=this.creaBaseDeDatos();
                carga();
        }catch(IOException e) {
                throw new IOException();
        }
}



private void carga(){
        try{
                imprimeMensaje("cargando base de datos de %s \n",archivo);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(archivo)));
                this.bdd.carga(in);
                in.close();
                imprimeMensaje("Base de datos cargada exitosamente de %s \n",archivo);

        }catch(Exception ioe) {
                imprimeMensaje("Ocurrio un error al cargar la base de datos %s \n",archivo);
                imprimeMensaje("La base de datos estara vacia \n");

        }
}

private void guarda(){
        try{
                imprimeMensaje("Guardando base de datos en %s. \n", archivo);
                BufferedWriter out =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        new FileOutputStream(archivo)));
                this.bdd.guarda(out);
                out.close();
                imprimeMensaje("Base de datos guardada.");
        }catch(IOException ioe ) {
                imprimeMensaje("ocurrio un error al guardar la base de  datos. \n");

        }



}

private void imprimeMensaje(String formato, Object ... argumentos){
        for(EscuchaServidor escucha:escuchas)
                escucha.procesaMensaje(formato,argumentos);

}

/**
 * Comienza a escuchar por conexiones de clientes.
 */
public void sirve() {
        continuaEjecucion=true;
        imprimeMensaje(String.format("Escuchando en el puerto %d",puerto));
        while(continuaEjecucion) {
                try{
                        Socket enchufe=servidor.accept();
                        ConexionServidor<R> conexion = new ConexionServidor<R>(bdd,enchufe);
                        String hostName=
                                enchufe.getInetAddress().getCanonicalHostName();
                        imprimeMensaje(String.format("Conexión recibida de: %s.",hostName));
                        imprimeMensaje(String.format("Serial de conexión: %d",conexion.getSerial()));
                         conexion.agregaEscucha((c, e, m)->manejaEvento(c, e, m));
                        new Thread(()->conexion.manejaMensajes()).start();
                        synchronized(this.conexiones) {
                                conexiones.agregaFinal(conexion);
                        }
                }catch(IOException io) {
                        if(continuaEjecucion)
                                imprimeMensaje("Error al reciibir la conexión");
                }
        }
        imprimeMensaje("Ejecución del servidor ha terminado");
}

/**
 * Agrega un escucha de servidor.
 * @param escucha el escucha a agregar.
 */
public void agregaEscucha(EscuchaServidor escucha) {
        this.escuchas.agregaFinal(escucha);
}

/**
 * Limpia todos los escuchas del servidor.
 */
public void limpiaEscuchas() {
        this.escuchas.limpia();
}

/* Maneja los eventos de la conexión. */
private void manejaEvento(Conexion<R> conexion, EventoConexion evento,
                          String mensaje) {
        switch(evento) {
        case DESCONEXION:
        imprimeMensaje("La conexión %d solicitó desconectarse.",conexion.getSerial());
                conexion.desconecta();
                imprimeMensaje("La conexión %d ha sido desconectada.",conexion.getSerial());
                break;
        case MODIFICACION:
                for(ConexionServidor<R> c : conexiones)
                        c.informa(conexion);
                imprimeMensaje("La conexión %d modificó la base de datos: %s.", conexion.getSerial(),mensaje);
                guarda();

                break;
        case TERMINACION:
             synchronized (continuaEjecucion){
              this.continuaEjecucion=false;
             }

                imprimeMensaje("La conexión %d solicitó desconectarse.",conexion.getSerial());
                synchronized (this.conexiones){
                for(ConexionServidor<R> c : conexiones)
                        c.desconecta();
                      }
                      this.conexiones.limpia();
                        try{
                                servidor.close();
                        }catch (IOException e) { }

                break;
        case ADVERTENCIA:
                imprimeMensaje("La conexión %d envió una advertencia: %s.", conexion.getSerial(),mensaje);
                break;
        case ERROR:
                imprimeMensaje("La conexión %d solicitó desconectarse.",conexion.getSerial());
                conexion.desconecta();
                imprimeMensaje("Lsa conexión %d ha sido desconectada.",conexion.getSerial());
                break;
        }
      }



/**
 * Crea la base de datos concreta.
 * @return la base de datos concreta.
 */
public abstract BaseDeDatos<R, ? extends Enum> creaBaseDeDatos();
}
