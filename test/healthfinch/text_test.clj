(ns healthfinch.text-test
  (:require [healthfinch.text :refer :all]
            [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.string :as str]))

(deftest tokenization
  (testing "correct tokenization"
    (is (= ["quick" "brown" "fox"]
           (tokenize "quick brown fox"))
        "basic tokenization failed")
    (is (= ["jackdaws" "love" "my" "giant" "sphinx"]
           (tokenize "Jackdaws love, my giant sphinx."))
        "punctuation confuses tokenizer")
    (is (= ["grumpy" "wizards" "make" "toxic" "brew" "evil" "queen" "jack"]
           (tokenize "Grumpy wizards make toxic brew for the evil Queen and Jack."))
        "tokenizer doesn't exclude stop words")
    (is (= ["wine" "dark" "sea"]
           (tokenize "Wine-dark sea"))
        "tokenizer doesn't handle hypenation")))

(def real-words #{"quick"
                  "brown"
                  "fox"
                  "grumpy"
                  "wizards"
                  "jackdaws"
                  "wine-dark"
                  "giant"
                  "sphinx"
                  "toxic"
                  "brew"
                  "sea"
                  "evil"
                  "queen"})

(def stop-word-generator (gen/elements stop-words))
(def real-word-generator (gen/elements real-words))
(def sentence-generator (gen/fmap #(str (str/capitalize %) (rand-nth ["." "?" "!"]))
                                  (gen/fmap (partial str/join " ") (gen/vector real-word-generator 1 10))))
(def paragraph-generator (gen/fmap (partial str/join " ") (gen/vector sentence-generator 1 10)))

(defn explode-word-count [[word occurrences]]
  (repeat occurrences word))

(defspec word-counting
  (prop/for-all
   [para paragraph-generator]
   (is (= (sort (tokenize para))
          (sort (mapcat explode-word-count (count-words para))))
       "The words in the count should match the tokenized words.")))
