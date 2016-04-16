(ns conway.core-test
  (:require [clojure.test :refer :all]
            [conway.core :refer :all]))

(deftest get-neighbor-coordinates-test
  (testing "Gets all eight neighbors for any two points"
    (let [test-cell [1 1]
          expected-neighbors [[0 1] [2 1] [0 2] [1 2] [2 2] [0 0] [1 0] [2 0]]]
      (is (= (get-neighbor-coordinates test-cell) expected-neighbors)))))

(deftest get-living-neighbors-test
  (testing "Gets a vector of all the living neighbors of a cell"
    (let [test-cell [1 1]
          expected-living-neighbors [[2 2] [1 2]]
          living-cells [[3 4] [5 6] [1 2] [2 2]]]
      (is (= (get-living-neighbors test-cell living-cells) expected-living-neighbors)))))

(deftest cell-is-alive?-test
  (testing "Cell is alive because it has 3 living neighbors"
    (let [living-cell [1 1]
          living-cells [[1 2] [2 2] [2 1]]
          is-alive? true]
      (is (= (cell-is-alive? living-cell living-cells) is-alive?))))
  (testing "Cell is dead because it has 1 living neighbor"
    (let [living-cell [1 1]
          living-cells [[1 2] [3 3] [4 4]]
          is-alive? false]
      (is (= (cell-is-alive? living-cell living-cells) is-alive?))))
  (testing "Cell is dead because it has 4 living neighbors"
    (let [living-cell [1 1]
          living-cells [[1 2] [2 2] [2 1] [1 0]]
          is-alive? false]
      (is (= (cell-is-alive? living-cell living-cells) is-alive?)))))

(deftest get-next-board-state-test
  (testing "All living cells should die from underpopulation"
    (let [living-cells [[1 2] [4 5] [6 8]]
          expected-next-generation []]
      (is (= (get-next-board-state living-cells) expected-next-generation))))
  (testing "All cells should survive to the next generation"
    (let [living-cells [[1 2] [1 1] [2 1] [2 2]]
          expected-next-generation living-cells]
      (is (= (get-next-board-state living-cells) expected-next-generation)))))