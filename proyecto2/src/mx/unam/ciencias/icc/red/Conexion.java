package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para las conexiones de la base de datos.
 */
public abstract class Conexion<R extends Registro<R, ?>> {

    /** La entrada de la conexión. */
    protected BufferedReader in;
    /** La salida de la conexión. */
    protected BufferedWriter out;
    /** La base de datos. */
    protected BaseDeDatos<R, ?> bdd;
    /** El mensaje. */
    protected Mensaje mensaje;
    /** El primer registro. */
    protected R registro1;
    /** El segundo registro. */
    protected R registro2;

    /* El enchufe. */
    private Socket enchufe;
    /* Lista de escuchas de conexión. */
    private Lista<EscuchaConexion<R>> escuchas;
    /* Si la conexión está activa. */
    private boolean activa;

    /**
     * Define el estado inicial de una nueva conexión.
     * @param bdd la base de datos.
     * @param enchufe el enchufe de la conexión ya establecida.
     * @throws IOException si ocurre un error de entrada o salida.
*/
public Conexion(BaseDeDatos<R, ?> bdd, Socket enchufe) throws IOException {

        try{
                this.enchufe = enchufe;
                this.bdd = bdd;
                this.in =
                        new BufferedReader(
                                new InputStreamReader(
                                      enchufe.getInputStream()));
                this.out =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                     enchufe.getOutputStream()));
                escuchas=new Lista<EscuchaConexion<R>>();
                activa = true;
                this.registro1=bdd.creaRegistro();
                this.registro2=bdd.creaRegistro();
                this.mensaje=null;
        }catch(Exception ioe) {
                throw new IOException();
        }
}

    /**
     * Maneja los mensajes que recibe la conexión. El método no termina hasta
     * que la conexión sea cerrada. Al ir leyendo su entrada, la conexión
     * convertirá lo que lea en mensajes y manejará cada uno.
     */
    public void manejaMensajes() {
        try {
            String linea = null;
            while ((linea = in.readLine()) != null)
                manejaMensaje(Mensaje.getMensaje(linea));
            activa = false;
        } catch (IOException ioe) {
            if (activa)
                avisaEscuchas(EventoConexion.ERROR,
                              "Error recibiendo mensaje");
        }
        avisaEscuchas(EventoConexion.DESCONEXION,
                      "Conexión terminada");
    }

    /* Maneja un mensaje. */
    private void manejaMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
        registro1 = registro2 = null;
        switch (mensaje) {
        case BASE_DE_DATOS:
            manejaBaseDeDatos();
            break;
        case REGISTRO_AGREGADO:
            manejaRegistroAlterado();
            break;
        case REGISTRO_ELIMINADO:
            manejaRegistroAlterado();
            break;
        case REGISTRO_MODIFICADO:
            manejaRegistroModificado();
            break;
        case DESCONECTAR:
            manejaDesconectar();
            break;
        case DETENER_SERVICIO:
            manejaDetenerServicio();
            break;
        case ECO:
            manejaEco();
            break;
        case INVALIDO:
            manejaInvalido();
            break;
        }
    }

    /**
     * Maneja un mensaje {@link Mensaje#BASE_DE_DATOS}.
     */
    protected abstract void manejaBaseDeDatos();

    /**
     * Maneja los mensajes {@link Mensaje#REGISTRO_AGREGADO} y {@link
     * Mensaje#REGISTRO_MODIFICADO}.
     */
    protected void manejaRegistroAlterado() {
             try{
                 this.registro1=this.bdd.creaRegistro();

                  if (!this.registro1.carga(this.in)) {
                          throw new IOException();
                  }
             }catch(IOException e){
                   this.avisaEscuchas(EventoConexion.ERROR, "Error al recibir al registro");
                   return;
             }
              if(this.mensaje==Mensaje.REGISTRO_AGREGADO){
               this.agregaRegistro();
                this.avisaEscuchas(EventoConexion.MODIFICACION, "Registro agregado");
           }else{

           this.eliminaRegistro();
           this.avisaEscuchas(EventoConexion.MODIFICACION, "Registro eliminado");
         }
    }

    /**
     * Maneja un mensaje {@link Mensaje#REGISTRO_MODIFICADO}.
     */
