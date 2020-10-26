package Grafo;

import Application.Persona;

/**
 * Clase ListaEnlazada para el manejo de información en el proyecto.
 *
 * @author Enrique Niebles
 */
public class ListaEnlazada<T> {

    private T dato;
    private ListaEnlazada ptr;
    private ListaEnlazada link;
    private int size = 0;

    /**
     * Constructor de la clase.
     *
     */
    public ListaEnlazada() {
        this.ptr = null;
    }

    /**
     * Obtener el dato de la ListaEnlazada.
     *
     * @return T Dato almacenado.
     */
    public T getDato() {
        return dato;
    }

    /**
     * Se obtiene el link del Nodo.
     *
     * @return ListaEnlazada Link del Nodo.
     */
    public ListaEnlazada getLink() {
        return link;
    }

    /**
     * PTR de la ListaEnlazada.
     *
     * @return PTR.
     */
    public ListaEnlazada getPtr() {
        return ptr;
    }

    /**
     * Se obtiene el tamaño de la ListaEnlazada.
     *
     * @return Size tamaño de la lista.
     */
    public int getSize() {
        return size;
    }

    /**
     * Asigna el PTR de la ListaEnlazada.
     *
     * @param ptr PTR a asignar.
     */
    public void setPtr(ListaEnlazada ptr) {
        this.ptr = ptr;
    }

    /**
     * Añadir dato T en la ListaEnlazada.
     *
     * @param dato T dato a añadir.
     */
    public void add(T dato) {
        ListaEnlazada p = ptr;
        ListaEnlazada q = new ListaEnlazada();
        q.dato = dato;
        if (ptr == null) {
            ptr = q;
        } else {
            while (p.link != null) {
                p = p.link;
            }
            p.link = q;
        }
        size++;
        this.setPtr(ptr);
    }

    /**
     * Obtener dato en una posición específica de la ListaEnlazada.
     *
     * @param index Posición de la cual se quiere extraer el dato.
     * @return T dato de la lista enlazada Enlazada en dicha posición.
     */
    public T get(int index) {
        ListaEnlazada p = this.ptr;
        int i = 0;
        while (p != null) {
            if (i == index) {
                T dato = (T) p.dato;
                return dato;
            }
            p = p.link;
            i++;
        }
        return null;
    }

    /**
     * Obtener el índice en la lista de un dato T a buscar.
     *
     * @param busq Dato que se desea buscar en la lista.
     * @return int con la posición del grafo en la lista.
     */
    public int index(T busq) {
        ListaEnlazada p = this.ptr;
        int i = 0;
        while (p != null) {
            T dato = (T) p.dato;
            if (dato.equals(busq)) {
                return i;
            }
            p = p.link;
            i++;
        }
        return -1;
    }

    public void clear() {
        ListaEnlazada p = this.ptr;
        ListaEnlazada q = p;
        while (p != null) {
            q = q.link;
            p = null;
            p = q;
        }
        this.size = 0;
    }

    /**
     * Verfica si la lista contiene una persona con un ID específico.
     *
     * @param busq ID de la persona que se desea buscar en la ListaEnlazada.
     * @return boolean si encontró o no el dato.
     */
    public boolean hasDato(int busq) {
        ListaEnlazada p = this.ptr;
        int i = 0;
        while (p != null) {
            T dato = (T) p.dato;
            if (dato instanceof Persona) {
                Persona person = (Persona) dato;
                if (person.equals(busq)) {
                    return true;
                }
            }
            p = p.link;
            i++;
        }
        return false;
    }

    /**
     * Verfica si la lista tiene un dato en específico.
     *
     * @param busq Información de la cual se quiere extraer el dato.
     * @return boolean si encontró o no el dato.
     */
    public boolean hasDato(T busq) {
        ListaEnlazada p = this.ptr;
        int i = 0;
        while (p != null) {
            T dato = (T) p.dato;
            if (dato.equals(busq)) {
                return true;
            }
            p = p.link;
            i++;
        }
        return false;
    }

    /**
     * Se verifica si la ListaEnlazada tiene al menos un dato en ella.
     *
     * @return true si la ListaEnlazada está vacía.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }
}
