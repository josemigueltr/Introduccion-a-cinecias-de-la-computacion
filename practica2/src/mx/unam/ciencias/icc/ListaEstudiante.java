package mx.unam.ciencias.icc;

/**
 * <p>Clase para listas de estudiantes doblemente ligadas.</p>
 *
 * <p>Las listas de estudiantes nos permiten agregar elementos al inicio o final
 * de la lista, eliminar elementos de la lista, comprobar si un elemento está o
 * no en la lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas de estudiantes son iterables utilizando sus nodos. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * <p>Los elementos en una lista de estudiantes siempre son instancias de la
 * clase {@link Estudiante}.</p>
 */
public class ListaEstudiante {

    /**
     * Clase interna para nodos.
     */
    public class Nodo {

        /* El elemento del nodo. */
        private Estudiante elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(Estudiante elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }

        /**
         * Regresa el nodo anterior del nodo.
         * @return el nodo anterior del nodo.
         */
        public Nodo getAnterior() {
            // Aquí va su código.
            return this.anterior;
        }

        /**
         * Regresa el nodo siguiente del nodo.
         * @return el nodo siguiente del nodo.
         */
        public Nodo getSiguiente() {
            // Aquí va su código.
            return this.siguiente;
        }

        /**
         * Regresa el elemento del nodo.
         * @return el elemento del nodo.
         */
        public Estudiante get() {
            // Aquí va su código.
            return this.elemento;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
	int r = 0;
	Nodo n = cabeza;
	while(n != null)
	    {
		r++;
		n=n.siguiente;

	    }
        return r;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return cabeza == null;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar. El elemento se agrega únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void agregaFinal(Estudiante elemento) {
        // Aquí va su código.
        if(elemento == null)
            return;
        Nodo n = new Nodo(elemento);
        longitud++;
        if(rabo == null)
            cabeza = rabo = n;
        else{
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        }
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar. El elemento se agrega únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void agregaInicio(Estudiante elemento) {
        // Aquí va su código.
        if(elemento == null)
            return;
        Nodo n = new Nodo(elemento);
        longitud++;
        if(cabeza == null)
            cabeza = rabo = n;
        else{
            cabeza.anterior = n;
            n.siguiente = cabeza;
            cabeza = n;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar. El elemento se inserta únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void inserta(int i, Estudiante elemento) {
        // Aquí va su código.
        if(elemento == null)
            return;
       else  if(i <= 0)
            agregaInicio(elemento);
       else if(i >= this.longitud)
            agregaFinal(elemento);
       else{
        Nodo n = new Nodo(elemento);
        Nodo c = cabeza;

        while(i-- > 1)
            c = c.siguiente;
        n.anterior = c;
        n.siguiente = c.siguiente;
        c.siguiente.anterior = n;
        c.siguiente = n;
        longitud++;
    }
    }

    private Nodo buscaNodo(Estudiante elemento){
        Nodo n = cabeza;
        while(n != null){
            if(n.elemento.equals(elemento))
                return n;
            n = n.siguiente;
        }
        return null;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(Estudiante elemento) {
        // Aquí va su código.
        Nodo r = this.buscaNodo(elemento);

        if(r == null)
            return;
       else if(r.elemento.equals(cabeza.elemento))
            eliminaPrimero();
       else if(r.elemento.equals(rabo.elemento))
            eliminaUltimo();
       else{
        r.anterior.siguiente = r.siguiente;
        r.siguiente.anterior = r.anterior;
        longitud--;
       }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo, o
     *         <code>null</code> si la lista es vacía.
     */
    public Estudiante eliminaPrimero() {
        // Aquí va su código.
        if(cabeza == null)
            return null;
        Estudiante r = cabeza.elemento;
        if(longitud == 1){
            longitud--;
            cabeza=rabo=null;
        }
        else{
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
            longitud--;
        }
        return r;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo, o
     *         <code>null</code> si la lista es vacía.
     */
    public Estudiante eliminaUltimo() {
        // Aquí va su código.
        if(rabo == null)
            return null;
        Estudiante r = rabo.elemento;

        if(longitud == 1){
            cabeza = rabo = null;
            //longitud = 0;
            longitud--;
        }else{
            rabo = rabo.anterior;
            rabo.siguiente = null;
            longitud--;
        }
        return r;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(Estudiante elemento) {
        // Aquí va su código.
        Nodo n = cabeza;
        while(n != null){
            if(n.elemento.equals(elemento))
                return true;
            n = n.siguiente;
        }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public ListaEstudiante reversa() {
        // Aquí va su código.
        ListaEstudiante r = new ListaEstudiante();
        Nodo n = rabo;
        while(n != null){
            r.agregaFinal(n.elemento);
            n = n.anterior;
        }
        return r;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public ListaEstudiante copia() {
        // Aquí va su código.
        ListaEstudiante r = new ListaEstudiante();
        Nodo n = cabeza;
        while(n != null){
            r.agregaFinal(n.elemento);
            n = n.siguiente;
        }
        return r;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        // Aquí va su código.
        cabeza = rabo = null;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista, o <code>null</code> si la lista
     *         es vacía.
     */
    public Estudiante getPrimero() {
        // Aquí va su código.
        if(cabeza == null)
            return null;
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista, o <code>null</code> si la lista
     *         es vacía.
     */
    public Estudiante getUltimo() {
        // Aquí va su código.
        if(rabo == null)
            return null;
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, o <code>null</code> si
     *         <em>i</em> es menor que cero o mayor o igual que el número de
     *         elementos en la lista.
     */
    public Estudiante get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= longitud)
            return null;
        Nodo n = cabeza;
        int c = 0;
        while(c++ < i)
            n = n.siguiente;
        return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(Estudiante elemento) {
        // Aquí va su código.
        int r = 0;
        Nodo n = cabeza;
        while(n != null){
            if(n.elemento.equals(elemento))
                return r;
            n = n.siguiente;
            r++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    public String toString() {
        // Aquí va su código.
        if(cabeza == null)
            return "[]";
        String r = "[" + cabeza.elemento.toString();
        Nodo n = cabeza.siguiente;
        while(n != null){
            r += ", " + n.elemento.toString();
            n = n.siguiente;
        }
        return r + "]";
    }

    /**
     * Nos dice si la lista es igual a la lista recibida.
     * @param lista la lista con la que hay que comparar.
     * @return <tt>true</tt> si la lista es igual a la recibida;
     *         <tt>false</tt> en otro caso.
     */
    public boolean equals(ListaEstudiante lista) {
             	if(lista == null || longitud != lista.longitud)
	    return false;
	
	Nodo n = cabeza;
	Nodo n1 = lista.cabeza;
	while(n!=null){
	    
		if(n.elemento.equals(n1.elemento))
		    { n=n.siguiente;
			n1=n1.siguiente;}
		else{
		    return false;
               	}
	    }
		return true;

          
    }

    /**
     * Regresa el nodo cabeza de la lista.
     * @return el nodo cabeza de la lista.
     */
    public Nodo getCabeza() {
        // Aquí va su código.
        return cabeza;
    }

    /**
     * Regresa el nodo rabo de la lista.
     * @return el nodo rabo de la lista.
     */
    public Nodo getRabo() {
        // Aquí va su código.
        return rabo;
    }
}
