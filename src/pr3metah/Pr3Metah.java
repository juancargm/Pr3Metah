/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr3metah;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Juanca
 */
public class Pr3Metah {

    private static Pair cubreOrdenado[];
    private static int cubre[];
    private static int matriz[][];
    private static int x, y;

    /**
     * Funcion para leer n fichero
     *
     * @param fich Ruta del fichero a leer
     * @throws FicheroNoEncontrado Excepcion en caso de no encontrar el fichero
     * @throws java.io.FileNotFoundException
     */
    private static void leerFichero(String fich) throws FicheroNoEncontrado, IOException {
        if (!(new File(fich)).exists()) {
            throw new FicheroNoEncontrado("Fichero no encontrado \n");
        }
        File archivo;
        FileReader fr = null;
        BufferedReader br;
        try {
            archivo = new File(fich);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String texto;
            String[] datos;
            texto = br.readLine();
            datos = texto.split(" ");
            x = Integer.parseInt(datos[1]) + 1;
            y = Integer.parseInt(datos[2]) + 1;

            matriz = new int[x][y];
            cubre = new int[y];

            for (int i = 0; i < y; i++) {
                cubre[i] = 0;
            }
            for (int i = 1; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    matriz[i][j] = 0;
                }
            }
            matriz[0][0] = 0;
            int comisariasV = 1;
            while (y != comisariasV) {
                texto = br.readLine();
                datos = texto.split(" ");
                for (int i = 1; i < datos.length; i++) {
                    matriz[0][comisariasV] = Integer.parseInt(datos[i]);
                    ++comisariasV;
                }
            }
            int cont;
            for (int i = 1; i < x; i++) {
                texto = br.readLine();
                datos = texto.split(" ");
                cont = Integer.parseInt(datos[1]);
                while (cont != 0) {
                    texto = br.readLine();
                    datos = texto.split(" ");
                    for (int j = 1; j < datos.length; j++) {
                        matriz[i][Integer.parseInt(datos[j])] = 1;
                        ++cubre[Integer.parseInt(datos[j])];
                        --cont;
                    }
                }
            }
        } finally {
            if (null != fr) {
                    fr.close();
            }
        }
    }

    /**
     * Funcion para inicializar el vector de pair cubreOrdenado
     * que contiene cuantas zonas cubre cada comisaría
     *
     */
    private static void inicializo() {
        cubreOrdenado = new Pair[y - 1];
        for (int i = 0; i < y - 1; i++) {
            cubreOrdenado[i] = new Pair(i + 1, cubre[i + 1]);
        }
        MyQuickSort sorter = new MyQuickSort();
        sorter.sort(cubreOrdenado);
    }

    /**
     * @param args the command line arguments
     * @throws pr3metah.FicheroNoEncontrado
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws FicheroNoEncontrado, InterruptedException, IOException {
        String algoritmo[] = {"AGGfusion", "AGGhux", "AGEhux"};
        String ficheros[] = {"scpe1.txt", "scp41.txt", "scpd1.txt", "scpnrf1.txt", "scpa1.txt"};
        String optimos[] = {"5  ", "429", "60 ", "14 ", "253"};
        int n = 5;
        Genetico gen;
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; ++j) {
                leerFichero(ficheros[i]);
                inicializo();
                gen = new Genetico();
                gen.AGGHux(x, y, matriz, cubreOrdenado, optimos[i], algoritmo[1], " BL EN TODOS", 0);
                System.out.println();
                gen = new Genetico();
                gen.AGGHux(x, y, matriz, cubreOrdenado, optimos[i], algoritmo[1], " BL CON PROB 0.1", 0);
                System.out.println();
                gen = new Genetico();
                gen.AGGHux(x, y, matriz, cubreOrdenado, optimos[i], algoritmo[1], " BL EN LOS 0.1 N MEJORES", 0);
                System.out.println();
            }
        }
        time_end = System.currentTimeMillis();
        System.out.println("EL PROGRAMA HA TARDADO " + (float) (time_end - time_start) / 1000.0 + " SEGUNDOS");
    }

}
