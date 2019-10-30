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
 * Clase para representar estudiantes. Un estudiante tiene nombre, número de
 * cuenta, promedio y edad. La clase implementa {@link Registro}, por lo que
 * puede cargarse y guardarse utilizando objetos de las clases {@link
 * BufferedReader} y {@link BufferedWriter} como entrada y salida
 * respectivamente, además de determinar si sus campos cazan valores
 * arbitrarios y actualizarse con los valores de otro estudiante.
 */
public class Estudiante implements Registro<Estudiante, CampoEstudiante> {

    /* Nombre del estudiante. */
    private StringProperty nombre;
    /* Número de cuenta. */
    private IntegerProperty cuenta;
    /* Pormedio del estudiante. */
    private DoubleProperty promedio;
    /* Edad del estudiante.*/
    private IntegerProperty edad;

    /**
     * Define el estado inicial de un estudiante.
     * @param nombre el nombre del estudiante.
     * @param cuenta el número de cuenta del estudiante.
     * @param promedio el promedio del estudiante.
     * @param edad la edad del estudiante.
     */
    public Estudiante(String nombre,
                      int    cuenta,
                      double promedio,
                      int    edad) {
        this.nombre = new SimpleStringProperty(nombre);
        this.cuenta=new SimpleIntegerProperty(cuenta);
          this.promedio=new SimpleDoubleProperty(promedio);
            this.edad=new SimpleIntegerProperty(edad);
    }

    /**
     * Regresa el nombre del estudiante.
     * @return el nombre del estudiante.
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Define el nombre del estudiante.
     * @param nombre el nuevo nombre del estudiante.
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
     * Regresa el número de cuenta del estudiante.
     * @return el número de cuenta del estudiante.
     */
    public int getCuenta() {
        return cuenta.get();
    }

    /**
     * Define el número cuenta del estudiante.
     * @param cuenta el nuevo número de cuenta del estudiante.
     */
    public void setCuenta(int cuenta) {
         this.cuenta.set(cuenta);
    }

    /**
     * Regresa la propiedad del número de cuenta.
     * @return la propiedad del número de cuenta.
     */
    public IntegerProperty cuentaProperty() {
      return this.cuenta;
    }

    /**
     * Regresa el promedio del estudiante.
     * @return el promedio del estudiante.
     */
    public double getPromedio() {
         return promedio.get();
    }

    /**
     * Define el promedio del estudiante.
     * @param promedio el nuevo promedio del estudiante.
     */
    public void setPromedio(double promedio) {
    this.promedio.set(promedio);
    }

    /**
     * Regresa la propiedad del promedio.
     * @return la propiedad del promedio.
     */
    public DoubleProperty promedioProperty() {
         return this.promedio;
    }

    /**
     * Regresa la edad del estudiante.
     * @return la edad del estudiante.
     */
    public int getEdad() {
         return edad.get();
    }

    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setEdad(int edad) {
       this.edad.set(edad);
    }

    /**
     * Regresa la propiedad de la edad.
     * @return la propiedad de la edad.
     */
    public IntegerProperty edadProperty() {
          return this.edad;
    }

    /**
     * Regresa una representación en cadena del estudiante.
     * @return una representación en cadena del estudiante.
     */
    @Override public String toString() {
      String cadena= String.format("Nombre   : %s\n" +
                                 "Cuenta   : %09d\n" +
                                 "Promedio : %2.2f\n" +
                                 "Edad     : %d",nombre.get(), cuenta.get(), promedio.get(),edad.get());
    return cadena;

    }

