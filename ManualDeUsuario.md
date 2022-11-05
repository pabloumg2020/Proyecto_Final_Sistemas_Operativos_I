# Simulador Gestor de Memoria
Este proyecto es un ejemplo de como el Sistema Operativo de una computadora gestiona la memoria principal, la memoria virtual y el uso de paginación para la ejecución de todos los procesos que se ejecutan.

Simulamos que creamos procesos en el segmento Crear Nuevo Proceso, para ello debemos agregar nombre y cantidad de memoria del proceso, luego dar click en el botón Crear.

![imagen1](8.jpg)

La memoria simulada en el programa está dada en KB, el total de memoria es 2097152 KB y fué dividida en 10 segmentos de 200000 KB lo cual también corresponde al tamaño de cada página.

![imagen2](2.jpg)

Cuando se crea un proceso, si este no es mayor al tamaño total de memoria inicia su ejecución, se muestra en la tabla Procesos y en la tabla Asignacion de Memoria.

![imagen3](9.jpg)

![imagen4](10.jpg)

Cuando se ocupan todos los segmentos de la memoria principal, los procesos son cargados en la memoria virtual, lo que se muestra en la tabla Memoria Virtual.

![imagen5](3.jpg)

La tabla Paginas nos muestra que proceso ocupa cada pagina y el tamaño de memoria.

![imagen6](4.jpg)

La Tabla de Paginas nos muestra la pagina y el marco utilizado por cada proceso

![imagen7](5.jpg)

Cuando un proceso termina su ejecución se libera un segmento de memoria, si hay procesos en memoria virtual pasa uno a ocupar el espacio necesario en memoria principal.

![imagen8](6.jpg)

Si un proceso termina su ejecución libera la pagina que estaba utilizando.

![imagen9](7.jpg)

