package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
* Clase para representar celulares. Un celular tiene nombre, numero de
* serie, precio , marca y un numero de telefono asociado . La clase implementa {@link Registro}, por lo que
* puede cargarse y guardarse utilizando objetos de las clases {@link
* BufferedReader} y {@link BufferedWriter} como entrada y salida
* respectivamente, además de determinar si sus campos cazan valores
* arbitrarios y actualizarse con los valores de otro Celular.
 */
public class Celular implements Registro<Celular, CampoCelular> {

/* Nombre del celular */
private StringProperty nombre;
/* Número de serie. */
private IntegerProperty serie;
/* Precio del celular. */
private DoubleProperty precio;
/* Marca del celular.*/
private StringProperty marca;
/*Numero de telefono asociado */
private IntegerProperty telefono;





public Celular(String nombre,
               int serie,
               double precio,
               String marca,
               int telefono) {
        this.nombre = new SimpleStringProperty(nombre);
        this.serie=new SimpleIntegerProperty(serie);
        this.precio=new SimpleDoubleProperty(precio);
        this.marca = new SimpleStringProperty(marca);
        this.telefono=new SimpleIntegerProperty(telefono);

}

/**
 * Regresa el nombre del Celular.
 * @return el nombre del Celular.
 */
public String getNombre() {
        return nombre.get();
}

/**
 * Define el nombre del Celular.
 * @param nombre el nuevo nombre del Celular.
 */
public void setNombre(String nombre) {
        this.nombre.set(nombre);
}

/**
 * Regresa la propiedad del nombre.
 * @return la propiedad del nombre.
 */
public StringProperty nombreProperty() {
        return this.nombre;
}

/**
 * Regresa el número de serie del celular.
 * @returnel número de serie del celular.
 */
public int getSerie() {
        return serie.get();
}

/**
 * Define el número de serie del celular.
 * @param serie el nuevo número de serie del celular.
 */
public void setSerie(int serie) {
        this.serie.set(serie);
}

/**
 * Regresa la propiedad del número de serie.
 * @return la propiedad del número de serie.
 */
public IntegerProperty serieProperty() {
        return this.serie;
}

/**
 * Regresa el precio del celular.
 * @return el precio del celular.
 */
public double getPrecio() {
        return precio.get();
}

/**
 * Define el  precio del celular.
 * @param promedio el nuevo  precio del celular.
 */
public void setPrecio(double precio) {
        this.precio.set(precio);
}

/**
 * Regresa la propiedad del precio.
 * @return la propiedad del precio.
 */
public DoubleProperty precioProperty() {
        return this.precio;
}

/**
 * Regresa la marca del celular.
 * @return la marca del celular.
 */
public String getMarca() {
        return marca.get();
}

/**
 * Define la marca del celular.
 * @param marca la nueva marca el Celular.
 */
public void setMarca(String marca) {
        this.marca.set(marca);
}

/**
 * Regresa la propiedad de marca.
 * @return la propiedad de marca.
 */
public StringProperty marcaProperty() {
        return this.marca;
}



/**
 * Regresa el telefono del celular.
 * @return el telefono del celular.
 */

public int getTelefono() {
        return telefono.get();
}

/**
 * Define la telefono del celular.
 * @param telefono el nueva telefono del celular.
 */
public void setTelefono(int telefono) {
        this.telefono.set(telefono);
}

/**
 * Regresa la propiedad del telefono.
 * @return la propiedad  del telefono.
 */
public IntegerProperty telefonoProperty() {
        return this.telefono;
}

/**
 * Regresa una representación en cadena del celular.
 * @return una representación en cadena del celular.
 */
@Override public String toString() {
        String cadena= String.format("Nombre            : %s\n" +
                                     "Numero de serie   : %d\n" +
                                     "Precio            : %2.2f\n" +
                                     "Marca             : %s\n" +
                                     "Numero Telefonico : %d",nombre.get(), serie.get(),precio.get(), marca.get(),telefono.get());
        return cadena;



}

/**
 * Nos dice si el objeto recibido es un celular igual al que manda llamar
 * el método.
 * @param objeto el objeto con el que el celular se comparará.
 * @return <tt>true</tt> si el objeto o es un celular con las mismas
 *         propiedades que el objeto que manda llamar al método,
 *         <tt>false</tt> en otro caso.
 */
@Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Celular))
                return false;
        Celular celular = (Celular)objeto;
        if (celular == null)
                return false;

        if( !nombre.get().equals(celular.getNombre()))
                return false;

        if(serie.get() != celular.getSerie())
                return false;


        if(precio.get() != celular.getPrecio() )
                return false;

        if( !marca.get().equals(celular.getMarca()))
                return false;

        if(telefono.get() != celular.getTelefono())
                return false;
        return true;
}

