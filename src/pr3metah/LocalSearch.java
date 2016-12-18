/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr3metah;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Juan Carlos
 */
class LocalSearch {

    private int numIteraciones;
    private int solucionVecina[];

    /**
     * Calcula una solución vecina intentando mejorar el coste de la solución Greedy
     * @param solucion vector solucion
     * @param matriz matriz con los datos del problema
     * @param x Numero de territorios +1
     * @param y Numero de comisarias +1
     * @param pair vector de Pair para eliminar la s redundancias
     * @return Devuelve una solución vecina
     */
    int busquedaLocal(ArrayList<int[]> solucion, int costes[], int matriz[][], int x, int y, int num, Pair pair[]) {
        int anterior, costeVecina, costeActual, posicion;
        int solucionActual[] = solucion.get(num);
        costeActual = costes[num];
        numIteraciones = 0;
        Random aleatorio = new Random();
        //aleatorio.setSeed(semilla);
        ArrayList<Integer> comisarias;

        do {
            comisarias = calculaComisarias(solucionActual, y);
            do {
                posicion = Math.abs(aleatorio.nextInt() % comisarias.size());
                costeVecina = generaVecino(solucionActual, matriz, x, y, comisarias.get(posicion), pair);
                comisarias.remove(posicion);
            } while ((costeVecina >= costeActual) &&  !comisarias.isEmpty() && numIteraciones < 200);
            anterior = costeActual;
            if (costeVecina < costeActual) {
                solucionActual = solucionVecina.clone();
                solucion.set(num, solucionActual.clone());
                costeActual = costeVecina;
            }
        } while (costeVecina < anterior && numIteraciones < 200);
        costes[num] = costeActual;
        return numIteraciones;
    }


     /**
     * Calcula una solucion vecina para el entorno
     * @param solucionActual la solucion original (Antes de aplicar el operador vecino)
     * @param matriz matriz con los datos del problema
     * @param x Numero de territorios +1
     * @param y Numero de comisarias +1
     * @param pos comisaria que vamos a eliminar
     * @param pair vector de Pair para eliminar la s redundancias
     * @return Devuelve el coste del vecino generado
     */
    private int generaVecino(int solucionActual[], int matriz[][], int x, int y, int pos, Pair pair[]) {

        solucionVecina = solucionActual;
        ++numIteraciones;
        solucionVecina[pos] = 0;

        reparaSol(x, y, matriz, solucionVecina);
        eliminaRedundancias(x, y, solucionVecina, pair, matriz);
        return objetivo(solucionActual, y, matriz);
    }

    /**
     * Repara un cromosoma para que sea solucion
     *
     * @param x numero de filas de la matriz (zonas)
     * @param y numero de columnas de la matriz (comisarias)
     * @param matriz datos de las comisarías y las zonas que cubren asi como su coste
     * @param sol vector solucion que se esta reparando
     */
    void reparaSol(int x, int y, int matriz[][], int sol[]) {
        while (true) {
            int cubiertos[] = new int[x];
            for (int i = 1; i < x; i++) {
                cubiertos[i] = 0;
            }
            for (int c = 1; c < y; c++) {
                if (sol[c] == 1) {
                    for (int f = 1; f < x; f++) {
                        if (matriz[f][c] == 1) {
                            cubiertos[f] = 1;
                        }
                    }
                }
            }
            int cubre[] = new int[y];
            for (int i = 0; i < y; i++) {
                cubre[i] = 0;
            }
            for (int f = 1; f < x; f++) {
                if (cubiertos[f] == 0) {
                    for (int c = 1; c < y; c++) {
                        if (matriz[f][c] == 1) {
                            ++cubre[c];
                        }
                    }
                }
            }
            float mayor = (float) cubre[1] / matriz[0][1];
            int pos = 1;
            for (int i = 2; i < y; i++) {
                if (((float) cubre[i] / matriz[0][i]) > mayor) {
                    mayor = (float) cubre[i] / matriz[0][i];
                    pos = i;
                }
            }
            if (mayor == 0) {
                return;
            }
            sol[pos] = 1;
            //coste += matriz[0][pos];
        }
    }

    /**
     * Elimina las redundancias de un cromosoma
     *
     * @param x numero de filas de la matriz (zonas)
     * @param y numero de columnas de la matriz (comisarias)
     * @param solucion el cromosoma al que le eliminamos las redundancias
     * @param cubreOrdenado vector con el numero de zonas que cubre cada
     * comisaria
     * @param matriz datos de las comisarías y las zonas que cubren asi como su coste
     */
    void eliminaRedundancias(int x, int y, int solucion[], Pair cubreOrdenado[], int matriz[][]) {
        int quito;
        int i;
        boolean columnaRedundante, sustituible;
        for (int z = 0; z < y - 1; z++) {
            if (solucion[cubreOrdenado[z].getLugar()] == 1) {
                columnaRedundante = true;
                quito = cubreOrdenado[z].getLugar();
                for (i = 1; i < x; i++) {
                    if (matriz[i][quito] == 1) {
                        sustituible = false;
                        for (int j = 1; j < y; j++) {
                            if (matriz[i][j] == 1 && solucion[j] == 1 && quito != j) {
                                sustituible = true;
                            }
                        }
                        if (!sustituible) {
                            columnaRedundante = false;
                        }
                    }
                }
                if (columnaRedundante) {
                    solucion[quito] = 0;
                }
            }
        }
    }


    /**
     * Calcula el coste de un vector solucion
     * @param solucionVecina vector solucion
     * @param tam tamaño del vector solucion
     * @param matriz matriz con los datos del problema
     * @return Devuelve el coste de una solucion
     */
    private int objetivo(int solucionVecina[], int tam, int matriz[][]) {
        int suma = 0;
        for (int i = 1; i < tam; ++i) {
            suma += solucionVecina[i] * matriz[0][i];
        }
        return suma;
    }

    /**
     * @param solucion vector solución  
     * @param y tamaño del vector solución
     * @return Devuelve un ArrayList con todas las comisarias que tiene una solución
     */
    private ArrayList<Integer> calculaComisarias(int solucion[], int y){
        ArrayList<Integer> array;
        array = new ArrayList<>();
        for(int i = 1; i < y; ++i){
            if(solucion[i] == 1){
                array.add(i);
            }
        }
        return array;
    }
}
