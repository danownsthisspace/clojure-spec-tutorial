(ns spec-tutorial.core
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))


(s/valid? string? "a string")
(s/valid? string? 1)

(def short-string? (s/and string? #(< (count %) 5)))

(s/valid? short-string? "and")
(s/valid? short-string? "on the code again")

(def col-of-short-string (s/coll-of short-string?))

(s/valid? col-of-short-string ["a" "b" "c"])
(s/valid? col-of-short-string ["a" "b" "c" 1])
(s/valid? col-of-short-string ["a" "b" "c" "on the code again"])

(def short-string-or-number (s/or :short-string short-string? :is-number number?))

(s/explain short-string-or-number 10)

(def f1-car {:team "Mercedez"
             :driver "Lewis Hamilton"
             :starting-pos 1
             :positions [18 3 10 6]})

(s/def ::f1-car-spec
  (s/keys :req-un [::team ::driver ::starting-pos ::positions]))

(s/conform ::f1-car-spec f1-car)

(s/def ::team string?)
(s/def ::driver string?)
(s/def ::starting-pos int?)
(s/def ::positions (s/coll-of int?))

(defn scored? [last-pos min-pos]
  (< last-pos min-pos))

(scored? (first (:positions f1-car)) 10)

(s/fdef scored?
  :args (s/cat :last-pos int? :min-pos int?)
  :ret boolean?
  :fn (fn [{:keys [args ret]}]
        (let [last-pos (:last-pos args)]
          (< last-pos 6))))

(stest/instrument 'spec-tutorial.core/scored?)

(stest/check 'spec-tutorial.core/scored?)