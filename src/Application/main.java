/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

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
    static final int NUM_NODOS = 20;
    static final int MAX_NODOS_CERCANOS = (int) NUM_NODOS/2;
    static ListaEnlazada<ListaEnlazada> adyacencias = new ListaEnlazada<>();
    static ListaEnlazada<ListaEnlazada> pesos = new ListaEnlazada<>();
    
    public static void main(String[] args) {
        
        // Dependiendo de la decisión se asigna el número de máscarillas.
        int decisionMascarillas = 2;  
        final boolean MASCARILLAS;
        
        switch (decisionMascarillas){
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
        
        Persona primer_contagio = personas.get(random.nextInt(NUM_NODOS));
        System.out.println("Primer contagiado: " + primer_contagio.getID());
        crearGrafo();
        ListaEnlazada p = adyacencias.getPtr();
        while (p != null){
            ListaEnlazada personas = (ListaEnlazada) p.getDato();
            ListaEnlazada q = personas.getPtr();
            System.out.println("===========================================");
            while (q != null){
                Persona person = (Persona) q.getDato();
                System.out.println("ID: " + person.getID() + "\t| Mascarilla:\t" + Boolean.toString(person.getMascarilla()));
                q = q.getLink();
            }
            p = p.getLink();
        }
    }
    
    public static void crearPersonas(int numNodos){
        int contBoolean = 0;
        int personasMascarillas = 0;
        final int MAX_MASCARILLAS = random.nextInt(numNodos);
        for (int i = 0; i < numNodos; i++) {
            Boolean mascarilla = random.nextBoolean();
            if (mascarilla) ++contBoolean;
            if (contBoolean >= MAX_MASCARILLAS){
                while(mascarilla) mascarilla = random.nextBoolean();
            }
            if (mascarilla) ++personasMascarillas;
            personas.add(new Persona(mascarilla));
        }
        
        System.out.println("Número de máximo de personas con mascarilla: " + MAX_MASCARILLAS);               
        System.out.println("Número de personas con máscarillas: " + personasMascarillas);
        System.out.println("");
    }

    public static void crearPersonas(int numNodos, boolean mascarilla){
        for (int i = 0; i < numNodos; i++) {
            personas.add(new Persona(mascarilla));
        }        
    }
    
    public static void crearGrafo(){
        ListaEnlazada p = personas.getPtr();
        final int DISTANCIA_MAX = random.nextInt(7) + 1;
        int id_random;
        int num_personas;
        while (p != null){
            ListaEnlazada<Persona> nodos = new ListaEnlazada<>();
            ListaEnlazada<Integer> distancias = new ListaEnlazada<>();
            Persona person = (Persona) p.getDato();
            // Se añade a la primera principal de la lista.
            nodos.add(person);
            // Se calcula con cuántas personas tendrá contacto.
            num_personas = random.nextInt(MAX_NODOS_CERCANOS);
            for (int i = 0; i < num_personas; i++) {
                do{
                    // Se calcula el ID de la persona con la que tendrá contacto.
                    id_random = random.nextInt(NUM_NODOS - 1) + 1;
                }while (nodos.hasDato(id_random));
                // Se añade la persona con dicho ID.
                nodos.add(personas.get(id_random - 1));
                distancias.add(random.nextInt(DISTANCIA_MAX));
            }
            pesos.add(distancias);
            adyacencias.add(nodos);
            p = p.getLink();
        }
    }
}
