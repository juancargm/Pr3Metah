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
    class MyQuickSort {

        private Pair array[];

        /**
         * Funcion para ordenar de mayor a menor un vector de pair en funcion del elemento cubre
         * @param inputArr Array de pair
         */
        void sort(Pair[] inputArr) {
            if (inputArr == null || inputArr.length == 0) {
                return;
            }
            this.array = inputArr;
            quickSort(0, inputArr.length - 1);
        }

        private void quickSort(int lowerIndex, int higherIndex) {
            int i = lowerIndex;
            int j = higherIndex;

            Pair pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];

            while (i <= j) {
                while (array[i].getCubre() > pivot.getCubre()) {
                    ++i;
                }
                while (array[j].getCubre() < pivot.getCubre()) {
                    --j;
                }
                if (i <= j) {
                    exchangeNumbers(i, j);
                    ++i;
                    --j;
                }
            }

            if (lowerIndex < j) {
                quickSort(lowerIndex, j);
            }
            if (i < higherIndex) {
                quickSort(i, higherIndex);
            }
        }

        private void exchangeNumbers(int i, int j) {
            Pair temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

    }