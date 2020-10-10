/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafo;

import Application.Persona;
import java.util.Random;

/**
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

    public Grafo(ListaEnlazada<Persona> personas, int numNodos, int distanciaMax, int maxNodosCercanos) {
        this.NUM_NODOS = numNodos;
        this.DISTANCIA_MAX = distanciaMax;
        this.MAX_NODOS_CERCANOS =  maxNodosCercanos;
        this.PERSONAS = personas;
        this.PESOS = new ListaEnlazada<>();
        this.ADYACENCIAS = new ListaEnlazada<>();
        this.CONEXOS = new ListaEnlazada<>();
    }

    public void crearGrafo(){        
        ListaEnlazada p = PERSONAS.getPtr();
        int id_random;
        int num_personas;
        while (p != null){
            ListaEnlazada<Persona> nodos = new ListaEnlazada<>();
            ListaEnlazada<Integer> distances = new ListaEnlazada<>();
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
                if (!CONEXOS.hasDato(id_random)){
                    System.out.println("Dato Añadido: "+ id_random);
                    CONEXOS.add(PERSONAS.get(id_random - 1));
                }
                nodos.add(PERSONAS.get(id_random - 1));
                distances.add(random.nextInt(DISTANCIA_MAX) + 1);
            }    
            // Se garantiza que el grafo sea conexo.
            if (p.getLink() == null){
                for (int i = 1; i <= NUM_NODOS; i++) {
                    if (!CONEXOS.hasDato(i)){
                        if (person.getID() == i){
                            ((ListaEnlazada) this.ADYACENCIAS.getPtr().getDato()).add(person);
                            ((ListaEnlazada) this.PESOS.getPtr().getDato()).add(random.nextInt(DISTANCIA_MAX) + 1);
                            CONEXOS.add(person);
                            System.out.println("\tDato Añadido ptr: "+ i);
                        }else {
                            CONEXOS.add(PERSONAS.get(i - 1));
                            System.out.println("\tDato Añadido: "+ i);
                            nodos.add(PERSONAS.get(i - 1));
                            distances.add(random.nextInt(DISTANCIA_MAX) + 1);                            
                        }
                    }
                }
            }
            this.ADYACENCIAS.add(nodos);
            this.PESOS.add(distances);
            p = p.getLink();
        }
    }
    
    public void recorrerGrafo(){
        ListaEnlazada p = this.ADYACENCIAS.getPtr();
        ListaEnlazada dis = this.PESOS.getPtr();
        while (p != null) {
            ListaEnlazada nodos = (ListaEnlazada) p.getDato();
            ListaEnlazada distancia = (ListaEnlazada) dis.getDato();
            ListaEnlazada q = nodos.getPtr();
            Persona fperson = (Persona) q.getDato();
            ListaEnlazada d = distancia.getPtr();
            System.out.println("===========================================");
            while (q != null) {
                Persona person = (Persona) q.getDato();
                System.out.println("ID: " + person.getID() + "\t| Mascarilla:\t" + Boolean.toString(person.getMascarilla()) + "\t | Contagio:\t" + Boolean.toString(person.isContagio()));
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
    
    public ListaEnlazada obtenerPosibleContagios(int index){
        ListaEnlazada p = (ListaEnlazada) this.ADYACENCIAS.get(index);
        return p;
    }
    
    public ListaEnlazada<Persona> getPersonas() {
        return this.PERSONAS;
    }

    public ListaEnlazada<ListaEnlazada> getAristas() {
        return this.ADYACENCIAS;
    }

    public ListaEnlazada<ListaEnlazada> getPesos() {
        return this.PESOS;
    }

    public int getNumNodos() {
        return NUM_NODOS;
    }
}
