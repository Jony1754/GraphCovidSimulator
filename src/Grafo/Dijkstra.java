
package Grafo;

/**
 *  Clase Dijkstra para calcular el caminos mínimo entre dos nodos del grafo.
 * 
 * @author Enrique Niebles
 */
public class Dijkstra {

    private final int HUERFANO = -1;
    private final int INFINITO = (int) 1e09;
    private final int NUM_NODOS;
    private ListaEnlazada<ListaEnlazada> listaDistancias;
    private ListaEnlazada<ListaEnlazada> caminos;
    private ListaEnlazada<Integer> filas;
    private ListaEnlazada<Integer> rutas = new ListaEnlazada<>();

    /**
     * Constructor de la clase Dijkstra.
     * 
     * @param numNodos Número de nodos del grafo.
     * @param listaDistancias Representación de las distancias del grafo.
     */
    public Dijkstra(int numNodos, ListaEnlazada listaDistancias) {
        this.NUM_NODOS = numNodos;
        this.listaDistancias = listaDistancias;
        this.caminos = new ListaEnlazada<>();
    }

    /**
     * Método para calcular los caminos mínimos entre los nodos.
     * 
     * @param verticeInicio Vértice inicial para la búsqueda.
     * @param nodo2find Vértice a encontrar.
     */
    public void dijkstra(int verticeInicio, int nodo2find) {
        ListaEnlazada<Integer> distancias = new ListaEnlazada<>();
        ListaEnlazada<Boolean> añadidos = new ListaEnlazada<>();
        ListaEnlazada<Integer> padres = new ListaEnlazada<>();

        for (int i = 0; i < NUM_NODOS; i++) {
            distancias.add(INFINITO);
            añadidos.add(false);
            padres.add(0);
        }

        distancias.set(verticeInicio, 0);
        padres.set(verticeInicio, HUERFANO);

        for (int i = 1; i < NUM_NODOS; i++) {
            int verticeCercano = -1;
            int distMin = INFINITO;
            for (int vertice = 0; vertice < NUM_NODOS; vertice++) {
                if (!añadidos.get(vertice) && distancias.get(vertice) < distMin) {
                    verticeCercano = vertice;
                    distMin = distancias.get(vertice);
                }
            }

            añadidos.set(verticeCercano, true);

            for (int vertice = 0; vertice < NUM_NODOS; vertice++) {
                int distancia = (Integer) ((ListaEnlazada) this.listaDistancias.get(verticeCercano)).get(vertice);
                if (distancia > 0 && ((distMin + distancia) < distancias.get(vertice))) {
                    padres.set(vertice, verticeCercano);
                    distancias.set(vertice, distMin + distancia);
                }
            }
        }

        caminoMinimo(nodo2find, distancias, padres);
    }

    /**
     * Obtener la ruta del recorrido entre ambos nodos.
     * 
     * @param nodo2find Nodo final del recorrido.
     * @param distancias Lista de distancias entre los nodos.
     * @param padres  Lista con los padres de cada nodo.
     */
    private void caminoMinimo(int nodo2find, ListaEnlazada distancias, ListaEnlazada padres) {
        if ((Integer) distancias.get(nodo2find) != 0 && (Integer) distancias.get(nodo2find) != 9999) {
            filas = new ListaEnlazada<>();
            obtenerCamino(nodo2find, padres);
            caminos.add(filas);
            rutas.add((Integer) distancias.get(nodo2find));
        }
    }

    /**
     * Añade los nodos que deben recorrerse.
     * 
     * @param vertice Vértice que se desea analizaar.
     * @param padres Padres de los vértices.
     */
    private void obtenerCamino(int vertice, ListaEnlazada padres) {
        if (vertice == HUERFANO) {
            return;
        }
        obtenerCamino((Integer) padres.get(vertice), padres);
        filas.add(vertice);        
    }

    /**
     * Se obtienen los caminos a recorrer.
     * 
     * @return ListaEnlazada con las rutas a recorrer.
     */
    public ListaEnlazada<ListaEnlazada> getCaminos() {
        return caminos;
    }

    /**
     * Se obtienen las distancias que hay entre los nodos de cada ruta a recorrer.
     * 
     * @return ListaEnlazada con las distancias mínimas de cada ruta.
     */
    public ListaEnlazada<Integer> getRutas() {
        return rutas;
    }   
}
