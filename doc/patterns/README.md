# Patroiak Proiektua 26

Repositorio: YerayOrdoki/proiektua2. Rama: patroiak-proiektua26.
Base: 05510415231a40dfa3946b4ce976957e9edc9503.
Requisitos: patroiakProiektua26.pdf, paginas 1-5.

## Factory Method
Creator: BLFactory.Creator. Factory Method: create(). ConcreteCreator: LocalCreator y RemoteCreator. Product: BLFacade. ConcreteProduct: BLFacadeImplementation y RemoteFacade. BLFactory selecciona el Creator segun el booleano recibido de la presentacion. No es solo un if: cada Creator sobrescribe create. Se pueden inyectar creadores que devuelvan mocks de Mockito. ApplicationLauncher lee ConfigXML y obtiene la fachada mediante la factoria, sin construir DataAccess ni configurar SOAP. La red y la DB se resuelven fuera del EDT; las ventanas se crean dentro del EDT.

## Iterator
ExtendedIterator<T> extiende Iterator<T>. ListExtendedIterator copia la pertenencia a la lista, no clona los Sale. El cursor indica el elemento actual: next devuelve ese elemento e incrementa; previous lo devuelve y decrementa. goFirst establece 0; goLast establece size-1 para no omitir el ultimo elemento. No es la semantica de ListIterator. Mezclar next y previous sin reposicionar sigue ese mismo cursor, segun el comentario del PDF. El agotamiento lanza NoSuchElementException; remove no esta permitido. Se cubren listas vacias, un elemento, cambios de direccion y snapshot. BLFacade y BLFacadeImplementation exponen getSalesIterator(String desc) reutilizando getSales(desc). La sobrecarga sin parametros usa la busqueda vacia, por la inconsistencia entre firma y ejemplo del enunciado. PatternsDemo imprime primero el recorrido inverso y despues el directo.

## SOAP y compatibilidad
El iterador es estado del cliente y no se serializa por SOAP. RemoteBLFacade conserva las 33 operaciones originales y el nombre XML BLFacade. BLFacade extiende ese contrato y agrega la iteracion local. BLFacadeImplementation publica RemoteBLFacade. RemoteFacade delega las operaciones del servicio y hereda la construccion local del iterador. Es necesario reiniciar el servidor con el codigo de esta rama. Las pruebas con mocks no certifican SOAP extremo a extremo ni ObjectDB real. BLFacadeImplementation conserva sus operaciones anteriores, reformateadas, cambia endpointInterface y agrega getSalesIterator. No se corrigen otros problemas historicos de conexiones.

## Adapter
Target: TableModel. AbstractTableModel implementa parte de ese contrato. Adapter: SellerAdapter. Adaptee: Seller y sus Sale. Client: JTable, creada dentro de SellerTable. getRowCount corresponde al numero de ventas; getValueAt traduce los getters de Sale a celdas. Las columnas son Sale, Title, Description, Status, Price y Publication date; sus tipos son Integer, String, String, Integer, Float y Date, incluso sin filas. El modelo no permite editar y copia la pertenencia a la lista. Seller.sales es XmlTransient: el ejemplo remoto carga getSalesBySeller(email) y usa la sobrecarga que recibe la lista. isLogged devuelve Emaitza en este proyecto, no Seller; la demostracion resuelve el vendedor por email en lugar de copiar ese error del ejemplo del PDF. La extraccion textual del PDF no permite verificar las cabeceras de su imagen: se documentan expresamente las seis columnas elegidas.

## Ejecutar la aplicacion
Requisitos: JDK 11 y Maven. No se cambia config.xml ni se sobrescriben bases de datos. Para trabajar localmente, configurar businessLogic local="true". Para inicializar los datos del enunciado, usar initialize="true" en una copia de desarrollo, arrancar una vez y devolverlo a false. No se hace automaticamente porque puede afectar a datos existentes. En modo remoto, configurar host y puerto alcanzables, mantener local="false" y arrancar businessLogic.BusinessLogicServer antes del cliente. Los recursos y dependencias existentes del proyecto deben estar en el classpath de ejecucion.

```sh
mvn -B package -DskipTests
mvn -q org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass=patterns.demo.PatternsDemo -Dexec.args="seller1@gmail.com"
```

El argumento es el email de un vendedor existente; el segundo argumento opcional filtra las ventas del iterador. La ventana muestra todas las ventas del vendedor. Se puede ejecutar patterns.demo.PatternsDemo desde Eclipse con los mismos argumentos.

## Pruebas y evidencias
PatternsTest contiene ocho pruebas: ordenes y limites, vacio/un elemento, snapshot/cambio de direccion, seleccion de Creator con Mockito, busqueda local con DataAccess mock, iterador remoto cliente con mock SOAP, mapeo/tipos del Adapter y vendedores vacios/remotos. PatternsScreenshotTest agrega una prueba con datos deterministas no persistidos que ejecuta los ejemplos y captura ventanas Swing reales mediante Robot bajo Xvfb. No son imagenes inventadas ni capturas de los datos personales del usuario. Sin servidor grafico la prueba se omite y la entrega se rechaza como no validada.

```sh
xvfb-run -a mvn -B test -Dtest=PatternsTest,PatternsScreenshotTest
plantuml -charset UTF-8 -tpng doc/patterns/*.puml
pip install reportlab python-docx
python tools/build-patterns-docs.py
```

El workflow patterns-delivery comprueba los XML de Surefire: al menos nueve pruebas y cero fallos, errores u omisiones. Es importante porque el pom historico ignora fallos de test. Solo con las pruebas y capturas presentes genera patters.pdf en la raiz y doc/patterns/patroiak.docx, con UML ampliado, explicaciones, capturas y codigo fuente numerado. Guarda esos resultados automaticamente en esta misma rama, nunca en master. Los informes se conservan como artefactos de Actions incluso si falla la ejecucion. Un workflow preparado no equivale a una ejecucion exitosa: consultar validation.json y los informes generados.

## Entrega y OneDrive
El enunciado exige entregar la URL del proyecto y tener patters.pdf en el repositorio. El Word es adicional, compatible con Word de OneDrive. No se ha subido a OneDrive: la carga directa requiere conectar Microsoft 365. Los documentos de esta automatizacion solo se generan tras validar las pruebas; los borradores adjuntos anteriores no certifican ejecucion. Quedan fuera de la validacion automatica: SOAP extremo a extremo, ObjectDB real y coincidencia exacta de cabeceras con la imagen del enunciado.
