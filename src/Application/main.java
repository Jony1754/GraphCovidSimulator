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
    static final ListaEnlazada<Integer> numContagiados = new ListaEnlazada<>();

    public static void main(String[] args) {

        // Dependiendo de la decisión se asigna el número de máscarillas.
        int decisionMascarillas = 1;
        final boolean MASCARILLAS;
        final int NUM_NODOS = 20;
        final int MAX_NODOS_CERCANOS;
        if (NUM_NODOS >= 3) {
            MAX_NODOS_CERCANOS = (int) NUM_NODOS / 3;
        } else{
            MAX_NODOS_CERCANOS = 1;
        }
        final int DISTANCIA_MAX = 5;

        switch (decisionMascarillas) {
            case 0:
                MASCARILLAS = true;
                crearPersonas(NUM_NODOS, MASCARILLAS);
                break;
            case 1:
                MASCARILLAS = false;
                crearPersonas(NUM_NODOS, MASCARILLAS);
                break;
            case 2:
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
        
        Persona contagio = personas.get(random.nextInt(NUM_NODOS));
        System.out.println("Primer contagiado: " + contagio.getID());       
        contagio.setContagio(true);
        numContagiados.add(contagio.getID());
        grafo.recorrerGrafo();        
        
        // Se obtienen los posibles contagios para esa Persona.
        ListaEnlazada distancias = (ListaEnlazada) grafo.getPesos().get(contagio.getID() - 1);
        ListaEnlazada adyacencias = (ListaEnlazada) grafo.getAristas().get(contagio.getID() - 1);
        ListaEnlazada posiblesContagios = grafo.obtenerPosibleContagios(contagio.getID() - 1);
        ListaEnlazada p = posiblesContagios.getPtr().getLink();
        while (p != null){
            Persona person = (Persona) p.getDato();    
            int index = adyacencias.index(person) - 1;
            int distancia = (Integer) distancias.get(index);
            float probabilidad = calcularProbabilidad(contagio.getMascarilla(), person.getMascarilla(), distancia);
            contagiar(person, probabilidad);
            p = p.getLink();
        }
        System.out.println("");
        System.out.println("*****************************************************");
        System.out.println(""); 
        grafo.recorrerGrafo();
        System.out.println("El número de contagiados hasta ahora es: " + numContagiados.getSize());
    }

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

    public static void crearPersonas(int numNodos, boolean mascarilla) {
        for (int i = 0; i < numNodos; i++) {
            personas.add(new Persona(mascarilla));
        }
    }

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
    
    public static void contagiar(Persona persona, float probabilidad){
        if (!persona.isContagio()){
            double prob = random.nextDouble(); 
            if (prob <= probabilidad){
                persona.setContagio(true);
                System.out.println("La persona " + persona.getID() + " fue contagiada :(");
                numContagiados.add(persona.getID());
            }
        }
    } 
}