protected void manejaRegistroModificado() {
        try{
                this.registro1 = this.bdd.creaRegistro();
                if (!this.registro1.carga(this.in)) {
                        throw new IOException();
                }

                this.registro2 = this.bdd.creaRegistro();
                if (!this.registro2.carga(this.in)) {
                        throw new IOException();
                }

        }catch(IOException e) {}

        this.modificaRegistro();
        this.avisaEscuchas(EventoConexion.MODIFICACION, "Registro modificado");
}

    /**
     * Maneja un mensaje {@link Mensaje#DESCONECTAR}.
     */
    protected void manejaDesconectar() {
        desconecta();
    }



    /**
     * Maneja un mensaje {@link Mensaje#DETENER_SERVICIO}.
     */
    protected abstract void manejaDetenerServicio();

    /**
     * Maneja un mensaje {@link Mensaje#ECO}.
     */
    protected abstract void manejaEco();

    /**
     * Maneja un mensaje {@link Mensaje#INVALIDO}.
     */
    protected void manejaInvalido() {
         this.avisaEscuchas(EventoConexion.ADVERTENCIA,"Mensaje inválido");

    }

    /**
     * Agrega el primer registro a la base de datos.
     */
protected synchronized void agregaRegistro() {
        this.bdd.agregaRegistro(this.registro1);



}

/**
 * Elimina el primer registro de la base de datos.
 */
protected synchronized void eliminaRegistro() {

        this.bdd.eliminaRegistro(this.registro1);

}

    /**
     * Modifica el primer registro con el segundo registro.
     */
    protected synchronized void modificaRegistro() {

                this.bdd.modificaRegistro(this.registro1,this.registro2);

}

    /**
     * Envía un mensaje de agregar o eliminar un registro.
     * @param mensaje el mensaje; {@link Mensaje#REGISTRO_AGREGADO} o {@link
     *        Mensaje#REGISTRO_ELIMINADO}.
     * @param registro el registro a agregar o eliminar de la base de datos.
     */
protected void enviaAlteracionRegistro(Mensaje mensaje,
                                       R registro) {

        try{
                this.out.write(mensaje.toString());
                this.out.newLine();
                registro.guarda(this.out);
                this.out.flush();

        }catch(IOException e) {
              String m = mensaje==Mensaje.REGISTRO_AGREGADO? "agregado":"eliminado";
              String m1= "Registro" + m +".";
              this.avisaEscuchas(EventoConexion.ERROR,m1);
        }
}

    /**
     * Envía un mensaje de modificar un registro con otro.
     * @param registro1 el registro a modificar en la base de datos.
     * @param registro2 los nuevos valores del registro en la base de datos.
     */
    protected void enviaModificacionRegistro(R registro1, R registro2) {
      try{
              out.write(Mensaje.REGISTRO_MODIFICADO.toString());
              out.newLine();
              registro1.guarda(out);
              registro2.guarda(out);
              this.out.flush();
      }catch(IOException e){
    this.avisaEscuchas(EventoConexion.ERROR,"Error al modificar registro");
      }
    }

    /**
     * Envía el mensaje de desconectar.
     */
    protected void enviaDesconectar() {
      try{
              out.write(Mensaje.DESCONECTAR.toString());
              out.newLine();
              this.out.flush();
      }catch(IOException e){
        System.out.println("Error Al desconectar");
      }
    }

    /**
     * Nos dice si la conexión está activa.
     * @return <tt>true</tt> si la conexión está activa, <tt>false</tt> en otro
     *         caso.
     */
    public boolean activa() {
        return this.activa;
    }

    /**
     * Regresa un número serial para cada conexión.
     * @return un número serial para cada conexión.
     */
    public abstract int getSerial();

    /**
     * Cierra la conexión.
     */
    public void desconecta() {
      try {
             this.activa=false;
              enchufe.close();
      } catch (IOException ioe) {

      }
    }

    /**
     * Agrega un escucha de conexión.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaConexion<R> escucha) {
       this.escuchas.agregaFinal(escucha);
    }

    /**
     * Regresa el último mensaje recibido.
     * @return el último mensaje recibido.
     * @throws IllegalStateException si no se ha recibido ningún mensaje.
     */
    public Mensaje getMensaje() {
         if(this.mensaje==null){
           throw new IllegalStateException();
         }else{
           return this.mensaje;
         }
    }

    /**
     * Avisa a todos los escuchas de un evento.
     * @param evento el evento a avisar.
     * @param mensaje el mensaje asociado al evento.
     */
    protected void avisaEscuchas(EventoConexion evento, String mensaje) {
        for (EscuchaConexion<R> escucha : this.escuchas){
          escucha.eventoOcurrido(this,evento,mensaje);
        }
    }
}
