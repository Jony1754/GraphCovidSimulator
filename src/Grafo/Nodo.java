/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafo;

import Application.Persona;

/**
 *
 * @author Enrique Niebles
 */
public class Nodo {
    private ListaEnlazada<Persona> personas;
    private ListaEnlazada<Integer> distancias;
    private ListaEnlazada<Boolean> mascarillas;

    public Nodo() {
        this.personas = new ListaEnlazada<>();
        this.distancias = new ListaEnlazada<>();
        this.mascarillas = new ListaEnlazada<>();
    }

    public ListaEnlazada<Persona> getPersonas() {
        return personas;
    }

    public ListaEnlazada<Integer> getDistancias() {
        return distancias;
    }

    public ListaEnlazada<Boolean> getMascarillas() {
        return mascarillas;
    }
    
    
    
    
}