/**
 * Guarda al celular en la salida recibida.
 * @param out la salida dónde hay que guardar al Celular.
 * @throws IOException si un error de entrada/salida ocurre.
 */
@Override public void guarda(BufferedWriter out) throws IOException {
        try{

                out.write(String.format("%s\t%d\t%2.2f\t%s\t%d\n",nombre.get(),serie.get(),precio.get(),marca.get(),telefono.get()));
        }
        catch(IOException e ) {

                throw new IOException();

        }
}

/**
 * Carga al celular de la entrada recibida.
 * @param in la entrada de dónde hay que cargar al celular.
 * @return <tt>true</tt> si el método carga un celular válido,
 *         <tt>false</tt> en otro caso.
 * @throws IOException si un error de entrada/salida ocurre, o si la entrada
 *         recibida no contiene a un celular.
 */
@Override public boolean carga(BufferedReader in) throws IOException {
        try{
                String s = in.readLine();
                if(s == null || s.isEmpty()) {
                        return false;
                }
                String[] values=s.split("\t");

                if(values.length !=5 ) {
                        throw  new IOException();
                }
                this.setNombre(values[0]);
                this.setSerie(Integer.parseInt(values[1]));
                this.setPrecio(Double.parseDouble(values[2]));
                this.setMarca(values[3]);
                this.setTelefono(Integer.parseInt(values[4]));
                return true;
        } catch (Exception e) {
                throw new IOException();

        }

}

/**
 * Nos dice si el celular caza el valor dado en el campo especificado.
 * @param campo el campo que hay que cazar.
 * @param valor el valor con el que debe cazar el campo del registro.
 * @return <tt>true</tt> si:
 *         <ul>
 *           <li><tt>campo</tt> es {@link CampoCelular#NOMBRE} y
 *              <tt>valor</tt> es instancia de {@link String} y es una
 *              subcadena del nombre del celular.</li>
 *           <li><tt>campo</tt> es {@link CampoCelular#serie} y
 *              <tt>valor</tt> es instancia de {@link Integer} y su
 *              valor entero es mayor o igual al numero de serie del
 *              celular.</li>
 *           <li><tt>campo</tt> es {@link CampoCelular#PRECIO} y
 *              <tt>valor</tt> es instancia de {@link Double} y su
 *              valor doble es mayor o igual al precio del
 *              celular.</li>
 <li><tt>campo</tt> es {@link CampoCelular#MARCA} y
*              <tt>valor</tt> es instancia de {@link String} y es una
*              subcadena de la  Marca del celular.</li>
 *           <li><tt>campo</tt> es {@link CampoCelular#TELEFONO} y
 *              <tt>valor</tt> es instancia de {@link Integer} y su
 *              valor entero es mayor o igual al numero de telefono del
 *              celular.</li>
 *         </ul>
 *         <tt>false</tt> en otro caso.
 * @throws IllegalArgumentException si el campo no es instancia de
 *         {@link CampoCelular}.
 */
@Override public boolean caza(CampoCelular campo, Object valor) {
        if(!(campo instanceof CampoCelular))
                throw new IllegalArgumentException();
        CampoCelular ce =(CampoCelular)campo;
        switch(ce) {
        case NOMBRE: return cazaNombre(valor);
        case SERIE: return cazaSerie(valor);
        case PRECIO: return cazaPrecio(valor);
        case MARCA: return cazaMarca(valor);
        case TELEFONO: return cazaTelefono(valor);
        }
        return true;
}

private boolean cazaNombre(Object valor){
        if(!(valor instanceof String ))
                return false;
        String s = (String) valor;
        if(s.isEmpty())
                return false;
        return nombre.get().indexOf(s)!=-1;
}

private boolean cazaSerie(Object valor){
        if(!(valor instanceof Integer ))
                return false;
        Integer v =(Integer)valor;
        return serie.get() >=v.intValue();

}


private boolean cazaPrecio(Object valor){
        if(!(valor instanceof Double ))
                return false;
        Double v =(Double)valor;
        return precio.get() >=v.doubleValue();

}


private boolean cazaMarca(Object valor){
        if(!(valor instanceof String ))
                return false;
        String v =(String)valor;
        if(v.isEmpty())
                return false;
        return marca.get().indexOf(v)!=-1;
}



private boolean cazaTelefono(Object valor){
        if(!(valor instanceof Integer ))
                return false;
        Integer v =(Integer)valor;
        return telefono.get() >=v.intValue();

}


/**
 * Actualiza los valores del celular con los del registro recibido.
 * @param estudiante el estudiante con el cual actualizar los valores.
 */
@Override public void actualiza(Celular celular) {

        this.setNombre(celular.getNombre());
        this.setSerie(celular.getSerie());
        this.setPrecio(celular.getPrecio());
        this.setMarca(celular.getMarca());
        this.setTelefono(celular.getTelefono());


}
}
