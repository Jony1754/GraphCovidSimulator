/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

/**
 *
 * @author Enrique Niebles
 */
public class Persona {
    private final boolean MASCARILLA;    
    private static int contID = 0;
    private final int ID;

    public Persona(boolean mascarilla) {
        this.MASCARILLA = mascarilla;
        this.ID = ++contID;
    }

    public boolean getMascarilla() {
        return MASCARILLA;
    }
    
    public int getID() {
        return ID;
    }

    public boolean equals(int ID) {
        if (this.ID == ID) {
            return true;
        }
        return false;
    }
    
    
}
