<<<<<<< HEAD
<h1 align="center">NutriApp — Frontend</h1>


NutriApp es una aplicación web moderna enfocada en la gestión de alimentación diaria, visualización de información nutricional, registro de actividades físicas y seguimiento del bienestar del usuario.
Está desarrollada en Angular, con un enfoque modular, responsive y orientado a la experiencia de usuario.

NutriApp-Frontend forma parte del ecosistema completo de NutriApp, conectado a servicios externos de nutrición y un backend construido en Spring Boot.
      
##   -Características principales      

NutriApp ofrece:  

Interfaz moderna, limpia y responsiva

Gestión diaria de comidas y valores nutricionales

Conexión con APIs externas (FatSecret, FoodData Central, etc.)

Registro y seguimiento de actividades físicas

Calendario mensual/semanal interactivo

Dashboard nutricional dinámico

Login tradicional y con Google

Sistema de roles y rutas protegidas

Animaciones suaves, UX cuidada y performance optimizada


  ## -Tecnologías y componentes

NutriApp-Frontend está construido con un stack moderno:

Angular 20+

TypeScript

RxJS

Angular Router

Angular Signals

HTML5 / CSS3 (Responsive)

Google Fonts / Material Icons

Integración con APIs REST


 ## -Instalación

Para instalar NutriApp-Frontend desde el repositorio:

git clone https://github.com/JuaniVT/NutriApp-Frontend.git
cd NutriApp-Frontend
npm install


 ## -Ejecutar la aplicación

Iniciar el servidor de desarrollo:

ng serve


Luego abrir:

http://localhost:4200/


La aplicación se recarga automáticamente ante cualquier cambio.


 ## -Build de producción

Para generar los artefactos optimizados:

ng build --configuration production


Los archivos se generan en dist/.


 ## -Pruebas

Ejecutar tests unitarios:


  ng test


Ejecutar pruebas E2E (si están configuradas):

ng e2e

  
##  -Integración con el Backend

NutriApp-Frontend se conecta a un backend desarrollado en Spring Boot, utilizando:

Interceptores para autenticación con token

Servicios centralizados HTTP

Modelo nutrido de DTOs

Google Identity Services para login social

  
##  📁 Estructura del proyecto
src/
 ├── app/
 │    ├── components/
 │    ├── pages/
 │    ├── services/
 │    ├── guards/
 │    └── utils/
 ├── assets/
 ├── environments/
 └── index.html

  
 ## -Guía para contribuir

Si querés contribuir a NutriApp, seguí estas pautas:

Realizá un fork del repositorio.

Creá una rama para tu feature:

git checkout -b feature/nueva-funcionalidad


Hacé commit y push.

Abrí un Pull Request detallando la mejora.

NutriApp se adhiere a buenas prácticas de desarrollo open-source: claridad, documentación y revisiones cuidadosas.

  
 ## 👥 Autores y colaboradores

Equipo de desarrollo:

Zuri Uruzuna – Calendario, UI/UX, diseño responsive

Juan Ignacio – Autenticación, módulos core, gráficos

Ekian – Solicitudes, perfil, mejoras visuales

Otros colaboradores

  
 ## -Recursos adicionales

Documentación oficial de Angular

Guía de Angular CLI

Documentación APIs nutricionales

Blog y material del equipo

  
##  📄 Licencia

NutriApp-Frontend se distribuye bajo la licencia que defina el equipo.
(Agregar MIT, GPL, Apache 2.0, etc., según corresponda.)

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
>>>>>>> header
