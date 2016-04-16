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

(deftest get-surviving-cells-test
  (testing "All living cells should die from underpopulation"
    (let [living-cells [[1 2] [4 5] [6 8]]
          expected-next-generation []]
      (is (= (get-surviving-cells living-cells) expected-next-generation))))
  (testing "All cells should survive to the next generation"
    (let [living-cells [[1 2] [1 1] [2 1] [2 2]]
          expected-next-generation living-cells]
      (is (= (get-surviving-cells living-cells) expected-next-generation)))))

(deftest get-resurrectable-cells-test
  (testing "Should return all neighbors of provided cell since their is only one living cell"
    (let [living-cells [[1 2]]
          expected-resurrectable-cells [[2 2] [2 3] [1 1] [1 3] [0 3] [0 2] [2 1] [0 1]]]
      (is (= (get-resurrectable-cells living-cells) expected-resurrectable-cells))))
  (testing "Should return only dead neighbors of 3 cells"
    (let [living-cells [[1 2] [2 2] [2 1] [1 1]]
          expected-resurrectable-cells [[0 0] [1 0] [2 3] [3 3] [3 0] [1 3] [0 3] [0 2] [2 0] [3 1] [3 2] [0 1]]]
      (is (= (get-resurrectable-cells living-cells) expected-resurrectable-cells)))))

(deftest resurrect-cell-test
  (testing "Resurrect cell because it has 3 living neighbors"
    (let [dead-cell [2 2]
          living-cells [[1 2] [1 1] [2 1]]]
      (is (= (resurrect-cell? dead-cell living-cells) true))))
  (testing "Don't resurrect cell because it only has 2 living neighbors"
    (let [dead-cell [2 2]
          living-cells [[1 2] [1 1]]]
      (is (= (resurrect-cell? dead-cell living-cells) false)))))

(deftest get-resurrected-cells-test
  (testing "No cells should be resurrected"
    (let [living-cells [[1 2] [2 1]]
          expected-resurrected-cells []]
      (is (= (get-resurrected-cells living-cells) expected-resurrected-cells))))
  (testing "One cell should be resurrected because it has 3 neighbors"
    (let [living-cells [[1 2] [2 1] [1 1]]
          expected-resurrected-cells [[2 2]]]
      (is (= (get-resurrected-cells living-cells) expected-resurrected-cells)))))

(deftest get-next-board-state-test
  (testing "One cell should resurrect and one cell should survive, others die from underpopulation"
    (let [living-cells [[1 2] [1 1] [2 3]]
          board-state {:living-cells living-cells :ticks 0}
          expected-board-state {:living-cells [[1 2] [2 2]] :ticks 1}]
      (is (= (get-next-board-state board-state) expected-board-state))))
  (testing "Cells don't change"
    (let [living-cells [[1 2] [1 1] [2 1] [2 2]]
          board-state {:living-cells living-cells :ticks 0}
          expected-board-state {:living-cells living-cells :ticks 1}]
      (is (= (get-next-board-state board-state) expected-board-state)))))