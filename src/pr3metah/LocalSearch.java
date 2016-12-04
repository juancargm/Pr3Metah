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
     * @param semilla semilla para aleatorizar los vecinos generados
     * @param pair vector de Pair para eliminar la s redundancias
     * @return Devuelve una solución vecina
     */
    int[] busquedaLocal(int solucion[], int matriz[][], int x, int y, Pair pair[], int semilla) {
        int anterior, costeVecina, costeActual, posicion;
        int solucionActual[] = solucion; // Inicializacion del Greedy
        costeActual = objetivo(solucionActual, y, matriz);
        numIteraciones = 1; //Empieza en uno ya que he llamado ya una vez a la funcion objetivo
        Random aleatorio = new Random();
        aleatorio.setSeed(semilla);
        ArrayList<Integer> comisarias;

        do {
            comisarias = calculaComisarias(solucionActual, y);
            do {
                posicion = Math.abs(aleatorio.nextInt() % comisarias.size());
                costeVecina = generaVecino(solucionActual, matriz, x, y, costeActual, comisarias.get(posicion), pair);
                comisarias.remove(posicion);
            } while ((costeVecina >= costeActual) &&  !comisarias.isEmpty() && numIteraciones < 10000); 
            anterior = costeActual;
            if (costeVecina < costeActual) {
                solucionActual = solucionVecina.clone();
                costeActual = costeVecina;
            }
        } while (costeVecina < anterior && numIteraciones < 10000);


        return solucionActual;
    }


     /**
     * Calcula una solucion vecina para el entorno
     * @param solucionActual la solucion original (Antes de aplicar el operador vecino)
     * @param matriz matriz con los datos del problema
     * @param x Numero de territorios +1
     * @param y Numero de comisarias +1
     * @param costeActual el coste actual de la solucion
     * @param pos comisaria que vamos a eliminar
     * @param pair vector de Pair para eliminar la s redundancias
     * @return Devuelve el coste del vecino generado
     */
    private int generaVecino(int solucionActual[], int matriz[][], int x, int y, int costeActual, int pos, Pair pair[]) {

        int costeVecina;
        solucionVecina = solucionActual.clone();
        ++numIteraciones; //1 factorizacion, por lo que se incrementa el contador
        solucionVecina[pos] = 0;
        costeVecina = costeActual - matriz[0][pos];
            
       

        //Se genera un vector con todos los candidatos que cubren alguna zona de las que me quedan por cubrir al eliminar esa ( sin incluirla )
        int vecino[] = new int[y];
        int zonas[] = new int[x];
        int zonasPendientes = 0;

        for (int i = 1; i < y; ++i) {
            vecino[i] = 0;
            if (i < x) {
                zonas[i] = 0;
            }
        }

        //Se rellena el vector de zonas, las posiciones que quedan con 0, son las que faltan por cubrir
        for (int k = 1; k < y; ++k) {
            for (int j = 1; j < x; ++j) {
                if (matriz[j][k] == 1 && zonas[j] == 0 && solucionVecina[k] == 1) {
                    zonas[j] = 1;
                    ++zonasPendientes;
                }
            }
        }
        zonasPendientes = x - zonasPendientes - 1;
        for (int k = 1; (k < y && zonasPendientes > 0); ++k) {
            for (int j = 1; j < x; j++) {
                if (k != pos) {
                    if ((matriz[j][k] == 1) && (zonas[j] == 0)) { //La zona esta sin cubrir
                        vecino[k] = 1;
                        zonas[j] = 1;
                        --zonasPendientes;
                    }
                }
            }
        }

        for (int i = 1; i < y; ++i) {
            if (solucionVecina[i] == 0 && vecino[i] == 1) {
                if (i != pos) {
                    solucionVecina[i] = 1;
                    costeVecina += matriz[0][i];
                }
            }
        }
        costeVecina = eliminaRedundancias(x, y, matriz, solucionVecina, pair, costeVecina);
        return costeVecina;
    }

     /**
     * Elimina las columnas redundantes de una solucioón
     * @param y Numero de territorios +1
     * @param x Numero de comisarias +1
     * @param matriz Matriz con la informacion del problema
     * @param cubreOrdenado vector de Pair para eliminar las redundancias
     * @param costeVecina el coste del vecino
     * @return el coste factorizado de una solución vecina
     */
     int eliminaRedundancias(int x, int y, int matriz[][], int solucion[], Pair cubreOrdenado[], int costeVecina) {
        int factorizacion = costeVecina;
        MyQuickSort sorter = new MyQuickSort();
        sorter.sort(cubreOrdenado);
        int quito;
        int i;
        boolean columnaRedundante, sustituible;
        for (int z = 0; z < y - 1; ++z) {
            if (solucion[cubreOrdenado[z].getLugar()] == 1) {
                columnaRedundante = true;
                quito = cubreOrdenado[z].getLugar();
                for (i = 1; i < x; ++i) {
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
                    factorizacion = factorizacion - matriz[0][quito];
                }
            }
        }
        return factorizacion;
    }

    /**
     * @param solucionVecina vector solucion
     * @param tam el tamaño del vector solucion
     * @return Devuelve el número de conjuntos que tiene una solucion
     */
    public static int calculaIteraciones(int solucionVecina[], int tam) {
        int cont = 0;
        for (int i = 1; i < tam; ++i) {
            if (solucionVecina[i] == 1) {
                ++cont;
            }
        }
        return cont;
    }

    /**
     * Calcula el coste de un vector solucion
     * @param solucionVecina vector solucion
     * @param tam tamaño del vector solucion
     * @param matriz matriz con los datos del problema
     * @return Devuelve el coste de una solucion
     */
    private static int objetivo(int solucionVecina[], int tam, int matriz[][]) {
        int suma = 0;
        for (int i = 1; i < tam; ++i) {
            suma += solucionVecina[i] * matriz[0][i];
        }
        return suma;
    }

    /**
     * @return Devuelve el número de iteraciones dadas
     */
    public int getIteraciones() {
        return numIteraciones;
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
