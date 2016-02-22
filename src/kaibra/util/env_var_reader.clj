(ns kaibra.util.env_var_reader
  (:require [environ.core :as env]))

(defn read-env-var
  ([[env-var-key fallback]]
   (or
     (get env/env env-var-key fallback)
     "")))
