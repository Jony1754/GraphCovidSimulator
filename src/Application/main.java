/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import Grafo.Grafo;
import Grafo.ListaEnlazada;
import java.util.Random;

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
    static final ListaEnlazada<Persona> numContagiados = new ListaEnlazada<>();
    static int iteracion = 0;

    public static void main(String[] args) {

        // Dependiendo de la decisión se asigna el número de máscarillas.
        int decisionMascarillas = 1;
        final boolean MASCARILLAS;
        final int NUM_NODOS = 20;
        final int MAX_NODOS_CERCANOS;
        // Se garantiza un número mínimo de aristas entre los nodos.
        if (NUM_NODOS >= 3) {
            MAX_NODOS_CERCANOS = (int) NUM_NODOS / 3;
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
        grafo.crearGrafo();

        // Recorrer grafo.
        grafo.recorrerGrafo();
        System.out.println("");
        System.out.println("*****************************************************");
        System.out.println("");

        // Se obtiene una persona aleatoriamente para establecer el contagio.
        Persona contagio = personas.get(random.nextInt(NUM_NODOS));
        System.out.println("Primer contagiado: " + contagio.getID());
        contagio.setContagio(true);
        // Los contagiados se almacenan en una ListaEnlazada.
        numContagiados.add(contagio);
        
        ////////////////////////////////////////////////////////////////////////////
        // Recorrer grafo hasta que se alcancen todos los contagios.
        
        // Se obtienen los posibles contagios para esa Persona.
        System.out.println("");
        System.out.println("*****************************************************");
        System.out.println("");
        grafo.recorrerGrafo();
        ListaEnlazada distancias = (ListaEnlazada) grafo.getPesos().get(contagio.getID() - 1);
        ListaEnlazada adyacencias = (ListaEnlazada) grafo.getAristas().get(contagio.getID() - 1);
        // Se obtienen los posibles contagios que puede tener el nodo.
        ListaEnlazada posiblesContagios = grafo.obtenerPosibleContagios(contagio.getID() - 1);
        ListaEnlazada p = posiblesContagios.getPtr().getLink();
        while (p != null) {
            Persona vecino = (Persona) p.getDato();
            int index = adyacencias.index(vecino) - 1;
            int distancia = (Integer) distancias.get(index);
            float probabilidad = calcularProbabilidad(contagio.getMascarilla(), vecino.getMascarilla(), distancia);
            contagiar(vecino, probabilidad);
            p = p.getLink();
        }
        System.out.println("El número de contagiados hasta ahora es: " + numContagiados.getSize());
    }

    /**
     * Se crea la ListaEnlazada con las personas que conforman la población de
     * estudio, donde el uso de mascarilla es aleatorio.
     * 
     * @param numNodos Número de nodos que tendrá el grafo.
     */
    public static void crearPersonas(int numNodos) {
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
     * Dependiendo de la probabilidad, se establece si la persona se contagia o
     * no.
     *
     * @param persona Persona que puede resultar contagiada.
     * @param probabilidad Probabilidad que tiene la persona de contagiarse.
     */
    public static void contagiar(Persona persona, float probabilidad) {
        if (!persona.isContagio()) {
            double prob = random.nextDouble();
            if (prob <= probabilidad) {
                persona.setContagio(true);
                numContagiados.add(persona);
                System.out.println("La persona " + persona.getID() + " fue contagiada :(");
            }
        }
    }
}
