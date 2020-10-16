
package Grafo;

import Application.Persona;
import java.util.Random;

/**
 * Clase Grafo, contiene todas las conexiones entre las personas con las cuales
 * se realizará la simulación de contagio del COVID-19.
 *
 * @author Enrique Niebles
 */
public class Grafo {

    private final ListaEnlazada<Persona> PERSONAS;
    private final ListaEnlazada<ListaEnlazada> ADYACENCIAS;
    private final ListaEnlazada<ListaEnlazada> PESOS;
    private final ListaEnlazada<Persona> CONEXOS;
    private final Random random = new Random();
    private final int NUM_NODOS;
    private final int MAX_NODOS_CERCANOS;
    private final int DISTANCIA_MAX;

    /**
     * Constructor de la clase grafo, se crean las conexiones entre las
     * personas.
     *
     * @param personas ListaEnlazada con todas las personas que conformarán la
     * población.
     * @param numNodos Número de nodos que tendrá el grafo.
     * @param distanciaMax Distancia máxima entre nodos.
     * @param maxNodosCercanos Cantidad máxima de conexiones que puede tener un
     * nodo.
     */
    public Grafo(ListaEnlazada<Persona> personas, int numNodos, int distanciaMax, int maxNodosCercanos) {
        this.NUM_NODOS = numNodos;
        this.DISTANCIA_MAX = distanciaMax;
        this.MAX_NODOS_CERCANOS = maxNodosCercanos;
        this.PERSONAS = personas;
        this.PESOS = new ListaEnlazada<>();
        this.ADYACENCIAS = new ListaEnlazada<>();
        this.CONEXOS = new ListaEnlazada<>();
    }

    /**
     * Método que se encarga de crear e grafo, teniendo en cuenta todas las
     * condiciones necesarias para garantizar un grafo conexo.
     *
     */
    public void crearGrafo() {
        ListaEnlazada p = PERSONAS.getPtr();
        int id_random;
        int num_personas;
        while (p != null) {
            ListaEnlazada<Persona> nodos = new ListaEnlazada<>();
            ListaEnlazada<Integer> distances = new ListaEnlazada<>();
            Persona person = (Persona) p.getDato();
            // Se añade a la persona principal en la lista.
            nodos.add(person);
            // Se calcula con cuántas personas tendrá contacto.
            num_personas = random.nextInt(MAX_NODOS_CERCANOS);
            for (int i = 0; i < num_personas; i++) {
                do {
                    // Se calcula el ID de la persona con la que tendrá contacto.
                    id_random = random.nextInt(NUM_NODOS - 1) + 1;
                } while (nodos.hasDato(id_random));
                // Se añade la persona con dicho ID a la lista de conexiones.
                if (!CONEXOS.hasDato(id_random)) {
                    CONEXOS.add(PERSONAS.get(id_random - 1));
                }
                // Se añada la arista de conexión entre ambos nodos y su distancia.
                nodos.add(PERSONAS.get(id_random - 1));
                distances.add(random.nextInt(DISTANCIA_MAX) + 1);
            }
            // Se garantiza que el grafo sea conexo.
            if (p.getLink() == null) {
                for (int i = 1; i <= NUM_NODOS; i++) {
                    // En caso de haber un nodo aislado, se deja que el último nodo apunte a él.
                    if (!CONEXOS.hasDato(i)) {
                        // Se garantiza que el último nodo siempre sea apuntado por alguien.
                        if (person.getID() == i) {
                            ((ListaEnlazada) this.ADYACENCIAS.getPtr().getDato()).add(person);
                            ((ListaEnlazada) this.PESOS.getPtr().getDato()).add(random.nextInt(DISTANCIA_MAX) + 1);
                            CONEXOS.add(person);
                        } else {
                            CONEXOS.add(PERSONAS.get(i - 1));
                            nodos.add(PERSONAS.get(i - 1));
                            distances.add(random.nextInt(DISTANCIA_MAX) + 1);
                        }
                    }
                }
            }
            // Se añaden las conexión a la lista de conexiones (Grafo).
            this.ADYACENCIAS.add(nodos);
            this.PESOS.add(distances);
            p = p.getLink();
        }
    }

    /**
     * Método que se encarga de recorrer el grafo y dibujar su recorrido.
     *
     */
    public void recorrerGrafo() {
        ListaEnlazada p = this.ADYACENCIAS.getPtr();
        ListaEnlazada dis = this.PESOS.getPtr();
        while (p != null) {
            ListaEnlazada nodos = (ListaEnlazada) p.getDato();
            ListaEnlazada distancia = (ListaEnlazada) dis.getDato();
            ListaEnlazada q = nodos.getPtr();
            Persona fperson = (Persona) q.getDato();
            ListaEnlazada d = distancia.getPtr();
            System.out.println("======================================================");
            while (q != null) {
                Persona person = (Persona) q.getDato();
                System.out.println("ID: " + person.getID() + "\t| Mascarilla:\t" + Boolean.toString(person.getMascarilla()) + "\t | Contagio:\t" + Boolean.toString(person.isContagiada()));
                if (!person.equals(fperson)) {
                    System.out.println("Distancia entre " + fperson.getID() + " y " + person.getID() + " es:\t" + d.getDato());
                    d = d.getLink();
                }
                q = q.getLink();
            }
            dis = dis.getLink();
            p = p.getLink();
        }
    }

    /**
     * Se obtiene los posibles contagios que puede tener un nodo, sin tener en
     * cuenta los nodos que ya han sido contagiados.
     *
     * @param index Nodo el cual se desea analizar.
     * @return Lista enlazada con los posibles contagios.
     */
    public ListaEnlazada obtenerPosibleContagios(int index) {
        ListaEnlazada p = ((ListaEnlazada) this.ADYACENCIAS.get(index)).getPtr().getLink();
        ListaEnlazada<Persona> posiblesContagios = new ListaEnlazada<>();
        while (p != null) {
            Persona person = (Persona) p.getDato();
            if (!person.isContagiada()) {
                posiblesContagios.add(person);
            }
            p = p.getLink();
        }
        return posiblesContagios;
    }

    /**
     * Se obtiene los posibles contagios que puede tener un nodo, incluyendo el
     * mismo nodo.
     *
     * @param index Nodo el cual se desea analizar.
     * @return Lista enlazada con los posibles contagios.
     */
    public ListaEnlazada obtenerAdyacencias(int index) {
        ListaEnlazada p = (ListaEnlazada) this.ADYACENCIAS.get(index);
        return p;
    }

    /**
     * Se obtiene la ListaEnlazada de nodos del grafo.
     *
     * @return ListaEnlaza con los nodos que conforman el grafo.
     */
    public ListaEnlazada<Persona> getPersonas() {
        return this.PERSONAS;
    }

    /**
     * Se obtiene la ListaEnlazada de adayacencia del grafo.
     *
     * @return ListaEnlaza con las aristas que conforman el grafo.
     */
    public ListaEnlazada<ListaEnlazada> getAristas() {
        return this.ADYACENCIAS;
    }

    /**
     * Se obtiene la ListaEnlazada de pesos del grafo.
     *
     * @return ListaEnlaza con los pesos.
     */
    public ListaEnlazada<ListaEnlazada> getPesos() {
        return this.PESOS;
    }

    /**
     * Se obtiene el número de nodos del grafo.
     *
     * @return int con el número de nodos.
     */
    public int getNumNodos() {
        return NUM_NODOS;
    }
}
