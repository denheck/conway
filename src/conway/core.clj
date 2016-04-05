(ns conway.core
  (:gen-class))

; https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
; TODO still need cell resurrection logic

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
    (and (> 1 total-living-neighbors) (< 4 total-living-neighbors))))

(defn get-next-board-state
  "Take the current board state and return the next"
  [living-cells]
  (loop [[living-cell & remaining-living-cells] living-cells
         next-generation-living-cells []]
    (if (empty? remaining-living-cells)
      next-generation-living-cells
      (if (cell-is-alive? living-cell living-cells)
        (recur remaining-living-cells
               (conj next-generation-living-cells living-cell))
        (recur remaining-living-cells next-generation-living-cells)))))

(defn start
  "Start the simulation"
  [initial-state]
  (loop [ticks 0
         living-cells initial-state]
    (recur (get-next-board-state living-cells) (inc ticks))))

(defn example-board [] [[1 10] [2 9] [3 8]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (start ))
