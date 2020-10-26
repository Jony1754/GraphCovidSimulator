package Application;

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
    private static ListaEnlazada<ListaEnlazada> rutas = new ListaEnlazada<>();
    static int iteracion = 0;

    public static void main(String[] args) throws InterruptedException {

        // Para pedir los datos por consola,
        Scanner sc = new Scanner(System.in);
        // Dependiendo de la decisión se asigna el número de máscarillas.
        int decisionMascarillas = 2;
        final boolean MASCARILLAS;
        final int NUM_NODOS;
        System.out.println("Digie el número de personas para esta simulación: ");
        //NUM_NODOS = sc.nextInt();
        NUM_NODOS = 10;
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

        //contagioPoblacion(grafo);   
        //ListaEnlazada<ListaEnlazada> rutas = obtenerRutaContagios(grafo, contagio.getID());

        contagioPoblacion(grafo);
        // Se imprime el resultado final de los contagiados.
        System.out.println("\n\n\t\tRESULTADOS\n\n");
        grafo.recorrerGrafo();
        System.out.println("\nNum de iteraciones: " + iteracion);
        System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());
    }

//    /**
//     * Se obtiene la ruta de contagio en la cual un nodo puede ser contagiado.
//     *
//     * @param ID Nodo el cual se desea analizar.
//     * @return Lista enlazada con los posibles contagios.
//     */
//    public static ListaEnlazada obtenerRutaContagios(Grafo grafo, int ID) {
//        ListaEnlazada ady = grafo.getAristas().getPtr();
//        ListaEnlazada<ListaEnlazada> rutas = new ListaEnlazada<>();
//        while (ady != null) {
//            ListaEnlazada nodos = (ListaEnlazada) ady.getDato();
//            ListaEnlazada ad = nodos.getPtr();
//            Persona principal = (Persona) ad.getDato();
//            if (!principal.equals(ID)) {
//                //ad = ad.getLink();
//                ListaEnlazada<ListaEnlazada> rutasTemp = new ListaEnlazada<>();
//                Persona pf = (Persona) ad.getDato();
//                System.out.println("*********Person : " + pf.getID());
//                ListaEnlazada vecinos = grafo.obtenerAdyacencias(pf.getID() - 1);
//                vecinos = vecinos.getPtr().getLink();
//                recursivo(grafo, vecinos, pf);
//            }
//            ady = ady.getLink();
//        }
//        return rutas;
//    }
//
//    public static void recursivo(Grafo grafo, ListaEnlazada p, Persona per) {
//        if (p == null){
//            return;
//        }
//        else {
//            Persona pf = (Persona) p.getDato();
//            if (pf.equals(per)) {
//                return;
//            } else {
//                System.out.println("=======Person : " + pf.getID());
//                ListaEnlazada vecinos = grafo.obtenerAdyacencias(pf.getID() - 1);
//                rutas.add(vecinos);
//                p = p.getLink();
//                recursivo(grafo, p, per);
//            }
//        }
//    }

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
