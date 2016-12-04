/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr3metah;

/**
 *
 * @author Juan Carlos
 */
class Pair {

    private int lugar;
    private int cubre;

    /**
     * Constructor parametrizado
     *
     * @param lugar
     * @param cubre
     */
    Pair(int lugar, int cubre) {
        super();
        this.lugar = lugar;
        this.cubre = cubre;
    }

    /**
     * @return Numero de la comisaria
     */
    int getLugar() {
        return lugar;
    }

    /**
     * @return Numero de de elementos que cubre la comisaria
     */
    int getCubre() {
        return cubre;
    }

    /**
     * Aumenta el numero de elementos que cubre una comisaria
     */
    void aumentaCubre() {
        ++cubre;
    }
}
