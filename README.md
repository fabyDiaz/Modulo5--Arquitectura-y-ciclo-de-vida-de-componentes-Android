# Proyecto AlkeWallet Módulo 6

## Objetivo 

El primer objetivo es llevar a cabo la funcionalidad principal de la
 aplicación, que es permitir a los usuarios efectuar transacciones virtuales.
 Esto implica implementar la lógica de comunicación con la API REST
 utilizando Retrofit. <b>
 Otro objetivo importante es implementar la base de datos local utilizando
 Room. Esto permitirá almacenar datos en el dispositivo del usuario para
 garantizar un acceso rápido y sin conexión a información relevante, como
 el historial de transacciones y la información del perfil del usuario. <b>
 Es crucial integrar todas las librerías y componentes de manera coherente
 en la arquitectura MVVM. Esto implica separar claramente la lógica de
 presentación de los datos y la lógica de negocio. La View se encargará de la
 interfaz de usuario, el ViewModel será responsable de coordinar la
 comunicación con Retrofit para acceder a la API REST, con Room para
 acceder a la base de datos local y con la View para mostrar los datos al
 usuario, mientras que el Model contendrá los datos. <b>
La implementación debe garantizar una experiencia de usuario fluida y
 amigable. Esto incluye una navegación intuitiva entre las pantallas de la
 aplicación, así como la presentación eficiente de información y la carga de
 imágenes sin inconvenientes. 

 ## Implementación

  1. Registro e inicio de sesión: La aplicación permite a los
 usuarios registrarse y luego iniciar sesión con sus credenciales.<b>
 2. Historial de transacciones: La aplicación muestra un historial
 de transacciones, detallando la fecha, monto y ID de la transacción<b>
 3. Realización de transacciones virtuales: Los usuarios pueden 
 efectuar transacciones virtuales, indicando el monto y una
 descripción.<b>
 4. Almacenamiento local: La aplicación alamcena en una base
 de datos local el historial de transacciones y los detalles del perfil del
 usuario para un acceso sin conexión.<b>
 5. Carga de imágenes: Laimágendelperfil del usuario debe cargarse y
 mostrarse adecuadamente en la interfaz<b><b>

 **Consideraciones**<b>
 - Por el momento la app permite crear una cuenta e iniciar sesión, pero no guarda la sesión para poder ingresar sin conexión (Se está trabajando en ello) <b>
 - Se decide crear un nuevo usuario (SignUp Page) considerar que no tiene una cuenta aosicada, por lo que no podrá realizar transacciones. Para crear una cuenta y poder realizar transacciones debe ir al perfil (imagen superior derecha en homePage), ver tarjetas y presionar el botón. En ese momento se creará una nueva cuenta con un saldo inicial de $1000<b>
 - Si decide inicar sesión con una cuenta existente, puede intentar con las siguientes cuentas: <b>
 email: vicky@correo.com       password: abc123 <b>
 email: caro@correo.com        password: abc123 <b>
 - La app muestra el historial de transacciones, pero por facilidad, se muestra el ID de la transacción, fecha y monto. Esto debido a que la Api permite crear usuario, pero estos no necesariamente tienen una cuenta asociada. En el spinner tengo una lista de usuarios de la página 2 de la API a quienes les puedo hacer una transacción, pero como nosé si tienen cuenta, ni como acceder a ellas, decidi dejar fijo la cuenta 5 para realizar las transacciones. Por este motivo no se muestra el nombre del destinatario, ya que tampoco tienen relación con la cuenta. <b>
- La app permite cargar una imagen de perfil, pero por el momento no se guarda en ninguna parte, por lo que solo es de prueba ( Se está trajando para guardar la imagen en la base de datos local ya que la api no permite guardar imágenes) <b><b>

**Para cumplir con los requerimientos se utlizan las siguientes herramientas**<b>
Estos requerimientos funcionales guiarán la implementación técnica de la
 aplicación: <b>
 ● Librería Retrofit<b>
 ● Librería Room<b>
 ● Patrón de Arquitectura MVVM<b>
 ● Integración con Picasso<b>
 ● Gestión de sesiones y autenticación<b>
 ● Manejo de errores<b>
 ● Seguridad de datos<b>
● Pruebasunitarias y de integración<b>