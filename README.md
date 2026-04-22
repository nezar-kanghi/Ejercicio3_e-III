En este ejercicio se nos pide crear un programa que gestione incidencias de ciclistas almacenadas en una base de datos. Primero vamos a empezar creando un menú mediante un bucle do while y un switch que permite elegir entre insertar una incidencia, mostrar todas las incidencias o mostrar las incidencias de un ciclista concreto.

En el primer case llamamos al metodo insertarIncidencia el cual solicita por consola el id del ciclista, el numero de etapa y el tipo de incidencia. Antes de insertar los datos se realizan dos verificaciones mediante consultas SQL para comprobar que el ciclista y la etapa existen en la base de datos, si uno no existe se ejecuta un rollback y se cancela la operacion pero si todo es correcto, se inserta la incidencia en la tabla incidencia mediante un PreparedStatement y finalmente se realiza un commit para guardar los cambios.

En el segundo case llamamos al metodo mostrarTodas, que realiza una consulta SQL utilizando JOIN entre las tablas incidencia y ciclista para obtener el identificador de la incidencia, el nombre del ciclista, el numero de etapa y el tipo de incidencia, mostrara los datos mediante ResulSet.

En el tercer case llamamos al metodo mostrarPorCiclista, que solicita el id de un ciclista por consola y ejecuta una consulta SQL con JOIN y una condicion WHERE = ? para mostrar solo las incidencias de X ciclista. Mostramos los datos mediante ResulSet.
(Se controlara mediante commit y rollback el programa)
