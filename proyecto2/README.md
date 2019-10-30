Introducción a Ciencias de la Computación
=========================================

Práctica 10: Hilos de ejecución y enchufes
------------------------------------------

### Fecha de entrega: martes 11 de diciembre, 2018

Deben hacer funcionar su base de datos en la red con un patrón
cliente/servidor. Esto implicará completar las clases del paquete
[red](https://aztlan.fciencias.unam.mx/gitlab/2019-1-icc/practica10/blob/master/src/mx/unam/ciencias/icc/red/).

Una vez que hayan terminado sus clases, además de que deben de pasar todas sus
pruebas unitarias, se deben ejecutar correctamente los programas escritos en las
clases
[ClientePractica10](https://aztlan.fciencias.unam.mx/gitlab/2019-1-icc/practica10/blob/master/src/mx/unam/ciencias/icc/ClientePractica10.java) y
[ServidorPractica10](https://aztlan.fciencias.unam.mx/gitlab/2019-1-icc/practica10/blob/master/src/mx/unam/ciencias/icc/ServidorPractica10.java).

Noten que las clases del paquete
[fx](https://aztlan.fciencias.unam.mx/gitlab/2019-1-icc/practica10/blob/master/src/mx/unam/ciencias/icc/fx/)
no tienen pruebas unitarias, así que deben probar manualmente que todos los
componentes de la interfaz gráfica en el cliente funcionen correctamente.

Para ejecutar el servidor, deben especificar el puerto y (opcionalmente) un
archivo con una base de datos:

```shell
$ java -jar servidor-practica10.jar 1234 estudiantes.bd
```

El cliente se ejecuta normalmente:

```shell
$ java -jar cliente-practica10.jar
```

Los únicos archivos que deben modificar son:

* `Arreglos.java`
* `BaseDeDatos.java`,
* `BaseDeDatosEstudiantes.java`,
* `CampoEstudiante.java`,
* `Estudiante.java`,
* `Lista.java`,
* `fx/ControladorFormaBusquedaEstudiantes.java`,
* `fx/ControladorFormaEstudiante.java`,
* `red/Conexion.java`,
* `red/ConexionCliente.java`,
* `red/ConexionServidor.java`,
* `red/Mensaje.java`,
* `red/ServidorBaseDeDatos.java` y
* `red/ServidorBaseDeDatosEstudiantes.java`.

*No deben modificar de ninguna manera ninguno de los otros archivos de la
práctica*.

### Repositorio

Pueden clonar la práctica con el siguiente comando:

```shell
$ git clone https://canek@aztlan.fciencias.unam.mx/gitlab/2019-1-icc/practica10.git
```

### Documentación

La documentación generada por JavaDoc la pueden consultar aquí:

[Documentación generada por JavaDoc para la práctica 10](https://aztlan.fciencias.unam.mx/~canek/2019-1-icc/practica10/)
