(ns landing.core
  (:require [clojure.string :as str]
            [reagent.dom.client :as rdom]
            [reagent.core :as r]))

(def blog-url "https://blog.carldan.com")
(def cv-url "https://data.carldan.com/cv.pdf")
(def github-url "https://github.com/arch-m")
(def projects-path "/projects")
(def home-path "/")

;; Add more project cards by appending new slugs, for example {:slug "d"}.
(def project-sites [{:slug "a"} {:slug "b"} {:slug "c"}])

(defn project-url [slug] (str "https://project" slug ".carldan.com"))

(defn project-label [slug] (str "Project " (str/upper-case slug)))

(def content
  {:en
     {:name "Carlos Delgado",
      :nav {:home "Home",
            :expertise "Expertise",
            :experience "Experience",
            :projects "Projects",
            :contact "Contact",
            :blog "Blog",
            :cv "CV",
            :github "Github"},
      :theme {:light "Light",
              :dark "Dark",
              :to-light "Switch to light mode",
              :to-dark "Switch to dark mode"},
      :language {:switch "Switch language"},
      :hero
        {:title "Software Engineer focused on high-performance systems.",
         :copy
           "Software engineer with over five years of experience in fintech and e-commerce. Specialized in microservices, architecture migrations, infrastructure, and optimization of business-critical services.",
         :card [{:label "Location", :value "Mexico City, Mexico | Remote"}
                {:label "Role", :value "Software Engineer"}
                {:label "Stack",
                 :value "Python, Node, Golang, Kafka, Redis, PostgreSQL, React"}
                {:label "Languages", :value "Spanish (Native) | English (C1)"}],
         :availability "Available for high-impact projects."},
      :expertise-title "Expertise",
      :expertise-items
        [{:title "Architecture",
          :copy
            "I design and evolve microservices, integrations, and resilient APIs for financial and e-commerce systems."}
         {:title "Fintech",
          :copy
            "Hands-on experience in payments, KYC/AML, fraud detection, and real-time transaction processing."}
         {:title "Delivery",
          :copy
            "I implement CI/CD pipelines, observability, and agent-assisted workflows to speed up quality delivery."}],
      :experience-title "Experience",
      :experience-highlights
        [{:title "Freelance Engineer | 2025-Present",
          :copy
            "Built Go applications for multi-layer security camera software management, orchestrated through event-driven architecture.",
          :stack "Go, Event-Driven Architecture, Agent-Assisted Engineering"}
         {:title "Backend Engineer | Finvero (2022-2025)",
          :copy
            "Migrated services from a monolith, implemented Kafka for real-time transactions, and distributed Redis cache for payment services.",
          :stack "Microservices, Kafka, Redis, MLOps"}
         {:title "Fullstack Developer | Evangelium (2021-2022)",
          :copy
            "Built an end-to-end platform for credit card application management with banking integrations and secure API workflows.",
          :stack "Node.js, REST APIs, React, Verification Workflows"}
         {:title "Backend Developer | Belopet (2020-2021)",
          :copy
            "Developed core e-commerce services, real-time shipment monitoring, and optimized financial PostgreSQL queries.",
          :stack "PostgreSQL, Docker, CI/CD, Real-Time Monitoring"}],
      :projects-page {:title "Projects",
                      :copy
                        "Project hub with direct links to deployed subdomains.",
                      :cta "Open"},
      :contact
        {:title "Let's Talk",
         :copy
           "If you need strong technical execution with architecture depth and business impact, we can align scope, timeline, and delivery strategy."},
      :footer "2026 | Carlos Delgado | Software Engineer"},
   :es
     {:name "Carlos Delgado",
      :nav {:home "Inicio",
            :expertise "Especialidad",
            :experience "Trayectoria",
            :projects "Proyectos",
            :contact "Contacto",
            :blog "Blog",
            :cv "CV",
            :github "Github"},
      :theme {:light "Claro",
              :dark "Oscuro",
              :to-light "Cambiar a modo claro",
              :to-dark "Cambiar a modo oscuro"},
      :language {:switch "Cambiar idioma"},
      :hero
        {:title
           "Ingeniero de Software enfocado en sistemas de alto rendimiento.",
         :copy
           "Ingeniero de software con mas de cinco anos de experiencia en fintech y e-commerce. Especializado en microservicios, migraciones de arquitectura, infraestructura y optimizacion de servicios criticos orientados a negocio.",
         :card [{:label "Ubicacion",
                 :value "Ciudad de México , México | Remoto"}
                {:label "Rol", :value "Ingeniero de Software"}
                {:label "Stack",
                 :value "Python, Node, Golang, Kafka, Redis, PostgreSQL, React"}
                {:label "Idiomas", :value "Español (Nativo) | Ingles (C1)"}],
         :availability "Disponible para proyectos de alto impacto."},
      :expertise-title "Especialidad",
      :expertise-items
        [{:title "Arquitectura",
          :copy
            "Diseno y evoluciono microservicios, integraciones y APIs resilientes para sistemas financieros y e-commerce."}
         {:title "Fintech",
          :copy
            "Experiencia practica en pagos, KYC/AML, deteccion de fraude y procesamiento transaccional en tiempo real."}
         {:title "Entrega",
          :copy
            "Implemento pipelines CI/CD, observabilidad y workflows asistidos por agentes para acelerar entrega con calidad."}],
      :experience-title "Trayectoria",
      :experience-highlights
        [{:title "Freelance Engineer | 2025-Presente",
          :copy
            "Desarrollo de aplicaciones en Go para gestion de software de videovigilancia en multiples capas, orquestadas con arquitectura orientada a eventos.",
          :stack "Go, Event-Driven Architecture, Agent-Assisted Engineering"}
         {:title "Backend Engineer | Finvero (2022-2025)",
          :copy
            "Migracion de servicios desde monolito, implementacion de Kafka para transacciones en tiempo real y cache distribuida con Redis para servicios de pago.",
          :stack "Microservices, Kafka, Redis, MLOps"}
         {:title "Fullstack Developer | Evangelium (2021-2022)",
          :copy
            "Construccion end-to-end de una plataforma de gestion de solicitudes de tarjeta de credito con integraciones bancarias y APIs seguras.",
          :stack "Node.js, REST APIs, React, Verification Workflows"}
         {:title "Backend Developer | Belopet (2020-2021)",
          :copy
            "Servicios core para e-commerce, monitoreo en tiempo real de envios y optimizacion de consultas financieras en PostgreSQL.",
          :stack "PostgreSQL, Docker, CI/CD, Real-Time Monitoring"}],
      :projects-page
        {:title "Proyectos",
         :copy
           "Hub de proyectos con enlaces directos a subdominios desplegados.",
         :cta "Abrir"},
      :contact
        {:title "Conversemos",
         :copy
           "Si necesitas un perfil tecnico con foco en arquitectura, ejecucion y resultados de negocio, podemos revisar alcance, tiempos y estrategia."},
      :footer "2026 | Carlos Delgado | Software Engineer"}})