    /**
     * Nos dice si el objeto recibido es un estudiante igual al que manda llamar
     * el método.
     * @param objeto el objeto con el que el estudiante se comparará.
     * @return <tt>true</tt> si el objeto o es un estudiante con las mismas
     *         propiedades que el objeto que manda llamar al método,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Estudiante))
            return false;
        Estudiante estudiante = (Estudiante)objeto;
        if (estudiante == null)
        return false;

if( !nombre.get().equals(estudiante.getNombre()))
        return false;

if(cuenta.get() != estudiante.getCuenta())
        return false;

if(promedio.get() != estudiante.getPromedio() )
        return false;

if(edad.get() != estudiante.getEdad())
        return false;
return true;
    }

    /**
     * Guarda al estudiante en la salida recibida.
     * @param out la salida dónde hay que guardar al estudiante.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    @Override public void guarda(BufferedWriter out) throws IOException {
      try{

           out.write(String.format("%s\t%d\t%2.2f\t%d\n",nombre.get(),cuenta.get(),promedio.get(),edad.get()));
   }
   catch(IOException e ) {

           throw new IOException();

   }
    }

    /**
     * Carga al estudiante de la entrada recibida.
     * @param in la entrada de dónde hay que cargar al estudiante.
     * @return <tt>true</tt> si el método carga un estudiante válido,
     *         <tt>false</tt> en otro caso.
     * @throws IOException si un error de entrada/salida ocurre, o si la entrada
     *         recibida no contiene a un estudiante.
     */
    @Override public boolean carga(BufferedReader in) throws IOException {
      try{
           String s = in.readLine();
           if(s == null) {
                   return false;
           }
           String[] values=s.split("\t");

           if(values.length != 4) {
                   throw  new IOException();
           }
           this.setNombre(values[0]);
           this.setCuenta(Integer.parseInt(values[1]));
           this.setPromedio(Double.parseDouble(values[2]));
           this.setEdad(Integer.parseInt(values[3]));
           return true;
   } catch (Exception e) {
           throw new IOException();

   }

    }

    /**
     * Nos dice si el estudiante caza el valor dado en el campo especificado.
     * @param campo el campo que hay que cazar.
     * @param valor el valor con el que debe cazar el campo del registro.
     * @return <tt>true</tt> si:
     *         <ul>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#NOMBRE} y
     *              <tt>valor</tt> es instancia de {@link String} y es una
     *              subcadena del nombre del estudiante.</li>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#CUENTA} y
     *              <tt>valor</tt> es instancia de {@link Integer} y su
     *              valor entero es mayor o igual a la cuenta del
     *              estudiante.</li>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#PROMEDIO} y
     *              <tt>valor</tt> es instancia de {@link Double} y su
     *              valor doble es mayor o igual al promedio del
     *              estudiante.</li>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#EDAD} y
     *              <tt>valor</tt> es instancia de {@link Integer} y su
     *              valor entero es mayor o igual a la edad del
     *              estudiante.</li>
     *         </ul>
     *         <tt>false</tt> en otro caso.
     * @throws IllegalArgumentException si el campo no es instancia de
     *         {@link CampoEstudiante}.
     */
    @Override public boolean caza(CampoEstudiante campo, Object valor) {
      if(!(campo instanceof CampoEstudiante))
                    throw new IllegalArgumentException();
            CampoEstudiante ce =(CampoEstudiante)campo;
            switch(ce) {
            case NOMBRE: return cazaNombre(valor);
            case CUENTA: return cazaCuenta(valor);
            case PROMEDIO: return cazaPromedio(valor);
            case EDAD: return cazaEdad(valor);
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

       private boolean cazaCuenta(Object valor){
               if(!(valor instanceof Integer ))
                       return false;
               Integer v =(Integer)valor;
               return  cuenta.get() >=v.intValue();

       }


       private boolean cazaPromedio(Object valor){
               if(!(valor instanceof Double ))
                       return false;
               Double v =(Double)valor;
               return promedio.get() >=v.doubleValue();

       }


       private boolean cazaEdad(Object valor){
               if(!(valor instanceof Integer ))
                       return false;
               Integer v =(Integer)valor;
               return edad.get() >=v.intValue();

       }


    /**
     * Actualiza los valores del estudiante con los del registro recibido.
     * @param estudiante el estudiante con el cual actualizar los valores.
     */
    @Override public void actualiza(Estudiante estudiante) {

        this.setNombre(estudiante.getNombre());
          this.setCuenta(estudiante.getCuenta());
            this.setPromedio(estudiante.getPromedio());
        this.setEdad(estudiante.getEdad());


    }
}
