# GraphCovidSimulator

Segundo laboratorio para estructuras de datos II: uso de grafos en un simulador. 

# Descripción

Debido a la reciente crisis de **Covid-19** hay una alta demanda de sistemas que logren medir
y predecir el comportamiento del virus en diferentes poblaciones ya sea por el seguimiento
de comportamiento de personas o la predicción de efectos secundarios de decisiones
basados en simulaciones.
La empresa BIK Solutions requiere un simulador de reapertura económica básico que
evidencie la importancia de una de las medidas más recomendadas de parte de las
autoridades e instituciones de la salud: el uso constante del tapocas para disminuir el
riesgo de contagio de este nuevo virus “El coronavirus”. Para esto ha contratado a la
compañía Ingeniería RRRR para que le dé solución a este requerimiento.
La gerente y dueña de Ingeniería RRRR ha dejado esta tarea para que sea resuelta por
todos sus empleados **(Estudiantes de ED II _ 202030)**, confiando absolutamente en la
capacidad y habilidad de cada uno de ellos.

# Requerimientos mínimos

1. La interfaz gráfica debe tener las siguientes funcionalidades:
    * Poder escoger el número de nodos para la simulación.
    * Generar un nuevo grafo para una nueva simulación.
    * Poder escoger entre una población de nodos en las cuales todos usen
       mascarilla, ninguno use mascarilla y en el que el uso de mascarilla sea
       aleatorio.
    * Un botón para pasar a la siguiente iteración.
    * Un contador de iteraciones.
    * Mostrar la ruta de mayor riesgo de contagio al hacer click a un nodo no
        contagiado.
    * Al hacer click a un nodo contagiado debe señalar los posibles nodos a
      contagiar por este.
      
2. El riesgo de contagio dependerá los nodos usan mascarilla.

3. Un nodo no puede contagiar a otro en la misma iteración en la cual se contagió.

4. El primer contagio de la simulación debe ser **aleatorio**.

5. El grafo generado para cada simulación debe ser aleatorio teniendo en cuenta el
   número de nodos establecido por el usuario en la interfaz gráfica.
   
6. Para la simulación se hará uso de grafos dirigidos, por lo cual la dirección de las
aristas debe tener sentido con el riesgo de contagio.