(defonce ui-state* (r/atom {:lang :en, :theme :light}))
(def theme-override-key "landing-theme-override")
(def lang-key "landing-lang")
(defonce system-theme-query* (atom nil))
(defonce system-theme-handler* (atom nil))

(defn safe-local-get
  [key]
  (try (.getItem js/localStorage key) (catch :default _ nil)))

(defn safe-local-set!
  [key value]
  (try (.setItem js/localStorage key value) (catch :default _ nil)))

(defn normalize-lang [value] (if (= value "es") :es :en))

(defn parse-theme
  [value]
  (cond (= value "dark") :dark
        (= value "light") :light
        :else nil))

(defn system-theme
  []
  (if-let [match-media (.-matchMedia js/window)]
    (if (.-matches (match-media "(prefers-color-scheme: dark)")) :dark :light)
    :light))

(defn apply-theme!
  [theme]
  (let [root (.-documentElement js/document)
        theme-value (name theme)]
    (.setAttribute root "data-theme" theme-value)))

(defn apply-lang!
  [lang]
  (let [root (.-documentElement js/document)
        lang-value (name lang)]
    (set! (.-lang root) lang-value)
    (safe-local-set! lang-key lang-value)))

(defn remove-system-theme-listener!
  []
  (when (and @system-theme-query* @system-theme-handler*)
    (let [query @system-theme-query*
          handler @system-theme-handler*]
      (if (some? (.-removeEventListener query))
        (.removeEventListener query "change" handler)
        (.removeListener query handler)))
    (reset! system-theme-query* nil)
    (reset! system-theme-handler* nil)))

