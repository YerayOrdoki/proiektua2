# Patroiak Proiektua 26

Repositorio: YerayOrdoki/proiektua2. Rama: patroiak-proiektua26.
Requisitos: patroiakProiektua26.pdf, paginas 1-5.

## Factory Method: version simplificada
El enunciado pide centralizar la obtencion de la logica de negocio en una factoria y mantener la decision local/remota en la presentacion. Se conserva la llamada del ejemplo: new BLFactory().getBusinessLogicFactory(isLocal).

BLFactory es el Creator. BLFacade es el Product. BLFacadeImplementation y RemoteFacade son los ConcreteProduct. getBusinessLogicFactory recibe el booleano y utiliza un if/else: si es true llama a createLocalBusinessLogic; si es false llama a createRemoteBusinessLogic. El primer metodo devuelve new BLFacadeImplementation(); el segundo devuelve RemoteFacade.connect(). Ambos devuelven BLFacade.

No hay clases Creator, LocalCreator ni RemoteCreator anidadas, atributos de creadores, constructores de inyeccion, Objects.requireNonNull ni operador ternario en BLFactory. Es una factoria parametrizada con dos metodos de creacion protegidos y sobrescribibles; no se presenta como una jerarquia productiva de creadores concretos. Esos dos metodos separan la construccion y permiten sustituirla en pruebas sin acceder a la DB ni a la red. La subclase anonima que devuelve mocks existe solo en la prueba, no en el codigo de la aplicacion.

El recorrido es: ApplicationLauncher lee ConfigXML, pide la fachada a BLFactory y la entrega a MainGUI. ApplicationLauncher ya no construye DataAccess ni prepara SOAP. Se mantiene el arranque Swing existente de esta rama para no mezclar cambios de interfaz con esta simplificacion.

## Iterator
ExtendedIterator<T> extiende Iterator<T> y agrega previous, hasPrevious, goFirst y goLast. ListExtendedIterator guarda una copia superficial de la lista y un cursor. goFirst lo situa en 0 y goLast en size-1. next devuelve el elemento actual e incrementa; previous lo devuelve y decrementa, siguiendo el comentario del enunciado. No es la semantica de ListIterator. Si se agota el recorrido se lanza NoSuchElementException; remove no se permite. No se cambian estas clases porque ya contienen solo la lista, el cursor y las operaciones solicitadas.

BLFacade y BLFacadeImplementation ofrecen getSalesIterator(String desc) reutilizando getSales(desc). La sobrecarga sin parametros usa busqueda vacia porque el ejemplo del PDF omite el argumento. PatternsDemo imprime del ultimo al primero y despues del primero al ultimo.

## Adapter
SellerAdapter extiende AbstractTableModel y adapta las ventas de Seller a JTable. Target: TableModel. Adapter: SellerAdapter. Adaptee: Seller y Sale. Client: JTable dentro de SellerTable. Una venta es una fila. getValueAt traduce getters a celdas; getColumnClass declara tipos sin leer una primera fila que podria no existir.

Las seis columnas son Sale, Title, Description, Status, Price y Publication date; sus tipos son Integer, String, String, Integer, Float y Date. El modelo es de solo lectura y copia la pertenencia a la lista. La extraccion textual del PDF no permite contrastar las cabeceras de su imagen, por lo que no se afirma una coincidencia visual exacta. No se reestructura este Adapter porque ya sigue directamente el esquema AbstractTableModel del material.

## Por que se mantiene la separacion SOAP
RemoteBLFacade conserva las 33 operaciones originales del servicio y el nombre XML BLFacade. BLFacade extiende ese contrato y agrega el iterador local. RemoteFacade delega las operaciones originales y construye el iterador en el cliente. Esta separacion evita intentar serializar un Iterator por SOAP y no se elimina solo para reducir lineas. El servidor debe reiniciarse con el codigo de esta rama. No se certifica SOAP extremo a extremo ni ObjectDB real mediante mocks.

Seller.sales es XmlTransient: en modo remoto PatternsDemo obtiene las ventas con getSalesBySeller(email) y las pasa a SellerTable. isLogged devuelve Emaitza en este proyecto, no Seller como supone el ejemplo del PDF; por eso la demostracion obtiene el vendedor por email.

## Ejecutar
Requisitos: JDK 11, Maven y los recursos originales del proyecto en el classpath. Se puede ejecutar patterns.demo.PatternsDemo desde Eclipse: primer argumento, email de un vendedor existente; segundo argumento opcional, filtro del iterador. La tabla muestra todas las ventas del vendedor.

No se cambia config.xml ni se sobrescriben bases de datos. Para negocio local poner businessLogic local="true". Para inicializar los datos del enunciado, utilizar initialize="true" en una copia de desarrollo, arrancar una vez y volver a false. Para negocio remoto poner local="false", configurar un host/puerto alcanzable y arrancar businessLogic.BusinessLogicServer antes del cliente.

## Pruebas y documentos
PatternsTest conserva ocho pruebas de Iterator, Factory, acceso local con mock, delegacion remota con mock y Adapter. La prueba de la factoria sobrescribe sus dos metodos protegidos con fachadas mock y comprueba que solo se llama al correspondiente. PatternsScreenshotTest agrega una prueba de ventanas Swing reales con fixtures no persistidos y captura con Robot bajo Xvfb. No se usan datos personales ni se inventan imagenes.

El workflow existente ejecuta las nueve pruebas, comprueba los XML de Surefire y rechaza fallos, errores u omisiones aunque el pom historico ignore fallos. Despues renderiza los tres UML y regenera patters.pdf, doc/patterns/patroiak.docx, capturas y validation.json en esta misma rama. No modifica master ni sube documentos a OneDrive. Tras este cambio debe comprobarse la nueva ejecucion: el resultado de una version anterior no valida esta version simplificada.

Para reproducir toda la entrega, usar los pasos del workflow .github/workflows/patterns-delivery.yml: pruebas bajo Xvfb, renderizado UML a nombres separados de las capturas y python tools/build-patterns-docs.py.

## Entrega y limites
El enunciado exige la URL del proyecto con patters.pdf en el repositorio. El documento contiene UML, codigo y explicaciones, y capturas de Iterator y Adapter. El Word es adicional y compatible con Word de OneDrive, pero no esta subido a Microsoft 365. No estan certificados la integracion SOAP real, ObjectDB real ni la coincidencia grafica exacta con la tabla del PDF.
