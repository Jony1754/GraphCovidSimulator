package Application;

import Grafo.Dijkstra;
import Grafo.Grafo;
import Grafo.ListaEnlazada;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Enrique Niebles
 */
public class main {

    /**
     * @param args the command line arguments
     */
    static final Random random = new Random();
    static ListaEnlazada<Persona> personas = new ListaEnlazada<>();
    static ListaEnlazada<Persona> personasAContagiar = new ListaEnlazada<>();
    static final ListaEnlazada<Persona> contagiados = new ListaEnlazada<>();
    static ListaEnlazada<ListaEnlazada> rutasContagios = new ListaEnlazada<>();
    static ListaEnlazada<ListaEnlazada> grafoDis;
    static int iteracion = 0;

    public static void main(String[] args) throws InterruptedException {

        // Dependiendo de la decisión se asigna el número de máscarillas.
        int decisionMascarillas = 2;
        final boolean MASCARILLAS;
        final int NUM_NODOS;
        Scanner sc = new Scanner(System.in);
        System.out.println("Digie el número de personas para esta simulación: ");
        NUM_NODOS = sc.nextInt();
        final int MAX_NODOS_CERCANOS;

        // Se garantiza un número mínimo de aristas entre los nodos.
        // Número mínimo de personas: 2 (VALIDAR). 
        if (NUM_NODOS >= 5) {
            MAX_NODOS_CERCANOS = random.nextInt((int) NUM_NODOS / 3) + 3;
        } else {
            MAX_NODOS_CERCANOS = 1;
        }

        // Se establece un peso máximo entre nodos.
        final int DISTANCIA_MAX = 6;

        // Se verfica la decisión del usuario sobre el uso de mascarillas por parte de la población.
        switch (decisionMascarillas) {
            case 0:
                // Todos usan mascarillas.
                MASCARILLAS = true;
                crearPersonas(NUM_NODOS, MASCARILLAS);
                break;
            case 1:
                // Nadie usa mascarillas.
                MASCARILLAS = false;
                crearPersonas(NUM_NODOS, MASCARILLAS);
                break;
            case 2:
                // El uso de mascarillas es aleatorio.
                crearPersonas(NUM_NODOS);
                break;
        }

        // Creación del grafo.
        Grafo grafo = new Grafo(personas, NUM_NODOS, DISTANCIA_MAX, MAX_NODOS_CERCANOS);
        System.out.println("Creando grafo...");
        grafo.crearGrafo();
        System.out.println("Grafo creado.");
        grafoDis = obtenerListaDistancias(grafo, NUM_NODOS);
        // Se imprime el grafo inicial.
        grafo.recorrerGrafo();
        System.out.println("");
        System.out.println("*****************************************************");
        System.out.println("");

        // Se obtiene una persona aleatoriamente para establecer el contagio.
        Persona contagio = personas.get(random.nextInt(NUM_NODOS));
        System.out.println("Primer contagiado: " + contagio.getID());
        contagio.setContagio(true);
        // Los contagiados se almacenan en una ListaEnlazada.
        contagiados.add(contagio);
        iteracion++;
        grafo.recorrerGrafo();
        System.out.println("Num de iteraciones: " + iteracion);
        System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());

        ListaEnlazada posiblesContagios = grafo.obtenerAdyacencias(contagio.getID() - 1);
        personasAContagiar.add(contagio);
        rutasContagios.add(posiblesContagios.getPtr());
        obtenerContagios(posiblesContagios, grafo);

        // =====================================================================================
        // Esto es lo que va dentro del botón, necesitas escoger el nodo a analizar.
        // Método para obtener la ruta de mayor riesgo de contagio al hacer click a un nodo no contagiado.
        int personID = 2;
        ListaEnlazada r = calcularRuta(NUM_NODOS, personID);
        // Recorrer la ruta mínima por el algoritmo de Dijkstra.
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        while (r != null) {
            Integer tempID = (Integer) r.getDato();
            Persona person = (Persona) personas.get(tempID);
            System.out.println(person.getID() + "\t " + person.isContagiada());
            r = r.getLink();
        }