(defn install-system-theme-listener!
  []
  (remove-system-theme-listener!)
  (when-let [match-media (.-matchMedia js/window)]
    (let [query (match-media "(prefers-color-scheme: dark)")
          handler (fn [_]
                    (when-not (safe-local-get theme-override-key)
                      (let [next-theme (system-theme)]
                        (swap! ui-state* assoc :theme next-theme)
                        (apply-theme! next-theme))))]
      (if (some? (.-addEventListener query))
        (.addEventListener query "change" handler)
        (.addListener query handler))
      (reset! system-theme-query* query)
      (reset! system-theme-handler* handler))))

(defn toggle-theme!
  []
  (let [next-theme (if (= (:theme @ui-state*) :light) :dark :light)]
    (swap! ui-state* assoc :theme next-theme)
    (apply-theme! next-theme)
    (safe-local-set! theme-override-key (name next-theme))))

(defn toggle-lang!
  []
  (let [next-lang (if (= (:lang @ui-state*) :en) :es :en)]
    (swap! ui-state* assoc :lang next-lang)
    (apply-lang! next-lang)))

(defn current-content [lang] (or (get content lang) (get content :en)))

(defn current-page
  []
  (let [path (.-pathname (.-location js/window))]
    (if (re-matches #"/projects/?" path) :projects :home)))

(defn moon-icon
  []
  [:svg.icon {:viewBox "0 0 24 24", :aria-hidden "true"}
   [:path {:d "M20 14.2A8.6 8.6 0 1 1 9.8 4a7.2 7.2 0 0 0 10.2 10.2z"}]])

(defn sun-icon
  []
  [:svg.icon {:viewBox "0 0 24 24", :aria-hidden "true"}
   [:circle {:cx "12", :cy "12", :r "4"}]
   [:path
    {:d
       "M12 2v2.2M12 19.8V22M4.9 4.9l1.6 1.6M17.5 17.5l1.6 1.6M2 12h2.2M19.8 12H22M4.9 19.1l1.6-1.6M17.5 6.5l1.6-1.6"}]])

(defn globe-icon
  []
  [:svg.icon {:viewBox "0 0 24 24", :aria-hidden "true"}
   [:circle {:cx "12", :cy "12", :r "9"}]
   [:path {:d "M3 12h18M12 3a15 15 0 0 1 0 18M12 3a15 15 0 0 0 0 18"}]])

(defn nav-link
  [href text & {:keys [external]}]
  [:a
   (cond-> {:href href}
     external (assoc :target
                "_blank" :rel
                "noopener noreferrer")) text])

(defn topbar
  [lang theme page t]
  [:header.topbar [:div.brand (:name t)]
   [:div.top-nav
    [:nav.top-links
     (if (= page :home)
       [:<> [nav-link "#expertise" (get-in t [:nav :expertise])]
        [nav-link "#experience" (get-in t [:nav :experience])]
        ;; [nav-link projects-path (get-in t [:nav :projects])]
        [nav-link "#contact" (get-in t [:nav :contact])]
        ;; [nav-link blog-url (get-in t [:nav :blog]) :external true]
        [nav-link github-url (get-in t [:nav :github]) :external true]
        [nav-link cv-url (get-in t [:nav :cv]) :external true]]
       [:<> [nav-link home-path (get-in t [:nav :home])]
        ;; [nav-link projects-path (get-in t [:nav :projects])]
        ;; [nav-link blog-url (get-in t [:nav :blog]) :external true]
        [nav-link github-url (get-in t [:nav :github]) :external true]
        [nav-link cv-url (get-in t [:nav :cv]) :external true]])]
    [:div.top-actions
     [:button.icon-btn
      {:type "button",
       :on-click toggle-theme!,
       :aria-label (if (= theme :light)
                     (get-in t [:theme :to-dark])
                     (get-in t [:theme :to-light])),
       :title (if (= theme :light)
                (get-in t [:theme :to-dark])
                (get-in t [:theme :to-light]))}
      (if (= theme :light) [moon-icon] [sun-icon])
      [:span
       (if (= theme :light)
         (get-in t [:theme :dark])
         (get-in t [:theme :light]))]]
     [:button.icon-btn
      {:type "button",
       :on-click toggle-lang!,
       :aria-label (get-in t [:language :switch]),
       :title (get-in t [:language :switch])} [globe-icon]
      [:span (str/upper-case (name lang))]]]]])

(defn hero
  [t]
  [:section.hero
   [:div [:h1 (get-in t [:hero :title])] [:p (get-in t [:hero :copy])]]
   [:aside.hero-card
    [:dl
     (for [{:keys [label value]} (get-in t [:hero :card])]
       ^{:key label} [:<> [:dt label] [:dd value]])]
    [:small (get-in t [:hero :availability])]]])

(defn expertise
  [t]
  [:section#expertise [:h2.section-title (:expertise-title t)]
   [:div.expertise-grid
    (for [{:keys [title copy]} (:expertise-items t)]
      ^{:key title} [:article.expertise-item [:h3 title] [:p copy]])]])

(defn experience-list
  [t]
  [:section#experience.projects [:h2.section-title (:experience-title t)]
   (for [{:keys [title copy stack]} (:experience-highlights t)]
     ^{:key title}
     [:article.project-row
      [:div [:h3.project-title title] [:p.project-copy copy]]
      [:p.project-stack stack]])])

(defn contact
  [t]
  [:section#contact.contact [:h2 (get-in t [:contact :title])]
   [:p (get-in t [:contact :copy])]
   [:p [:a {:href "mailto:contact@carldan.com"} "contact@carldan.com"] " | "
    [:a
     {:href "https://www.carldan.com",
      :target "_blank",
      :rel "noopener noreferrer"} "carldan.com"] " | "
    [:a
     {:href "https://github.com/arch-m",
      :target "_blank",
      :rel "noopener noreferrer"} "github.com/arch-m"]]])

(defn projects-hub
  [t]
  [:section.projects-hub [:h1.section-title (get-in t [:projects-page :title])]
   [:p.projects-intro (get-in t [:projects-page :copy])]
   [:div.project-tiles
    (for [{:keys [slug]} project-sites
          :let [site-url (project-url slug)
                domain (str "project" slug ".carldan.com")]]
      ^{:key slug}
      [:a.project-tile
       {:href site-url, :target "_blank", :rel "noopener noreferrer"}
       [:h3 (project-label slug)] [:p domain]
       [:span (get-in t [:projects-page :cta])]])]])

(defn home-page
  [lang theme t]
  [:main.page [topbar lang theme :home t] [hero t] [expertise t]
   [experience-list t] [contact t] [:footer.footer (:footer t)]])

(defn projects-page
  [lang theme t]
  [:main.page [topbar lang theme :projects t] [projects-hub t]
   [:footer.footer (:footer t)]])

(defn app
  []
  (let [{:keys [lang theme]} @ui-state*
        t (current-content lang)]
    (if (= (current-page) :projects)
      [projects-page lang theme t]
      [home-page lang theme t])))

(defonce root* (atom nil))

(defn init
  []
  (let [saved-lang (normalize-lang (safe-local-get lang-key))
        saved-theme (or (parse-theme (safe-local-get theme-override-key))
                        (system-theme))
        el (.getElementById js/document "app")]
    (reset! ui-state* {:lang saved-lang, :theme saved-theme})
    (apply-lang! saved-lang)
    (apply-theme! saved-theme)
    (install-system-theme-listener!)
    (when-not @root* (reset! root* (rdom/create-root el)))
    (rdom/render @root* [app])))
