/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import Grafo.Grafo;
import Grafo.ListaEnlazada;
import static java.lang.Thread.sleep;
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
    static ListaEnlazada<Persona> caminoContagio = new ListaEnlazada<>();
    static final ListaEnlazada<Persona> contagiados = new ListaEnlazada<>();
    static ListaEnlazada<ListaEnlazada> rutasContagios = new ListaEnlazada<>();
    static ListaEnlazada<ListaEnlazada> rutasNodos = new ListaEnlazada<>();
    static int iteracion = 1;

    public static void main(String[] args) throws InterruptedException {

        // Dependiendo de la decisión se asigna el número de máscarillas.
        int decisionMascarillas = 1;
        final boolean MASCARILLAS;
        final int NUM_NODOS = 5;
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
        contagiados.add(contagio);
        iteracion++;
        grafo.recorrerGrafo();
        System.out.println("Num de iteraciones: " + iteracion);
        System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());
//        ListaEnlazada contagios = grafo.obtenerPosibleContagios(contagio.getID() - 1);
//        rutasContagios.add(contagios);
        ListaEnlazada posiblesContagios = grafo.obtenerAdyacencias(contagio.getID() - 1);
        ListaEnlazada p = posiblesContagios.getPtr();
        rutasNodos.add(p);
        contagiosManual(grafo);
        //System.out.println(personas.hasDato(contagio));

        //calcularContagios(grafo);
    }

    public static void contagiosManual(Grafo grafo) throws InterruptedException{
        ListaEnlazada contagios = rutasNodos.getPtr();
        while (contagios != null){
            ListaEnlazada p = (ListaEnlazada) contagios.getDato();
            contagiosPersona(grafo, p);
            iteracion++;
            grafo.recorrerGrafo();
            System.out.println("Num de iteraciones: " + iteracion);
            System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());
            contagios = contagios.getLink();
            sleep(1);
        }
    }
    
    public static void contagiosPersona(Grafo grafo, ListaEnlazada p){
        while (p != null){
            Persona startPerson = (Persona) p.getDato();
            if (!caminoContagio.hasDato(startPerson)){
                System.out.println("Dato añadido: " + startPerson.getID());
                caminoContagio.add(startPerson);
            }
            if (startPerson.isContagio()){
                ListaEnlazada posiblesContagios = grafo.obtenerPosibleContagios(startPerson.getID() - 1);  
                if (!rutasNodos.hasDato(posiblesContagios)){
                    //rutasNodos.add(posiblesContagios);                                  
                    System.out.println("Starting person: " + startPerson.getID()); 
                    ListaEnlazada q = posiblesContagios.getPtr();
                    ListaEnlazada adyacencias = (ListaEnlazada) grafo.getAristas().get(startPerson.getID() - 1);
                    ListaEnlazada distancias = (ListaEnlazada) grafo.getPesos().get(startPerson.getID() - 1);
                    while (q != null) {
                        Persona vecino = (Persona) q.getDato();                        
                        System.out.println("Vecino: " + vecino.getID()); 
                        if (!vecino.isContagio()) {
                            int index = adyacencias.index(vecino) - 1;
                            int distancia = (Integer) distancias.get(index);
                            float probabilidad = calcularProbabilidad(startPerson.getMascarilla(), vecino.getMascarilla(), distancia);
                            contagiar(vecino, probabilidad);
                        }
                        q = q.getLink();
                    }
                }
            }
            p = p.getLink();
        }
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
        double prob = random.nextDouble();
        if (prob <= probabilidad) {
            persona.setContagio(true);
            contagiados.add(persona);
            System.out.println("La persona " + persona.getID() + " fue contagiada :(");
        }
    }

    
    public static void calcularContagios(Grafo grafo) throws InterruptedException {
        while (caminoContagio.getSize() < contagiados.getSize()) {
            ListaEnlazada contagios = rutasContagios.getPtr();
            if (((ListaEnlazada) rutasContagios.getPtr().getDato()).getSize() == 0) {
                break;
            }
            while (contagios != null) {
                System.out.println();
                System.out.println("**********************************************************************");
                System.out.println();
                ListaEnlazada p = ((ListaEnlazada) contagios.getDato()).getPtr();
                while (p != null) {
                    Persona person = (Persona) p.getDato();
                    System.out.println("********************************************************************** " + person.getID());
                    if (!caminoContagio.hasDato(person)) {
                        System.out.println("\t\t\t\tDato añadido: " + person.getID());
                        caminoContagio.add(person);
                    }
                    ListaEnlazada posiblesContagios = grafo.obtenerPosibleContagios(person.getID() - 1);
                    if (!rutasContagios.hasDato(posiblesContagios)) {
                        rutasContagios.add(posiblesContagios);
                        ListaEnlazada q = posiblesContagios.getPtr();
                        ListaEnlazada adyacencias = (ListaEnlazada) grafo.getAristas().get(person.getID() - 1);
                        ListaEnlazada distancias = (ListaEnlazada) grafo.getPesos().get(person.getID() - 1);
                        while (q != null) {
                            Persona vecino = (Persona) q.getDato();
                            if (!vecino.isContagio()) {
                                int index = adyacencias.index(vecino) - 1;
                                int distancia = (Integer) distancias.get(index);
                                float probabilidad = calcularProbabilidad(person.getMascarilla(), vecino.getMascarilla(), distancia);
                                contagiar(vecino, probabilidad);
                            }
                            q = q.getLink();
                        }
                    }
                    p = p.getLink();
                }
                iteracion++;
                grafo.recorrerGrafo();
                System.out.println("Num de iteraciones: " + iteracion);
                System.out.println("El número de contagiados hasta ahora es: " + contagiados.getSize());
                contagios = contagios.getLink();
                sleep(1);
            }
        }
    }
}
