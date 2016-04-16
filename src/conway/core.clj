(ns conway.core
  (:gen-class)
  (:require [clojure.set]))

; https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

(defn get-neighbor-coordinates
  "Get neighbors of the cell provided; a neighbor can be next to or diagonal to a cell"
  [[x y]]
  [[(- x 1) y]
   [(+ x 1) y]
   [(- x 1) (+ y 1)]
   [x       (+ y 1)]
   [(+ x 1) (+ y 1)]
   [(- x 1) (- y 1)]
   [x       (- y 1)]
   [(+ x 1) (- y 1)]])

(defn get-living-neighbors
  "Get vector of living neighbors from provided cell"
  [cell living-cells]
  (-> cell
      get-neighbor-coordinates
      set
      (clojure.set/intersection (set living-cells))
      vec))

(defn cell-is-alive?
  "Test if living cell survives to the next generation"
  [living-cell living-cells]
  (let [total-living-neighbors (count (get-living-neighbors living-cell living-cells))]
    (and (> total-living-neighbors 1) (< total-living-neighbors 4))))

(defn get-surviving-cells
  "Get living cells that will survive to the next generation"
  [living-cells]
  (loop [[living-cell & remaining-living-cells] living-cells
         next-generation-living-cells []]
    (if (empty? living-cell)
      next-generation-living-cells
      (if (cell-is-alive? living-cell living-cells)
        (recur remaining-living-cells
               (conj next-generation-living-cells living-cell))
        (recur remaining-living-cells next-generation-living-cells)))))

(defn get-resurrectable-cells
  "Takes living cells and returns all dead neighboring cells"
  [living-cells]
  (let [reducefn #(concat %1 (get-neighbor-coordinates %2))
        living-cells-neighbors (distinct (reduce reducefn [] living-cells))]
    (vec (clojure.set/difference (set living-cells-neighbors) (set living-cells)))))

(defn resurrect-cell?
  "Test if a cell should come back from the dead"
  [dead-cell living-cells]
  (let [total-living-neghbors (count (get-living-neighbors dead-cell living-cells))]
    (= total-living-neghbors 3)))

(defn get-resurrected-cells
  "Get cells that come back from the dead"
  [living-cells]
  (let [resurrectable-cells (get-resurrectable-cells living-cells)]
    (loop [[resurrectable-cell & remaining-resurrectable-cells] resurrectable-cells
           resurrected-cells []]
      (if (empty? resurrectable-cell)
        resurrected-cells
        (if (resurrect-cell? resurrectable-cell living-cells)
          (recur remaining-resurrectable-cells
                 (conj resurrected-cells resurrectable-cell))
          (recur remaining-resurrectable-cells resurrected-cells))))))

(defn get-next-board-state
  "Take the current board state and return the next"
  [current-board-state]
  (let [living-cells (:living-cells current-board-state)
        ticks (:ticks current-board-state)
        surviving-cells (get-surviving-cells living-cells)
        resurrected-cells (get-resurrected-cells living-cells)
        next-generation (concat surviving-cells resurrected-cells)
        next-tick (inc ticks)]
    {:living-cells next-generation :ticks next-tick}))

(defn start-simulation
  "Start the simulation"
  [initial-state allowed-ticks]
  (loop [current-board-state initial-state]
    (if (= (:ticks current-board-state) allowed-ticks)
      current-board-state
      (recur (get-next-board-state current-board-state)))))

(def example-state {:living-cells [[1 2] [2 2] [2 1]] :ticks 0})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (start-simulation example-state 100))
