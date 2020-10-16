
package Application;

/**
 * Clase Persona, contendrá la representación de los nodos para el grafo.
 * 
 * @author Enrique Niebles
 */
public class Persona {
    
    private final boolean MASCARILLA;    
    private static int contID = 0;
    private final int ID;
    private boolean contagio; 

    /**
     * Constructor de la clase Persona.
     * 
     * @param mascarilla información sobre el uso de tapabocas por parte de la persona.
     */
    public Persona(boolean mascarilla) {
        this.MASCARILLA = mascarilla;
        this.ID = ++contID;
    }

    /**
     * Se verifica si la persona usa o no tapabocas.
     * 
     * @return booleano con el resultado de dicha comprobación.
     */
    public boolean getMascarilla() {
        return MASCARILLA;
    }
    
    /**
     * Se obtiene el ID de la persona.
     * 
     * @return int con la ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Se verifica si la persona ha sido contagiada de COVID-19.
     * 
     * @return 
     */
    public boolean isContagiada() {
        return contagio;
    }

    /**
     * Se establece el estado de salud de la persona.
     * 
     * @param contagio booleano que informa si tiene o no COVID-19.
     */
    public void setContagio(boolean contagio) {
        this.contagio = contagio;
    }
    
    /**
     * Se verifica, por medio del ID, si dos personas son iguales.
     * 
     * @param ID int con la información de la ID a comparar.
     * @return  resultado de la comprobación.
     */
    public boolean equals(int ID) {
        if (this.ID == ID) {
            return true;
        }
        return false;
    }   
}