        // =====================================================================================
        
        contagioPoblacion(grafo);
        // Se imprime el resultado final de los contagiados.
        System.out.println("\n\n\t\tRESULTADOS\n\n");
        //grafo.recorrerGrafo();
        System.out.println("\nNum de iteraciones: " + iteracion);
        System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());

        // =====================================================================================
        // Método para obtener la ruta de mayor riesgo de contagio al hacer click a un nodo no contagiado.
        r = calcularRuta(NUM_NODOS, personID);
        // Recorrer la ruta mínima por el algoritmo de Dijkstra.
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        while (r != null) {
            Integer tempID = (Integer) r.getDato();
            Persona person = (Persona) personas.get(tempID);
            System.out.println(person.getID() + "\t " + person.isContagiada());
            r = r.getLink();
        }

        // =====================================================================================
    }

    /**
     * Se construye un conjunto de lista con las distancias entre todos los
     * nodos, con el fin de analizar posteriormente el camino mínimo con el
     * agoritmo de Dijkstra.
     *
     * @param grafo Grafo que desea analizarse.
     * @param numNodos Número de nodos que tiene el grafo.
     * @return ListaEnlazada con la información completa del grafo.
     */
    public static ListaEnlazada obtenerListaDistancias(Grafo grafo, int numNodos) {
        ListaEnlazada<ListaEnlazada> grafoDistancias = new ListaEnlazada<>();
        ListaEnlazada<Integer> filas;
        ListaEnlazada ady = grafo.getAristas().getPtr();
        ListaEnlazada adp = grafo.getPesos().getPtr();
        while (ady != null) {
            filas = new ListaEnlazada<>();
            ListaEnlazada nodos = (ListaEnlazada) ady.getDato();
            ListaEnlazada pesos = (ListaEnlazada) adp.getDato();
            for (int i = 1; i <= numNodos; i++) {
                int index = nodos.index(i);
                if (nodos.hasDato(i) && index != 0) {
                    filas.add((Integer) pesos.get(index - 1));
                } else if (index == 0) {
                    filas.add(0);
                } else {
                    filas.add(9999);
                }
            }
            grafoDistancias.add(filas);
            adp = adp.getLink();
            ady = ady.getLink();
        }
        return grafoDistancias;
    }

    /**
     * Se obtiene la ruta de contagio en la cual un nodo puede ser contagiado.
     *
     * @param numNodos Número de nodos que tiene el grafo.
     * @param personID ID de la persona no contagiada.
     * @return
     */
    public static ListaEnlazada calcularRuta(int numNodos, int personID) {
        ListaEnlazada<ListaEnlazada> rutas = new ListaEnlazada<>();
        ListaEnlazada<ListaEnlazada> distancias = new ListaEnlazada<>();
        Dijkstra dijkstra = new Dijkstra(numNodos, grafoDis);
        for (int i = 0; i < numNodos; i++) {
            if (i != personID) {
                dijkstra.dijkstra(i, personID - 1);
                rutas.add(dijkstra.getCaminos());
                distancias.add(dijkstra.getRutas());
            }
        }
        return menorRuta(rutas, distancias);
    }

    /**
     * Se obtiene la ruta de mayor riesgo de contagio de un nodo no contagiado.
     *
     * @param rutas Rutas mínimas entre todos los nodos y el nodo final.
     * @param distancias Distancia que tiene cada ruta.
     * @return ListaEnlazada con la ruta de mayor riesgo de contagio.
     */
    private static ListaEnlazada menorRuta(ListaEnlazada<ListaEnlazada> rutas, ListaEnlazada<ListaEnlazada> distancias) {
        ListaEnlazada rutaMinima = null;
        ListaEnlazada p = ((ListaEnlazada) rutas.getPtr().getDato()).getPtr();
        ListaEnlazada d = ((ListaEnlazada) distancias.getPtr().getDato()).getPtr();
        int distTemp = 9999;
        ListaEnlazada rutaTemp = null;
        while (p != null) {
            int distancia = (Integer) d.getDato();
            int numContagiados = 0;
            ListaEnlazada q = ((ListaEnlazada) p.getDato()).getPtr();
            rutaTemp = q;
            while (q != null) {
                Integer hentai = (Integer) q.getDato();
                Persona person = (Persona) personas.get(hentai);
                if (q.getLink() != null) {
                    if (q.getLink().getLink() == null && person.isContagiada()) {
                        numContagiados++;
                    }
                }
                q = q.getLink();
            }
            if (numContagiados == 1) {
                rutaMinima = rutaTemp;
            } else if (distancia < distTemp) {
                distTemp = distancia;
                rutaMinima = rutaTemp;
            }
            d = d.getLink();
            p = p.getLink();
        }
        return rutaMinima;
    }

    /**
     * Lleva a cabo el contagio automático de la población, dependiendo del
     * contagio incial.
     *
     * @param grafo Grafo que contiene la población.
     */
    public static void contagioPoblacion(Grafo grafo) {
        while (rutasContagios.getSize() != contagiados.getSize()) {
            contagioManual(grafo);
            //grafo.recorrerGrafo();
        }
    }

    /**
     * Lleva a cabo el contagio por iteraciones de la población, dependiendo del
     * contagio incial.
     *
     * @param grafo Grafo que contiene la población.
     */
    public static void contagioManual(Grafo grafo) {
        if (rutasContagios.getSize() != contagiados.getSize()) {
            System.out.println();
            ListaEnlazada q = rutasContagios.getPtr();
            iteracion++;
            int i = 1;
            System.out.println("================================================");
            ListaEnlazada<Persona> contagios = new ListaEnlazada<>();
            while (q != null) {
                System.out.println("\nCamino: " + i);
                ListaEnlazada m = (ListaEnlazada) q.getDato();
                Persona person = (Persona) m.getDato();
                if (!contagios.hasDato(person)) {
                    if (person.isContagiada()) {
                        ListaEnlazada adyacencias = (ListaEnlazada) grafo.getAristas().get(person.getID() - 1);
                        ListaEnlazada distancias = (ListaEnlazada) grafo.getPesos().get(person.getID() - 1);
                        System.out.println("Persona principal: " + person.getID());
                        m = m.getLink();
                        while (m != null) {
                            Persona vecino = (Persona) m.getDato();
                            System.out.println("\tVecino: " + vecino.getID());
                            if (!vecino.isContagiada()) {
                                int index = adyacencias.index(vecino) - 1;
                                int distancia = (Integer) distancias.get(index);
                                float probabilidad = calcularProbabilidad(person.getMascarilla(), vecino.getMascarilla(), distancia);
                                contagiar(vecino, probabilidad);
                                if (vecino.isContagiada()) {
                                    contagios.add(vecino);
                                }
                            }
                            m = m.getLink();
                        }
                    }
                }
                System.out.println("\nNum de iteraciones: " + iteracion);
                System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());
                System.out.println();
                if (rutasContagios.getSize() == contagiados.getSize()) {
                    return;
                }
                q = q.getLink();
                i++;
            }
        }
    }

    /**
     * Posibles nodos que pueden resultar contagiados por una persona.
     *
     * @param grafo Grafo a realizar el estudio.
     * @param ID Persona la cual se busca analizar.
     */
    public static void getContagios(Grafo grafo, int ID) {
        ListaEnlazada w = grafo.obtenerPosibleContagios(ID - 1);
        ListaEnlazada f = w.getPtr();
        while (f != null) {
            Persona pf = (Persona) f.getDato();
            System.out.println("Persona : " + pf.getID());
            f = f.getLink();
        }
    }

    /**
     * Se obtienen todos los contagios que pueden darse a lo largo de las
     * iteraciones.
     *
     * @param contagios ListaEnlazada con los nodos a contagiar.
     * @param grafo Grafo que contiene la información a recorrer.
     */
    public static void obtenerContagios(ListaEnlazada contagios, Grafo grafo) {
        ListaEnlazada p = contagios.getPtr();
        while (p != null) {
            Persona person = (Persona) p.getDato();
            if (!personasAContagiar.hasDato(person)) {
                personasAContagiar.add(person);
                ListaEnlazada posiblesContagios = grafo.obtenerAdyacencias(person.getID() - 1);
                if (posiblesContagios.getSize() != 0) {
                    rutasContagios.add(posiblesContagios.getPtr());
                    obtenerContagios(posiblesContagios, grafo);
                }
            }
            p = p.getLink();
        }
    }

    /**
     * Se reinician los parámetros para una nueva simulación.
     */
    public static void reiniciarSimulacion() {
        personas.clear();
        personasAContagiar.clear();
        contagiados.clear();
        rutasContagios.clear();
        iteracion = 0;
    }

    /**
     * Se crea la ListaEnlazada con las personas que conforman la población de
     * estudio, donde el uso de mascarilla es aleatorio.
     *
     * @param numNodos Número de nodos que tendrá el grafo.
     */
    public static void crearPersonas(int numNodos) {
        reiniciarSimulacion();
        int contBoolean = 0;
        int personasMascarillas = 0;
        final int MAX_MASCARILLAS = random.nextInt(numNodos);
        for (int i = 0; i < numNodos; i++) {
            Boolean mascarilla = random.nextBoolean();
            if (mascarilla) {
                ++contBoolean;
            }
            if (contBoolean >= MAX_MASCARILLAS) {
                while (mascarilla) {
                    mascarilla = random.nextBoolean();
                }
            }
            if (mascarilla) {
                ++personasMascarillas;
            }
            personas.add(new Persona(mascarilla));
        }

        System.out.println("Número de máximo de personas con mascarilla: " + MAX_MASCARILLAS);
        System.out.println("Número de personas con máscarillas: " + personasMascarillas);
        System.out.println("");
    }

    /**
     * Se crea la ListaEnlazada con las personas que conforman la población de
     * estudio.
     *
     * @param numNodos Número de nodos que tendrá el grafo.
     * @param mascarilla Información sobre el uso de mascarillas en la
     * población.
     */
    public static void crearPersonas(int numNodos, boolean mascarilla) {
        reiniciarSimulacion();
        for (int i = 0; i < numNodos; i++) {
            personas.add(new Persona(mascarilla));
        }
    }

    /**
     * Se calcula la probalidad que tienen dos personas de contagiarse.
     *
     * @param bool1 Booleano sobre el uso de mascarillas de la persona 1.
     * @param bool2 Booleano sobre el uso de mascarillas de la persona 2.
     * @param distancia Distancia que existe entre ambas personas.
     * @return float con la probabilidad de contagio.
     */
    public static float calcularProbabilidad(boolean bool1, boolean bool2, int distancia) {
        if (distancia <= 2) {
            if (!bool1 && !bool2) {
                return 0.9F;
            } else if (!bool1 && bool2) {
                return 0.6F;
            } else if (bool1 && !bool2) {
                return 0.4F;
            } else {
                return 0.3F;
            }
        } else {
            if (!bool1 && !bool2) {
                return 0.8F;
            } else if (!bool1 && bool2) {
                return 0.4F;
            } else if (bool1 && !bool2) {
                return 0.3F;
            } else {
                return 0.2F;
            }
        }
    }

    /**
     * Dependiendo de la probabilidad, se establece si una persona se contagia o
     * no.
     *
     * @param persona Persona que puede resultar contagiada.
     * @param probabilidad Probabilidad que tiene la persona de contagiarse.
     */
    public static void contagiar(Persona persona, float probabilidad) {
        double prob = random.nextDouble();
        if (prob <= probabilidad) {
            persona.setContagio(true);
            contagiados.add(persona);
            System.out.println("La persona " + persona.getID() + " fue contagiada :(");
        }
    }
}
